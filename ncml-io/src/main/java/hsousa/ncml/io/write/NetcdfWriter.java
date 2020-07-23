package hsousa.ncml.io.write;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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

import hsousa.ncml.annotation.CDLAttribute;
import hsousa.ncml.annotation.CDLDimension;
import hsousa.ncml.annotation.CDLDimensions;
import hsousa.ncml.annotation.CDLGroup;
import hsousa.ncml.annotation.CDLRoot;
import hsousa.ncml.annotation.CDLVariable;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;
import ucar.nc2.Variable;

/**
 * 
 * @author Henrique Sousa
 *
 * @param <T> They
 */
// NetcdfFileWriter is deprecated but there is no practical substitution as of 5.3.2
@SuppressWarnings("deprecation")
public class NetcdfWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetcdfWriter.class);
    
    public NetcdfWriter() {
    }

    public NetcdfFile write(Object model, File location) throws IOException {
        CDLRoot rootAnnotation = getFullHierarchy(model).stream()
                .map(t -> t.getAnnotation(CDLRoot.class))
                .filter(a -> a != null)
                .findFirst().orElse(null);
        if (rootAnnotation == null) {
            throw new IllegalArgumentException("Object is not annotated with @CDLRoot");
        }
        LOGGER.debug("Writing to " + location);
        // assuming new file or overwrite
        NetcdfFileWriter writer = NetcdfFileWriter.createNew(Version.netcdf4, location.getAbsolutePath());
        // create root group
        Group rootGroup = writer.addGroup(null, null);
        LOGGER.debug("Creating structure");
        createStructure(writer, rootGroup, model);
        writer.create();
        LOGGER.debug("Writing content");
        writeContent(writer, rootGroup, model);
        LOGGER.debug("All done");
        return writer.getNetcdfFile();
    }
    
    private Set<Class<?>> getFullHierarchy(Object obj) {
        Set<Class<?>> hierarchy = new LinkedHashSet<>();
        for (Class<?> type = obj.getClass(); !type.equals(Object.class) && type != null; type = type.getSuperclass()) {
            hierarchy.add(type);
            hierarchy.addAll(Arrays.asList(type.getInterfaces()));
        }
        return hierarchy;
    }

    private void createStructure(NetcdfFileWriter writer, Group group, Object model) {
        createLocalDimensions(writer, group, model);
        createAttributes(writer, group, model);
        createVariables(writer, group, model);
        createGroups(writer, group, model);
    }

    private void createLocalDimensions(NetcdfFileWriter writer, Group group, Object model) {
        for (Class<?> intf : model.getClass().getInterfaces()) {
            List<CDLDimension> declaredDimensions = new ArrayList<>();
            CDLDimensions dimensions = intf.getAnnotation(CDLDimensions.class);
            if (dimensions != null) {
                declaredDimensions.addAll(Arrays.asList(dimensions.value()));
            }
            Optional.ofNullable(intf.getAnnotation(CDLDimension.class)).ifPresent(declaredDimensions::add);
            for (CDLDimension dimension : declaredDimensions) {
                final boolean variableLength = dimension.variableLength();
                int length = dimension.length();
                if (variableLength) {
                    length = -1;
                }
                writer.addDimension(group, dimension.name(), length, dimension.unlimited(), variableLength);
            }
        }
    }

    private void createAttributes(NetcdfFileWriter writer, Group group, Object model) {
        createAttributes(writer, model, attribute -> writer.addGroupAttribute(group, attribute));
    }

    private void createAttributes(NetcdfFileWriter writer, Object model, Consumer<Attribute> attributeConsumer) {
        forEachAccessor(model, accessor -> {
            CDLAttribute attributeDecl = accessor.getAnnotation(CDLAttribute.class);
            if (attributeDecl == null) {
                return;
            }
            String name = attributeDecl.name();
            if (name.isEmpty()) {
                name = getDefaultName(accessor);
            }
            Attribute.Builder attributeBuilder = Attribute.builder(name);
            try {
                Object value = accessor.invoke(model);
                if (value instanceof String) {
                    attributeBuilder.setStringValue((String) value);
                } else if (value instanceof Number) {
                    attributeBuilder.setNumericValue((Number) value, attributeDecl.unsigned());
                } else {
                    String dataType = attributeDecl.dataType();
                    if (!dataType.isEmpty()) {
                        attributeBuilder.setDataType(map(dataType));
                    }
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Unable to retrieve attribute value from accessor " + accessor, e);
            }
            attributeConsumer.accept(attributeBuilder.build());
        });
    }

    private DataType map(String dataType) {
        return dataType == null ? DataType.STRING : DataType.valueOf(dataType.toUpperCase());
    }
    
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
                    Pattern nameRegex = Pattern.compile(variableDecl.name().substring(variableDecl.name().indexOf(':') + 1));
                    Map<String, Object> map = (Map<String, Object>) varModel;
                    for (Entry<String, Object> entry : map.entrySet()) {
                        if (!nameRegex.matcher(entry.getKey()).matches()) {
                            throw new IllegalArgumentException(
                                    "Variable name " + entry.getKey() + " does not match definition " + variableDecl.name());
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

    private void createSingleVariable(NetcdfFileWriter writer, String name, Group group, Method accessor, CDLVariable variableDecl,
            Object varModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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
        if (varModel != null && hsousa.ncml.declaration.Variable.class.isAssignableFrom(variableType)) {
            final Method getValue = varModel.getClass().getMethod("getValue");
            varValue = getValue.invoke(varModel);
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
        if (varValue == null) {
            return;
        }
        DataType dataType;
        if (varValue instanceof Array) {
            dataType = ((Array)varValue).getDataType();
        } else {
            boolean unsigned = variableDecl.unsigned();
            dataType = DataType.getType(javaType, unsigned);
        }
        String[] shape = variableDecl.shape();
        String shapeStr = shape.length == 0 ? null : Arrays.stream(shape).collect(joining(" "));
        Variable variable = writer.addVariable(group, name, dataType, shapeStr);
        if (accessor.getReturnType().isInterface()) {
            createVariableStructure(writer, variable, varModel);
        }
    }

    private void createVariableStructure(NetcdfFileWriter writer, Variable variable, Object model) {
        // variables only have attributes
        createAttributes(writer, model, attribute -> writer.addVariableAttribute(variable, attribute));
    }

    private void createGroups(NetcdfFileWriter writer, Group group, Object model) {
        forEachAccessor(model, accessor -> {
            try {
                CDLGroup groupDecl = accessor.getAnnotation(CDLGroup.class);
                Object childModel;
                if (groupDecl == null || (childModel = accessor.invoke(model)) == null) {
                    return;
                }
                String name = groupDecl.name();
                if (name.isEmpty()) {
                    name = getDefaultName(accessor);
                }
                if (childModel instanceof Map) {
                    for (Entry<String, Object> entry : ((Map<String,Object>)childModel).entrySet()) {
                        Group childGroup = writer.addGroup(group, entry.getKey());
                        createStructure(writer, childGroup, entry.getValue());
                    }
                } else {
                    Group childGroup = writer.addGroup(group, name);
                    createStructure(writer, childGroup, childModel);
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
    
    private void writeVariables(NetcdfFileWriter writer, Group group, Object model) {
        forEachAccessor(model, accessor -> {
            CDLVariable variableDecl = accessor.getAnnotation(CDLVariable.class);
            if (variableDecl == null) {
                return;
            }
            try {
                LOGGER.debug("Evaluating variable bound to " + accessor.getDeclaringClass().getSimpleName() + '.' + accessor.getName());
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
        if (varModel != null && hsousa.ncml.declaration.Variable.class.isAssignableFrom(variableType)) {
            final Method getValue = varModel.getClass().getMethod("getValue");
            varValue = getValue.invoke(varModel);
        } else {
            varValue = varModel;
        }
        if (varValue == null) {
            LOGGER.debug("no value, nothing to write");
            return;
        }
        LOGGER.debug("actual var name to be written is " + name);
        Array ncArray;
        if (varValue instanceof Array) {
            ncArray = (Array) varValue;
        } else {
            ncArray = Array.factory(DataType.getType(varValue.getClass(), variableDecl.unsigned()), new int[] { 1 });
            ncArray.setObject(0, varValue);
        }
        writer.write(group.findVariable(name), ncArray);
    }

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
                        writeContent(writer, group.findGroup(entry.getKey()), entry.getValue());
                    }
                } else {
                    String name = groupDecl.name();
                    if (name.isEmpty()) {
                        name = getDefaultName(accessor);
                    }
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
