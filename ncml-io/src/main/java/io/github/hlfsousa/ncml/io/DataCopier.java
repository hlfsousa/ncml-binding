package io.github.hlfsousa.ncml.io;

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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.hlfsousa.ncml.declaration.Variable;

/**
 * Copies the content of a model (which can be in a file) to a value object. 
 * @author Henrique Sousa
 *
 * @param <T> The interface that this instance is able to copy.
 */
public class DataCopier<T> {

    private Function<Class<?>, Class<?>> valueObjectTypeLocator;
    private Class<T> dataInterface;

    @SuppressWarnings("unchecked")
    public DataCopier() {
        if (getClass() == DataCopier.class) {
            throw new IllegalStateException("Either provide the interface type or create an empty anonymous class");
        }
        assert getClass().getSuperclass() == DataCopier.class : "not instantiated as new DataCopier<MyInterface>(){} ?";
        dataInterface = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public DataCopier(Class<T> dataInterface) {
        this.dataInterface = dataInterface;
    }
    
    public void setValueObjectTypeLocator(Function<Class<?>, Class<?>> valueObjectTypeLocator) {
        this.valueObjectTypeLocator = valueObjectTypeLocator;
    }

    @SuppressWarnings("unchecked")
    public T copy(T data, T copy) {
        if (valueObjectTypeLocator == null) {
            valueObjectTypeLocator = new DefaultValueObjectTypeLocator();
        }
        copy = (T) doCopy(data, copy, dataInterface);
        if (valueObjectTypeLocator instanceof DefaultValueObjectTypeLocator) {
            ((DefaultValueObjectTypeLocator) valueObjectTypeLocator).clearCache(); // pull up as dispose()?
        }
        return copy;
    }

    /**
     * Creates an in-memory copy of an object that implements a data interface.
     * 
     * @param data data to copy (in any type of representation)
     * @return in-memory copy
     */
    public T copy(T data) {
        return copy(data, null);
    }

    private Object doCopy(Object data, Object valueObject, Class<?> dataInterface) {
        Class<?> valueObjectType = locateValueObjectFor(dataInterface);
        try {
            if (valueObject == null) {
                valueObject = valueObjectType.newInstance();
            }
            copyProperties(data, valueObject, dataInterface);
            if (Variable.class.isAssignableFrom(dataInterface)) {
                copyProperties(data, valueObject, Variable.class);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to instantiate/copy " + valueObjectType + " to " + valueObject, e);
        }
        return valueObject;
    }

    @SuppressWarnings("unchecked")
    private void copyProperties(Object data, Object valueObject, Class<?> dataInterface) {
        BeanInfo beanInfo;
        try {
            // Class<?> stopClass = Object.class;
            beanInfo = Introspector.getBeanInfo(dataInterface);
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Unable to describe " + dataInterface, e);
        }
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            Object propertyValue;
            try {
                propertyValue = propertyDescriptor.getReadMethod().invoke(data);
            } catch (ReflectiveOperationException | IllegalArgumentException e) {
                throw new IllegalStateException(
                        "Unable to read attribute " + propertyDescriptor.getName() + " from " + data, e);
            }
            if (propertyValue == null
                    || (propertyValue instanceof Collection && ((Collection<?>) propertyValue).isEmpty())) {
                continue;
            }
            if (propertyDescriptor.getPropertyType().isInterface()) {
                Object propertyCopy = null;
                try {
                    propertyCopy = propertyDescriptor.getReadMethod().invoke(valueObject);
                } catch (Exception e) {
                    throw new IllegalStateException(
                            "Unable to retrieve the current value of property " + propertyDescriptor.getName());
                }
                if (propertyDescriptor.getPropertyType() == List.class) {
                    List<?> valueList = (List<?>) propertyValue;
                    Type valueType = ((ParameterizedType) propertyDescriptor.getReadMethod().getGenericReturnType())
                            .getActualTypeArguments()[0];
                    Class<?> valueClass;
                    if (valueType instanceof Class) {
                        valueClass = (Class<?>) valueType;
                    } else {
                        valueClass = (Class<?>) ((ParameterizedType) valueType).getRawType();
                    }
                    List<Object> copyList = propertyCopy == null ? new ArrayList<>(valueList.size())
                            : (List<Object>) propertyCopy;
                    for (Object valueItem : valueList) {
                        if (valueClass.isInterface()) {
                            copyList.add(doCopy(valueItem, null, valueClass));
                        } else {
                            // Dimension falls here
                            copyList.add(valueItem);
                        }
                    }
                    propertyCopy = copyList;
                } else if (propertyDescriptor.getPropertyType() == Map.class) {
                    // go through each key
                    Class<?> valueType = (Class<?>) ((ParameterizedType) ((ParameterizedType) propertyDescriptor
                            .getReadMethod().getGenericReturnType()).getActualTypeArguments()[1]).getRawType();
                    Map<String, ?> propertyMap = (Map<String, ?>) propertyValue;
                    Map<String, Object> copyMap = propertyCopy == null ? new LinkedHashMap<>()
                            : (Map<String, Object>) propertyCopy;
                    for (Entry<String, ?> valueEntry : propertyMap.entrySet()) {
                        Object valueCopy = copyMap.get(valueEntry.getKey());
                        valueCopy = doCopy(valueEntry.getValue(), valueCopy, valueType);
                        copyMap.put(valueEntry.getKey(), valueCopy);
                    }
                    propertyCopy = copyMap;
                } else {
                    propertyCopy = doCopy(propertyValue, propertyCopy, propertyDescriptor.getPropertyType());
                }
                try {
                    propertyDescriptor.getWriteMethod().invoke(valueObject, propertyCopy);
                } catch (ReflectiveOperationException | IllegalArgumentException e) {
                    throw new IllegalStateException(
                            "Unable to write attribute " + propertyDescriptor.getName() + " to " + propertyCopy);
                }
            } else {
                // this may be an array (Java or NetCDF), or a scalar value. No need to recurse
                // can it be anything else?
                try {
                    propertyDescriptor.getWriteMethod().invoke(valueObject, propertyValue);
                } catch (ReflectiveOperationException | IllegalArgumentException e) {
                    throw new IllegalStateException(
                            "Unable to write attribute " + propertyDescriptor.getName() + " to " + propertyValue);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    protected <V> Class<? extends V> locateValueObjectFor(Class<V> dataInterface) {
        return (Class<? extends V>) valueObjectTypeLocator.apply(dataInterface);
    }

    /**
     * Maps value object classes according to the default templates provided with this library.
     */
    private static class DefaultValueObjectTypeLocator implements Function<Class<?>, Class<?>> {

        private final Map<Class<?>, Class<?>> mappingCache = new HashMap<>();
        private static final Pattern VARIABLE_REGEX = Pattern.compile("^(.*)(\\$\\w+)Variable$");

        @Override
        public Class<?> apply(Class<?> dataInterface) {
            return mappingCache.computeIfAbsent(dataInterface, key -> {
                String intfName = dataInterface.getName();
                String valueObjectTypeName;
                if (dataInterface.getDeclaringClass() != null) {
                    Matcher matcher = VARIABLE_REGEX.matcher(intfName);
                    if (!matcher.matches()) {
                        throw new IllegalStateException("Unexpected variable interface " + intfName);
                    }
                    valueObjectTypeName = matcher.replaceFirst("$1VO$2VO");
                } else {
                    valueObjectTypeName = intfName + "VO";
                }
                try {
                    return Class.forName(valueObjectTypeName);
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Unable to locate VO class for " + dataInterface, e);
                }
            });
        }

        public void clearCache() {
            mappingCache.clear();
        }

    }
}
