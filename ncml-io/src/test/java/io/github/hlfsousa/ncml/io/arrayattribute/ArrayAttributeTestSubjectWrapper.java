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

import java.util.Optional;

import io.github.hlfsousa.ncml.io.RuntimeConfiguration;
import io.github.hlfsousa.ncml.io.wrapper.NetcdfWrapper;
import ucar.nc2.Group;

public class ArrayAttributeTestSubjectWrapper extends NetcdfWrapper implements ArrayAttributeTestSubject {

    public ArrayAttributeTestSubjectWrapper(Group group, RuntimeConfiguration runtimeConfiguration) {
        super(group, runtimeConfiguration);
    }

    @Override
    public int[] getArrayAttribute() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "array_attribute")))
                .map(arrayAttribute -> (int[])arrayAttribute.getValues().copyTo1DJavaArray())
                .orElse(null);
    }

    @Override
    public void setArrayAttribute(int[] arrayAttribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStrAttribute() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "str_attribute")))
                .map(strAttribute -> strAttribute.getStringValue())
                .orElse(null);
    }

    @Override
    public void setStrAttribute(String strAttribute) {
        throw new UnsupportedOperationException();
    }

    // additionalMethods >>
    // << additionalMethods

}
