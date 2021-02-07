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

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.ArrayUtils;
import io.github.hlfsousa.ncml.io.Converter;
import ucar.ma2.Array;
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
        Array array = Array.factory(DataType.STRING, ArrayUtils.shapeOf(value));
        IndexIterator indexIterator = array.getIndexIterator();
        while (indexIterator.hasNext()) {
            indexIterator.next();
            indexIterator.setObjectCurrent(ArrayUtils.getElement(value, indexIterator.getCurrentCounter()));
        }
        return array;
    }

    @Override
    public Object toJavaObject(Array array, Class<? extends Object> toType) {
        return array.copyToNDJavaArray();
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
        return array.getRank() > 0 && array.getDataType() == DataType.STRING;
    }

}
