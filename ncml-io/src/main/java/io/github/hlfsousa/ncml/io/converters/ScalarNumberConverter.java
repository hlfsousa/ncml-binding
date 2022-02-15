package io.github.hlfsousa.ncml.io.converters;

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
import io.github.hlfsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Index;

public class ScalarNumberConverter implements Converter<Number> {

    private static final int[] SCALAR = new int[] {1};

    @Override
    public Array toArray(Number value, CDLVariable variableDecl) {
        return toArray(value, variableDecl.unsigned(), variableDecl.shape().length == 0);
    }
    
    @Override
    public Array toArray(Number value, CDLAttribute attributeDecl) {
        return toArray(value, attributeDecl.unsigned(), true);
    }

    private Array toArray(Number value, boolean unsigned, boolean scalar) {
        DataType dataType = unsigned ? ArrayNumberConverter.getUnsignedType(value.getClass())
                : DataType.getType(value.getClass());
        Array numericArray;
        if (scalar) {
            numericArray = Array.factory(dataType, new int[0]);
            numericArray.setObject(Index.scalarIndexImmutable, value);
        } else {
            numericArray = Array.factory(dataType, SCALAR);
            numericArray.setObject(0, value);
        }
        if (unsigned) {
            numericArray.setUnsigned(true);
        }
        return numericArray;
    }

    @Override
    public Number toJavaObject(Array array, Class<? extends Number> toType) {
        Number value = (Number) array.getObject(Index.scalarIndexImmutable);
        boolean assumeUnsigned = !toType.isInstance(value);
        if (assumeUnsigned) {
            value = (Number) ArrayNumberConverter.toUnsigned(value);
        }
        return value;
    }

    @Override
    public boolean isApplicable(Object value, CDLAttribute attributeDeclaration) {
        return value instanceof Number && DataType.getType(attributeDeclaration.dataType()).isNumeric();
    }

    @Override
    public boolean isApplicable(Object value, CDLVariable variableDeclaration) {
        return value instanceof Number && DataType.getType(variableDeclaration.dataType()).isNumeric();
    }

    @Override
    public boolean isApplicable(Array array, Class<?> toType) {
        return array.getDataType().isNumeric() && (array.getRank() == 0 || Arrays.equals(array.getShape(), SCALAR))
                && Number.class.isAssignableFrom(toType);
    }

}
