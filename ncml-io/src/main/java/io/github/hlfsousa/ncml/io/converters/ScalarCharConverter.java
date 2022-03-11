package io.github.hlfsousa.ncml.io.converters;

/*-
 * #%L
 * ncml-io
 * %%
 * Copyright (C) 2020 - 2022 Henrique L. F. de Sousa
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
import ucar.ma2.ArrayChar;
import ucar.ma2.DataType;
import ucar.ma2.Index;

public class ScalarCharConverter implements Converter<Character> {

    @Override
    public Array toArray(Character value, CDLVariable variableDecl) {
        ArrayChar array;
        if (variableDecl.shape().length == 0) {
            array = toScalarArray(value);
        } else {
            array = toArrayD1(value);
        }
        return array;
    }

    @Override
    public Array toArray(Character value, CDLAttribute attributeDecl) {
        return toScalarArray(value);
    }

    private ArrayChar toArrayD1(Character value) {
        ArrayChar array;
        array = new ArrayChar.D1(1);
        array.getIndexIterator().setCharNext(value);
        return array;
    }

    private ArrayChar toScalarArray(Character value) {
        ArrayChar array;
        array = new ArrayChar.D0();
        array.setChar(Index.scalarIndexImmutable, value);
        return array;
    }

    @Override
    public Character toJavaObject(Array array, Class<? extends Character> toType) {
        return array.getIndexIterator().getCharNext();
    }

    @Override
    public boolean isApplicable(Object value, CDLVariable variableDecl) {
        return value instanceof Character && DataType.getType(variableDecl.dataType()) == DataType.CHAR;
    }

    @Override
    public boolean isApplicable(Object value, CDLAttribute attributeDecl) {
        return value instanceof Character && DataType.getType(attributeDecl.dataType()) == DataType.CHAR;
    }

    @Override
    public boolean isApplicable(Array array, Class<?> toType) {
        return toType == char.class || toType == Character.class;
    }

}
