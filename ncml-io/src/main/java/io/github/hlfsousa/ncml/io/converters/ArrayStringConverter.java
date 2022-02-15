package io.github.hlfsousa.ncml.io.converters;

import java.lang.reflect.Constructor;
import java.util.Arrays;

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

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.ArrayUtils;
import io.github.hlfsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayChar.StringIterator;
import ucar.ma2.ArrayString;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;

public class ArrayStringConverter implements Converter<Object> {

    @Override
    public Array toArray(Object value, CDLVariable variableDecl) {
        return toArray(value, variableDecl.shape().length == 0);
    }

    @Override
    public Array toArray(Object value, CDLAttribute attributeDecl) {
        return toArray(value, false); // TODO combine values?
    }

    private Array toArray(Object value, boolean scalar) {
        try {
            int[] lengths = ArrayUtils.shapeOf(value);
            if (lengths.length == 0 && !scalar) {
                lengths = new int[] { 1 };
                value = new String[] { String.valueOf(value) };
            }
            Class<?> arrayType = Class.forName(ArrayString.class.getName() + "$D" + lengths.length);
            Class<?>[] parameterTypes = new Class<?>[lengths.length];
            Arrays.fill(parameterTypes, int.class);
            Constructor<?> constructor = arrayType.getConstructor(parameterTypes);
            Object[] parameters = new Object[lengths.length];
            for (int i = 0; i < lengths.length; i++) {
                parameters[i] = lengths[i];
            }
            Array array = (Array) constructor.newInstance(parameters);
            IndexIterator indexIterator = array.getIndexIterator();
            while (indexIterator.hasNext()) {
                indexIterator.next();
                indexIterator.setObjectCurrent(ArrayUtils.getElement(value, indexIterator.getCurrentCounter()));
            }
            return array;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Object toJavaObject(Array array, Class<? extends Object> toType) {
        if (array.getDataType() == DataType.CHAR) {
            return fromCharArray((ArrayChar) array, toType);
        }
        return fromStringArray(array, toType);
    }

    private Object fromCharArray(ArrayChar array, Class<? extends Object> toType) {
        int[] rawShape = array.getShape();
        int[] stringShape = new int[rawShape.length - 1];
        System.arraycopy(rawShape, 0, stringShape, 0, stringShape.length);
        if (stringShape.length == 0) {
            // pseudo-scalar
            return new String[] { array.getString() };
        }
        Object javaArray = ArrayUtils.createArray(stringShape, String.class);
        int[] address = new int[stringShape.length];
        StringIterator iterator = array.getStringIterator();
        while (iterator.hasNext()) {
            ArrayUtils.setElement(javaArray, address, iterator.next());
        }
        return javaArray;
    }

    private Object fromStringArray(Array array, Class<? extends Object> toType) {
        // shape=1 to scalar
        if (toType.isArray()) {
            if (array.getDataType() == DataType.OBJECT) {
                /* Some people (insert adjectives) think it's okay to declare a 1-dimensional array and remove the
                 * dimension if there is only one item, making it a scalar value. If we expect an array but get a
                 * scalar, we work around it by saying the shape is 1. This should hammer things back on track. */
                int[] shape = array.getShape();
                if (array.getRank() == 0) {
                    shape = new int[] { 1 };
                }
                Object javaArray = ArrayUtils.createArray(shape, String.class);
                IndexIterator idx = array.getIndexIterator();
                while (idx.hasNext()) {
                    String value = (String) idx.getObjectNext();
                    ArrayUtils.setElement(javaArray, array.getRank() == 0 ? new int[] { 0 } : idx.getCurrentCounter(), value);
                }
                return javaArray;
            } else {
                return array.copyToNDJavaArray();
            }
        } else {
            assert array.getSize() == 1 : array.shapeToString();
            return array.getObject(0);
        }
    }
    
    @Override
    public boolean isApplicable(Object value, CDLAttribute attributeDecl) {
        if (attributeDecl.separator().length() == 0) {
            return false;
        }
        return value.getClass().isArray() && DataType.getType(attributeDecl.dataType()) == DataType.STRING
                && ArrayUtils.getComponentType(value.getClass()).isAssignableFrom(String.class);
    }
    
    @Override
    public boolean isApplicable(Object value, CDLVariable variableDecl) {
        if (variableDecl.shape().length == 0) {
            return false;
        }
        return DataType.getType(variableDecl.dataType()) == DataType.STRING
                && ArrayUtils.getComponentType(value.getClass()).isAssignableFrom(String.class);
    }

    public boolean isApplicable(Object value) {
        if (value instanceof Array) {
            return isApplicable((Array) value);
        }
        if (value == null || !value.getClass().isArray()) {
            return false;
        }
        Class<?> componentType = value.getClass();
        while (componentType.isArray()) {
            componentType = componentType.getComponentType();
        }
        return componentType == String.class;
    }

    @Override
    public boolean isApplicable(Array array, Class<?> toType) {
        if (toType.isArray() && ArrayUtils.getComponentType(toType).isAssignableFrom(String.class)) {
            if (array.getDataType() == DataType.OBJECT) {
                Object firstValue = array.getObject(array.getIndex());
                if (firstValue instanceof String) {
                    return true;
                }
            }
            return array.getRank() > 0 && array.getDataType() == DataType.STRING
                    || array.getRank() > 1 && array.getDataType() == DataType.CHAR;
        }
        return false;
    }

}
