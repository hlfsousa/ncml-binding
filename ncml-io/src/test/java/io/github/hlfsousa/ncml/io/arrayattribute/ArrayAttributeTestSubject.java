package io.github.hlfsousa.ncml.io.arrayattribute;

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
import io.github.hlfsousa.ncml.annotation.CDLDimensions;
import io.github.hlfsousa.ncml.annotation.CDLRoot;

@CDLRoot
@CDLDimensions({
})
public interface ArrayAttributeTestSubject  {
    @CDLAttribute(name = "array_attribute", dataType = "int", defaultValue = "0 1 2 3 4 5 6 7 8 9")
    int[] getArrayAttribute();

    void setArrayAttribute(int[] arrayAttribute);

    @CDLAttribute(name = "str_attribute", dataType = "String", defaultValue = "foobar")
    String getStrAttribute();

    void setStrAttribute(String strAttribute);

    // methods >>
    // << methods

}
