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
import io.github.hlfsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Index;

public class ScalarNumberConverter implements Converter<Number> {

    @Override
    public Array toArray(Number value, CDLVariable variableDecl) {
        return toArray(value, variableDecl.unsigned(), variableDecl.shape().length == 0);
    }
    
    @Override
    public Array toArray(Number value, CDLAttribute attributeDecl) {
        return toArray(value, attributeDecl.unsigned(), true);
    }

    private Array toArray(Number value, boolean unsigned, boolean scalar) {
        DataType dataType = DataType.getType(value.getClass());
        Array numericArray;
        if (scalar) {
            numericArray = Array.factory(dataType, new int[0]);
            numericArray.setObject(Index.scalarIndexImmutable, value);
        } else {
            numericArray = Array.factory(dataType, new int[] {1});
            numericArray.setObject(0, value);
        }
        return numericArray;
    }

    @Override
    public Number toJavaObject(Array array, Class<? extends Number> toType) {
        Number value = (Number) array.getObject(Index.scalarIndexImmutable);
        /*
         * For now, we are assuming that value and toType are compatible. Value is necessarily not a primitive. toType
         * can be a primitive. What if the value is not compatible with toType? We will eventually get a
         * ClassCastException.
         */
        return value;
    }

    @Override
    public boolean isApplicable(Number value) {
        return true;
    }

    @Override
    public boolean isApplicable(Array array) {
        return array.getRank() == 0;
    }

}
