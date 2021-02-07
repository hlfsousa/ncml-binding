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

public class ScalarStringConverter implements Converter<String> {

    @Override
    public Array toArray(String value, CDLVariable variableDecl) {
        return toArray(value);
    }

    @Override
    public Array toArray(String value, CDLAttribute attributeDecl) {
        return toArray(value);
    }

    private Array toArray(String value) {
        DataType dataType = DataType.STRING;
        Array scalarArray = Array.factory(dataType, new int[0]);
        scalarArray.setObject(Index.scalarIndexImmutable, value);
        return scalarArray;
    }

    @Override
    public String toJavaObject(Array array, Class<? extends String> toType) {
        return (String) array.getObject(Index.scalarIndexImmutable);
    }

    @Override
    public boolean isApplicable(String value) {
        return true;
    }

    @Override
    public boolean isApplicable(Array array) {
        return array.getDataType() == DataType.STRING;
    }

}
