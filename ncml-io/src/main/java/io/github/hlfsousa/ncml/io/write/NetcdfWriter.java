package io.github.hlfsousa.ncml.io.write;

/*-
 * #%L
 * ncml-io
 * %%
 * Copyright (C) 2020 - 2021 Henrique L. F. de Sousa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.beanutils.ConvertUtils.convert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLDimension;
import io.github.hlfsousa.ncml.annotation.CDLDimensions;
import io.github.hlfsousa.ncml.annotation.CDLGroup;
import io.github.hlfsousa.ncml.annotation.CDLRoot;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.ArrayUtils;
import io.github.hlfsousa.ncml.io.AttributeConventions;
import io.github.hlfsousa.ncml.io.AttributeConventions.ArrayScaling;
import io.github.hlfsousa.ncml.io.ConvertUtils;
import io.github.hlfsousa.ncml.io.RuntimeConfiguration;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.DataType.Signedness;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.CDMNode;
import ucar.nc2.Dimension;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;
import ucar.nc2.Variable;

/**
 * Writes annotated models to a given location.
 * 
 * @author Henrique Sousa
 */
// NetcdfFileWriter is deprecated but there is no practical substitution as of 5.3.2
@SuppressWarnings("deprecation")
public class NetcdfWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetcdfWriter.class);
    
    private static final AttributeConventions ATTRIBUTE_CONVENTIONS = new AttributeConventions();

    private ConvertUtils convertUtils = ConvertUtils.getInstance();
    private RuntimeConfiguration runtimeConfiguration;

    private boolean defaultAttributeValueUsed;
    private boolean closeAfterCreation;
    
    public NetcdfWriter() {
        this(true);
    }
    
    public NetcdfWriter(RuntimeConfiguration runtimeConfiguration) {
        this(true, runtimeConfiguration);
    }
    
    public NetcdfWriter(boolean closeAfterCreation) {
        this(closeAfterCreation, new RuntimeConfiguration(Collections.emptyMap()));
    }
    
    public NetcdfWriter(boolean closeAfterCreation, RuntimeConfiguration runtimeConfiguration) {
        this.closeAfterCreation = closeAfterCreation;
        this.runtimeConfiguration = runtimeConfiguration;
    }
    
    public void setDefaultAttributeValueUsed(boolean defaultAttributeValueUsed) {
        this.defaultAttributeValueUsed = defaultAttributeValueUsed;
    }
    
    public boolean isDefaultAttributeValueUsed() {
        return defaultAttributeValueUsed;
    }

    public NetcdfFile write(Object model, File location) throws IOException {
        CDLRoot rootAnnotation = getFullHierarchy(model).stream()
                .map(t -> t.getAnnotation(CDLRoot.class))
                .filter(a -> a != null)
                .findFirst().orElse(null);
        if (rootAnnotation == null) {
            throw new IllegalArgumentException("Object is not annotated with @CDLRoot");
        }
        
        Map<String,List<Dimension>> declaredDimensions = new HashMap<>();
        collectDimensions(model, declaredDimensions, "/");
        
        LOGGER.debug("Writing to " + location);
        // assuming new file or overwrite
        NetcdfFileWriter writer = NetcdfFileWriter.createNew(Version.netcdf4, location.getAbsolutePath());
        // create root group
        Group rootGroup = writer.addGroup(null, null);
        LOGGER.debug("Creating structure");
        createStructure(writer, rootGroup, model, declaredDimensions);
        writer.create();
        LOGGER.debug("Created structure:\n{}", writer.getNetcdfFile());
        LOGGER.debug("Writing content");
        writeContent(writer, rootGroup, model);
        LOGGER.debug("All done");
        NetcdfFile netcdfFile = writer.getNetcdfFile();
        if (closeAfterCreation) {
            netcdfFile.close();
        }
        return netcdfFile;
    }
    
    @SuppressWarnings("unchecked")
    private void collectDimensions(Object model, Map<String, List<Dimension>> declaredDimensions, String localPath) {
        for (Class<?> type : getFullHierarchy(model)) {
            List<CDLDimension> dimensionsList = new ArrayList<>();
            Optional.ofNullable(type.getAnnotation(CDLDimensions.class))
                    .ifPresent(dims -> dimensionsList.addAll(Arrays.asList(dims.value())));
            Optional.ofNullable(type.getAnnotation(CDLDimension.class)).ifPresent(dimensionsList::add);
            for (CDLDimension localDimension : dimensionsList) {
                declaredDimensions.computeIfAbsent(localPath, key -> new ArrayList<>())
                        .add(Dimension.builder()
                                .setName(localDimension.name())
                                .setIsUnlimited(localDimension.unlimited())
                                .setIsVariableLength(localDimension.variableLength())
                                .setIsShared(!localDimension.variableLength())
                                .setLength(localDimension.variableLength() ? -1 : Math.max(localDimension.length(), 1))
                                .build());
            }
        }
        
        forEachAccessor(model, accessor -> {
            CDLGroup groupAnnotation = accessor.getAnnotation(CDLGroup.class);
            if (groupAnnotation != null) {
                // is it a group? collect dimensions and recurse
                try {
                    Object value = accessor.invoke(model);
                    if (value instanceof Map) {
                        for (Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                            collectDimensions(entry.getValue(), declaredDimensions, localPath + entry.getKey() + '/');
                        }
                    } else if (value != null) {
                        String name = groupAnnotation.name();
                        if (name.isEmpty()) {
                            name = accessor.getName().substring("get".length());
                        }
                        collectDimensions(value, declaredDimensions, localPath + name + '/');
                    }
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException(e);
                }
                
                return; // accessor dealt with
            }
            CDLVariable variableAnnotation = accessor.getAnnotation(CDLVariable.class);
            if (variableAnnotation != null) {
                Object value;
                try {
                    value = accessor.invoke(model);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
                if (value instanceof Map) {
                    Map<String, ?> valueMap = (Map<String, ?>) value;
                    for (Entry<String, ?> varEntry : valueMap.entrySet()) {
                        Object entryValue = varEntry.getValue();
                        if (!(entryValue instanceof io.github.hlfsousa.ncml.declaration.Variable)) {
                            return;
                        }
                        collectVariableDimensions(declaredDimensions, localPath, variableAnnotation, entryValue);
                    }
                    return;
                }
                else if (!(value instanceof io.github.hlfsousa.ncml.declaration.Variable)) {
                    return;
                }
                
                collectVariableDimensions(declaredDimensions, localPath, variableAnnotation, value);
                return;
            }
        });
        
    }

    private void collectVariableDimensions(Map<String, List<Dimension>> declaredDimensions, String localPath,
            CDLVariable variableAnnotation, Object value) {
        io.github.hlfsousa.ncml.declaration.Variable<?> variable = (io.github.hlfsousa.ncml.declaration.Variable<?>) value;
        if (variable.getDimensions() != null) {
            for (Dimension dimension : variable.getDimensions()) {
                updateDimension(declaredDimensions, localPath, dimension);
            }
        } else if (variableAnnotation.shape().length > 0) {
            Object varValue = variable.getValue();
            if (varValue != null) {
                if (variableAnnotation.shape().length > 0) {
                    for (int i = 0; i < variableAnnotation.shape().length; i++) {
                        String name = variableAnnotation.shape()[i];
                        int length = varValue.getClass().isArray() ? java.lang.reflect.Array.getLength(varValue) : 1;
                        if (name.matches("\\d+") && !name.equals(String.valueOf(length))) {
                            throw new IllegalStateException("Immutable dimension at " + localPath);
                        }
                        Dimension declared = findDimension(name, declaredDimensions, localPath);
                        if (declared.isVariableLength()) {
                            continue; // variable length must be -1
                        }
                        updateDimension(declaredDimensions, localPath, new Dimension(name, length,
                                declared.isShared() && !declared.isVariableLength(), declared.isUnlimited(),
                                declared.isVariableLength()));
                        if (varValue.getClass().isArray()) {
                            varValue = java.lang.reflect.Array.get(varValue, 0); // to get next dimension
                        }
                    }
                } else if (varValue instanceof Array) {
                    
                }
            }
        }
    }

    private Dimension findDimension(String name, Map<String, List<Dimension>> declaredDimensions, String localPath) {
        String scope = localPath;
        while (!scope.equals("")) {
            List<Dimension> dimensionsInScope = declaredDimensions.computeIfAbsent(scope, key -> new ArrayList<>());
            for (Dimension existing : dimensionsInScope) {
                if (existing.getShortName().equals(name)) {
                    return existing;
                }
            }
            scope = scope.replaceAll("[^/]*+/$", "");
        }
        return null;
    }
    
    private void updateDimension(Map<String, List<Dimension>> declaredDimensions, String localPath,
            Dimension dimension) {
        String scope = localPath;
        while (!scope.equals("")) {
            List<Dimension> dimensionsInScope = declaredDimensions.computeIfAbsent(scope, key -> new ArrayList<>());
            boolean found = false;
            for (Iterator<Dimension> dimIterator = dimensionsInScope.iterator(); dimIterator.hasNext() && !found; ) {
                Dimension existing = dimIterator.next();
                if (existing.getShortName().equals(dimension.getShortName())) {
                    // TODO use equals in version 6
                    // assume all properties but size are equal
                    if (!existing.isVariableLength() && existing.getLength() != dimension.getLength()) {
                        LOGGER.debug("Dropping declared dimension for replacement: {}", existing);
                        dimIterator.remove();
                        found = true;
                    } else {
                        return;
                    }
                }
            }
            if (found) {
                // add dimension to list
                LOGGER.debug("Adding dimension {} to scope {}", dimension, scope);
                dimensionsInScope.add(dimension);
                return;
            }
            scope = scope.replaceAll("[^/]*+/$", "");
        }
        
        List<Dimension> dimensionsInScope = declaredDimensions.computeIfAbsent(localPath, key -> new ArrayList<>());
        LOGGER.debug("Adding dimension {} to scope {}", dimension, scope);
        dimensionsInScope.add(dimension);

    }

    private Set<Class<?>> getFullHierarchy(Object obj) {
        Set<Class<?>> hierarchy = new LinkedHashSet<>();
        for (Class<?> type = obj.getClass(); !type.equals(Object.class) && type != null; type = type.getSuperclass()) {
            hierarchy.add(type);
            hierarchy.addAll(Arrays.asList(type.getInterfaces()));
        }
        return hierarchy;
    }

    private void createStructure(NetcdfFileWriter writer, Group group, Object model,
            Map<String, List<Dimension>> declaredDimensions) {
        createAttributes(writer, group, model);
        createLocalDimensions(writer, group, model, declaredDimensions);
        createVariables(writer, group, model);
        createGroups(writer, group, model, declaredDimensions);
    }

    private void createLocalDimensions(NetcdfFileWriter writer, Group group, Object model,
            Map<String, List<Dimension>> declaredDimensions) {
        String fullName = group.getFullName() + "/";
        if (!fullName.startsWith("/")) {
            fullName = '/' + fullName;
        }
        List<Dimension> dimensions = declaredDimensions.get(fullName);
        if (dimensions != null) {
            dimensions.stream().filter(Dimension::isShared).forEach(group::addDimension);
        }
    }

    private void createAttributes(NetcdfFileWriter writer, Group group, Object model) {
        createAttributes(writer, model, group, attribute -> writer.addGroupAttribute(group, attribute));
    }

    private void createAttributes(NetcdfFileWriter writer, Object model, CDMNode parent, Consumer<Attribute> attributeConsumer) {
        forEachAccessor(model, accessor -> {
            CDLAttribute attributeDecl = accessor.getAnnotation(CDLAttribute.class);
            if (attributeDecl == null) {
                return;
            }
            String name = attributeDecl.name();
            if (name.isEmpty()) {
                name = getDefaultName(accessor);
            }
            name = runtimeConfiguration.getRuntimeName(parent, name);
            Attribute.Builder attributeBuilder = Attribute.builder(name);
            if (attributeDecl.dataType() != null && !attributeDecl.dataType().isEmpty()) {
                DataType dataType = DataType.getType(attributeDecl.dataType());
                if (attributeDecl.unsigned()) {
                    dataType = DataType.getType(dataType.getPrimitiveClassType(), true);
                }
                attributeBuilder.setDataType(dataType);
            }
            try {
                Object value = accessor.invoke(model);
                if (value == null && defaultAttributeValueUsed) {
                    value = attributeDecl.defaultValue();
                    if (!accessor.getReturnType().isInstance(value)) {
                        value = convert(value, accessor.getReturnType());
                    }
                }
                if (value == null) {
                    return;
                }
                attributeBuilder.setValues(convertUtils.toArray(value, attributeDecl));
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Unable to retrieve attribute value from accessor " + accessor, e);
            }
            attributeConsumer.accept(attributeBuilder.build());
        });
    }

    @SuppressWarnings("unchecked")
    private void createVariables(NetcdfFileWriter writer, Group group, Object model) {
        forEachAccessor(model, accessor -> {
            CDLVariable variableDecl = accessor.getAnnotation(CDLVariable.class);
            if (variableDecl == null) {
                return;
            }
            try {
                Object varModel = accessor.invoke(model);
                if (varModel instanceof Map) {
                    // variable is mapped, validate names
                    Pattern nameRegex = Pattern.compile(runtimeConfiguration.getRuntimeName(group,
                            variableDecl.name().substring(variableDecl.name().indexOf(':') + 1)));
                    Map<String, Object> map = (Map<String, Object>) varModel;
                    for (Entry<String, Object> entry : map.entrySet()) {
                        if (!nameRegex.matcher(entry.getKey()).matches()) {
                            throw new IllegalArgumentException("Variable name " + entry.getKey()
                                    + " does not match definition " + variableDecl.name() + " (runtime: "
                                    + nameRegex.pattern() + ")");
                        }
                        createSingleVariable(writer, entry.getKey(), group, accessor, variableDecl, entry.getValue());
                    }
                } else {
                    String name = variableDecl.name();
                    if (name.isEmpty()) {
                        name = getDefaultName(accessor);
                    }
                    createSingleVariable(writer, name, group, accessor, variableDecl, varModel);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Unable to declare variable from accessor " + accessor, e);
            }
        });
    }

    private void createSingleVariable(NetcdfFileWriter writer, String name, Group group, Method accessor,
            CDLVariable variableDecl, Object varModel)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        name = runtimeConfiguration.getRuntimeName(group, name);
        Object varValue;
        Class<?> javaType;
        Class<?> variableType = accessor.getReturnType();
        if (Map.class.isAssignableFrom(variableType)) {
            Type genericType = ((ParameterizedType)accessor.getGenericReturnType()).getActualTypeArguments()[1];
            if (genericType instanceof ParameterizedType) {
                variableType = (Class<?>) ((ParameterizedType)genericType).getRawType();
            } else {
                variableType = (Class<?>) genericType;
            }
        }
        if (varModel != null && io.github.hlfsousa.ncml.declaration.Variable.class.isAssignableFrom(variableType)) {
            final Method getValue = varModel.getClass().getMethod("getValue");
            varValue = getValue.invoke(varModel);
            if (varValue == null) {
                return;
            }
            javaType = getValue.getReturnType();
            if (javaType == Object.class) {
                javaType = varValue.getClass();
            }
        } else {
            varValue = varModel;
            javaType = accessor.getReturnType();
            if (Map.class.isAssignableFrom(javaType)) {
                Type valueType = ((ParameterizedType)accessor.getGenericReturnType()).getActualTypeArguments()[1];
                javaType = (valueType instanceof Class) ? (Class<?>) valueType : null;
            }
        }
        if (varValue == null || ArrayUtils.isEmpty(varValue)) {
            LOGGER.debug("no value for {}, skipping variable", name);
            return;
        }
        DataType dataType = getDataType(variableDecl, varValue, javaType);
        String[] shape = variableDecl.shape();
        if (varModel instanceof io.github.hlfsousa.ncml.declaration.Variable) {
            List<Dimension> dimensions = ((io.github.hlfsousa.ncml.declaration.Variable<?>)varModel).getDimensions();
            if (dimensions != null) {
                shape = dimensions.stream().map(Dimension::getShortName).collect(toList())
                        .toArray(new String[0]);
            }
        }
        if (shape.length > 1 && group.findDimension(shape[shape.length - 1]) == null) {
            shape[shape.length - 1] = "*";
        }
        String shapeStr = shape.length == 0 ? null : Arrays.stream(shape).collect(joining(" "));
        Variable variable = writer.addVariable(group, name, dataType, shapeStr);
        if (accessor.getReturnType().isInterface()) {
            createVariableStructure(writer, variable, varModel);
        }
    }

    private DataType getDataType(CDLVariable variableDecl, Object varValue, Class<?> javaType) {
        DataType dataType = null;
        if (!variableDecl.dataType().isEmpty()) {
            // this will fail for unsigned types as they do not match ncml-2.2.xsd
            dataType = DataType.getType(variableDecl.dataType())
                    .withSignedness(variableDecl.unsigned() ? Signedness.UNSIGNED : Signedness.SIGNED);
        } else if (varValue instanceof Array) {
            /* if this is a scaled value, the type should be explicit. Otherwise we would have to
             * look into the missing value attribute for the implicit type and... well, that's not
             * done here. */
            dataType = ((Array)varValue).getDataType();
        } else {
            boolean unsigned = variableDecl.unsigned();
            dataType = DataType.getType(javaType, unsigned);
        }
        return dataType;
    }

    private void createVariableStructure(NetcdfFileWriter writer, Variable variable, Object model) {
        // variables only have attributes
        createAttributes(writer, model, variable, attribute -> writer.addVariableAttribute(variable, attribute));
    }

    @SuppressWarnings("unchecked")
    private void createGroups(NetcdfFileWriter writer, Group group, Object model,
            Map<String, List<Dimension>> declaredDimensions) {
        forEachAccessor(model, accessor -> {
            try {
                CDLGroup groupDecl = accessor.getAnnotation(CDLGroup.class);
                Object childModel;
                if (groupDecl == null || (childModel = accessor.invoke(model)) == null) {
                    return;
                }
                if (childModel instanceof Map) {
                    for (Entry<String, Object> entry : ((Map<String,Object>)childModel).entrySet()) {
                        String name = runtimeConfiguration.getRuntimeName(group, entry.getKey());
                        // validate group name?
                        Pattern nameRegex = Pattern.compile(runtimeConfiguration.getRuntimeName(group,
                                groupDecl.name().substring(groupDecl.name().indexOf(':') + 1)));
                        if (!nameRegex.matcher(name).matches()) {
                            throw new IllegalStateException("Group name " + name + " does not match definition "
                                    + groupDecl.name() + " (runtime " + nameRegex.pattern() + ")");
                        }
                        Group childGroup = writer.addGroup(group, name);
                        createStructure(writer, childGroup, entry.getValue(), declaredDimensions);
                    }
                } else {
                    String name = groupDecl.name();
                    if (name.isEmpty()) {
                        name = getDefaultName(accessor);
                    }
                    name = runtimeConfiguration.getRuntimeName(group, name);
                    Group childGroup = writer.addGroup(group, name);
                    createStructure(writer, childGroup, childModel, declaredDimensions);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Unable to read metadata to create group from method " + accessor, e);
            }
        });
    }

    private void writeContent(NetcdfFileWriter writer, Group group, Object model) {
        writeVariables(writer, group, model);
        writeChildGroups(writer, group, model);
    }
    
    @SuppressWarnings("unchecked")
    private void writeVariables(NetcdfFileWriter writer, Group group, Object model) {
        forEachAccessor(model, accessor -> {
            CDLVariable variableDecl = accessor.getAnnotation(CDLVariable.class);
            if (variableDecl == null) {
                return;
            }
            try {
                LOGGER.debug("Evaluating variable bound to " + accessor.getDeclaringClass().getSimpleName() + '.'
                        + accessor.getName());
                Object varModel = accessor.invoke(model);
                if (varModel instanceof Map) {
                    // names were validated before
                    Map<String, Object> map = (Map<String, Object>) varModel;
                    for (Entry<String, Object> entry : map.entrySet()) {
                        writeSingleVariable(writer, entry.getKey(), group, accessor, variableDecl, entry.getValue());
                    }
                } else {
                    String name = variableDecl.name();
                    if (name.isEmpty()) {
                        name = getDefaultName(accessor);
                    }
                    writeSingleVariable(writer, name, group, accessor, variableDecl, varModel);
                }
            } catch (ReflectiveOperationException | IOException | InvalidRangeException e) {
                throw new IllegalStateException("Unable to write variable from accessor " + accessor, e);
            }
        });
    }

    private void writeSingleVariable(NetcdfFileWriter writer, String name, Group group, Method accessor,
            CDLVariable variableDecl, Object varModel) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, IOException, InvalidRangeException {
        name = runtimeConfiguration.getRuntimeName(group, name);
        Object varValue;
        Class<?> variableType = accessor.getReturnType();
        if (Map.class.isAssignableFrom(variableType)) {
            Type genericType = ((ParameterizedType)accessor.getGenericReturnType()).getActualTypeArguments()[1];
            if (genericType instanceof ParameterizedType) {
                variableType = (Class<?>) ((ParameterizedType)genericType).getRawType();
            } else {
                variableType = (Class<?>) genericType;
            }
        }
        if (varModel != null && io.github.hlfsousa.ncml.declaration.Variable.class.isAssignableFrom(variableType)) {
            final Method getValue = varModel.getClass().getMethod("getValue");
            varValue = getValue.invoke(varModel);
        } else {
            varValue = varModel;
        }
        if (varValue == null || ArrayUtils.isEmpty(varValue)) {
            LOGGER.debug("no value, nothing to write");
            return;
        }
        LOGGER.debug("actual var name to be written is " + name);
        final Variable variable = group.findVariable(name);
        if (variable == null) {
            LOGGER.debug("variable was not created during createStructure(...): {}", name);
            return;
        }
        
        try {
        Array ncArray;
        if (varValue instanceof Array) {
            ncArray = (Array)varValue;
        } else {
            ncArray = convertUtils.toArray(varValue, variableDecl);
        }
        
        if (ncArray != null) {
            ncArray = ATTRIBUTE_CONVENTIONS.transformVariableValue(variable, ncArray, ArrayScaling.TO_RAW);
        }
        
        writer.write(variable, ncArray);
        } catch (Exception e) {
            throw new IllegalStateException("Error writing variable " + variable.getShortName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private void writeChildGroups(NetcdfFileWriter writer, Group group, Object model) {
        forEachAccessor(model, accessor -> {
            try {
                CDLGroup groupDecl = accessor.getAnnotation(CDLGroup.class);
                Object childModel;
                if (groupDecl == null || (childModel = accessor.invoke(model)) == null) {
                    return;
                }
                if (childModel instanceof Map) {
                    for (Entry<String,Object> entry : ((Map<String,Object>)childModel).entrySet()) {
                        String childGroupName = runtimeConfiguration.getRuntimeName(group, entry.getKey());
                        writeContent(writer, group.findGroup(childGroupName), entry.getValue());
                    }
                } else {
                    String name = groupDecl.name();
                    if (name.isEmpty()) {
                        name = getDefaultName(accessor);
                    }
                    name = runtimeConfiguration.getRuntimeName(group, name);
                    writeContent(writer, group.findGroup(name), childModel);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Unable to read metadata to create group from method " + accessor, e);
            }
        });
    }

    private void forEachAccessor(Object model, Consumer<Method> consumer) {
        Class<?>[] implementedInterfaces = model.getClass().getInterfaces();
        for (Class<?> intf : implementedInterfaces) {
            for (Method method : intf.getMethods()) {
                if (method.getName().startsWith("get") && method.getName().length() > "get".length()) {
                    consumer.accept(method);
                }
            }
        }
    }
    
    private String getDefaultName(Method accessor) {
        return accessor.getName().substring("get".length());
    }

}
