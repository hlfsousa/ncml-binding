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
import ucar.ma2.ArrayString;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;

public class ArrayStringConverter implements Converter<Object> {

    @Override
    public Array toArray(Object value, CDLVariable variableDecl) {
        return toArray(value);
    }

    @Override
    public Array toArray(Object value, CDLAttribute attributeDecl) {
        return toArray(value);
    }

    private Array toArray(Object value) {
        try {
            int[] lengths = ArrayUtils.shapeOf(value);
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
        // shape=1 to scalar
        if (toType.isArray()) {
            if (array.getDataType() == DataType.OBJECT) {
                Object javaArray = ArrayUtils.createArray(array.getShape(), String.class);
                IndexIterator idx = array.getIndexIterator();
                while (idx.hasNext()) {
                    String value = (String) idx.getObjectNext();
                    ArrayUtils.setElement(javaArray, idx.getCurrentCounter(), value);
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
    public boolean isApplicable(Array array) {
        if (array.getDataType() == DataType.OBJECT) {
            Object firstValue = array.getObject(array.getIndex());
            if (firstValue instanceof String) {
                return true;
            }
        }
        return array.getRank() > 0 && array.getDataType() == DataType.STRING;
    }

}
