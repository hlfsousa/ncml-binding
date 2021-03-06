package io.github.hlfsousa.ncml.io.converters;

import java.math.BigInteger;

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
import ucar.ma2.DataType;
import ucar.ma2.DataType.Signedness;
import ucar.ma2.IndexIterator;

/**
 * Converts <strong>primitive</strong> arrays.
 */
public class ArrayNumberConverter implements Converter<Object> {

    public static DataType getUnsignedType(Class<?> componentType) {
        if (componentType == short.class || componentType == Short.class) {
            return DataType.BYTE;
        } else if (componentType == int.class || componentType == Integer.class) {
            return DataType.SHORT;
        } else if (componentType == long.class || componentType == Long.class) {
            return DataType.INT;
        } else if (componentType == BigInteger.class) {
            return DataType.LONG;
        }
        throw new IllegalArgumentException(componentType.getName());
    }

    public static Number toUnsigned(Object signed) {
        if (signed instanceof Byte) {
            return (short) Byte.toUnsignedInt(((Number) signed).byteValue());
        }
        if (signed instanceof Short) {
            return Short.toUnsignedInt(((Number) signed).shortValue());
        }
        if (signed instanceof Integer) {
            return Integer.toUnsignedLong(((Number) signed).intValue());
        }
        if (signed instanceof Long) {
            return new BigInteger(Long.toUnsignedString(((Number) signed).longValue()));
        }
        throw new IllegalArgumentException(signed.getClass().getName());
    }

    @Override
    public Array toArray(Object value, CDLVariable variableDecl) {
        int[] shape = ArrayUtils.shapeOf(value);
        boolean empty = false;
        for (int length : shape) {
            if (length == 0) {
                empty = true;
            }
        }
        if (!empty) {
            if (variableDecl.unsigned()) {
                Array array = Array.factory(getUnsignedType(ArrayUtils.getComponentType(value.getClass())), shape);
                int[] address = new int[shape.length];
                IndexIterator idxIterator = array.getIndexIterator();
                do {
                    idxIterator.setObjectNext(ArrayUtils.getElement(value, address));
                } while (ArrayUtils.increment(address, shape));
                return array;
            } else {
                return Array.makeFromJavaArray(value);
            }
        } else {
            DataType dataType;
            if (variableDecl.dataType().isEmpty()) {
                dataType = DataType.getType(ArrayUtils.getComponentType(value.getClass()), variableDecl.unsigned());
            } else {
                dataType = DataType.getType(variableDecl.dataType())
                        .withSignedness(variableDecl.unsigned() ? Signedness.UNSIGNED : Signedness.SIGNED);
            }
            return Array.factory(dataType, shape);
        }
    }

    @Override
    public Array toArray(Object value, CDLAttribute attributeDecl) {
        int[] shape = ArrayUtils.shapeOf(value);
        boolean empty = false;
        for (int length : shape) {
            if (length == 0) {
                empty = true;
            }
        }
        if (!empty) {
            return Array.makeFromJavaArray(value, attributeDecl.unsigned());
        } else {
            DataType dataType = null;
            if (!attributeDecl.dataType().isEmpty()) {
                dataType = DataType.valueOf(attributeDecl.dataType());
            } else {
                Class<?> componentType = ArrayUtils.getComponentType(value.getClass());
                dataType = DataType.getType(componentType, attributeDecl.unsigned());
            }
            return Array.factory(dataType, shape);
        }
    }

    @Override
    public Object toJavaObject(Array array, Class<? extends Object> toType) {
        // shape=1 to scalar
        if (toType.isArray()) {
            int[] shape = array.getShape();
            Class<?> componentType = ArrayUtils.getComponentType(toType);
            Object javaArray = ArrayUtils.createArray(shape, componentType);
            boolean assumeUnsigned = array.getDataType().getPrimitiveClassType() != componentType;
            int[] address = new int[shape.length];
            IndexIterator idxIterator = array.getIndexIterator();
            do {
                Object element = idxIterator.getObjectNext();
                if (assumeUnsigned) {
                    element = toUnsigned(element);
                }
                ArrayUtils.setElement(javaArray, address, element);
            } while (ArrayUtils.increment(address, shape));
            return javaArray;
        } else {
            assert array.getSize() == 1 : array.shapeToString();
            return array.getObject(0);
        }
    }

    @Override
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
        return componentType.isPrimitive();
    }

    @Override
    public boolean isApplicable(Array array) {
        return array.getRank() > 0 && array.getDataType().isNumeric() || array.getDataType() == DataType.CHAR;
    }

}
