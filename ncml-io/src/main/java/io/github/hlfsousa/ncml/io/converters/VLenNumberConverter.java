package io.github.hlfsousa.ncml.io.converters;

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

import java.util.Arrays;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.ArrayUtils;
import io.github.hlfsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;

public class VLenNumberConverter implements Converter<Object> {

    @Override
    public Array toArray(Object value, CDLVariable variableDecl) {
        int[] shape = ArrayUtils.shapeOf(value);
        int[] fixedShape = Arrays.copyOf(shape, shape.length - 1);
        int[] address = new int[fixedShape.length];
        Array array = Array.factory(DataType.OBJECT, fixedShape);
        IndexIterator idxIterator = array.getIndexIterator();
        do {
            idxIterator.setObjectNext(Array.makeFromJavaArray(ArrayUtils.getElement(value, address)));
        } while (ArrayUtils.increment(address, fixedShape));
        return array;
    }

    @Override
    public Array toArray(Object value, CDLAttribute attributeDecl) {
        throw new UnsupportedOperationException("Attributes cannot hold VLEN values");
    }

    @Override
    public Object toJavaObject(Array array, Class<? extends Object> toType) {
        int[] fixedShape = array.getShape();
        int[] address = new int[fixedShape.length];
        Object javaArray = ArrayUtils.createArray(fixedShape, getComponentType(toType, fixedShape.length));
        for (IndexIterator idxIterator = array.getIndexIterator(); idxIterator.hasNext(); ArrayUtils.increment(address, fixedShape)) {
            Array vlenArray = (Array) idxIterator.getObjectNext();
            ArrayUtils.setElement(javaArray, address, vlenArray.copyToNDJavaArray());
        }
        return javaArray;
    }

    private Class<?> getComponentType(Class<?> toType, int rank) {
        Class<?> componentType = toType;
        for (int i = 0; i < rank; i++) {
            componentType = componentType.getComponentType();
        }
        return componentType;
    }

    @Override
    public boolean isApplicable(Object value) {
        int[] shape = ArrayUtils.shapeOf(value);
        if (shape.length <= 1) {
            return false;
        }
        int[] fixedShape = Arrays.copyOf(shape, shape.length - 1);
        int[] address = new int[fixedShape.length];
        int baseLength = shape[address.length];
        boolean typeChecked = false;
        do {
            Object vlenCandidate = ArrayUtils.getElement(value, address);
            if (!typeChecked && !(java.lang.reflect.Array.get(vlenCandidate, 0) instanceof Number)) {
                return false;
            }
            // TODO VLEN does not depend strictly on the array type, extract parameter
            if (java.lang.reflect.Array.getLength(vlenCandidate) != baseLength) {
                return true;
            }
        } while (ArrayUtils.increment(address, fixedShape));
        return false;
    }

    @Override
    public boolean isApplicable(Array array) {
        if (array.getDataType()  == DataType.OBJECT) {
            Object firstElement = array.getIndexIterator().getObjectNext();
            if (firstElement instanceof Array) {
                DataType dataType = ((Array) firstElement).getDataType();
                return dataType.isNumeric();
            }
        }
        return false;
    }

}
