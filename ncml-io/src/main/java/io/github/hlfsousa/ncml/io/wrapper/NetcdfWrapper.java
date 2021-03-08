package io.github.hlfsousa.ncml.io.wrapper;

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

import java.util.Map;

import io.github.hlfsousa.ncml.io.AttributeConventions;
import io.github.hlfsousa.ncml.io.RuntimeConfiguration;
import ucar.ma2.Array;
import ucar.nc2.CDMNode;
import ucar.nc2.Group;
import ucar.nc2.Variable;

/**
 * Wraps a group, granting access to the variables and attributes it contains.
 * 
 * @author Henrique Sousa
 */
public abstract class NetcdfWrapper {

    protected final Group group;
    protected static AttributeConventions attributeConventions = new AttributeConventions();
    protected final RuntimeConfiguration runtimeConfiguration;

    public NetcdfWrapper(Group group, RuntimeConfiguration runtimeConfiguration) {
        this.group = group;
        this.runtimeConfiguration = runtimeConfiguration;
    }

    public Group unwrap() {
        return group;
    }
    
    public static String getRuntimeName(CDMNode node, String childName, Map<String, String> runtimeProperties) {
        if (runtimeProperties != null) {
            String fullName = node.getFullName();
            String key = fullName.isEmpty() ? childName : (fullName + '/' + childName);
            if (runtimeProperties.containsKey(key)) {
                return runtimeProperties.get(key);
            }
        }
        return childName;
    }

    protected Array getNumericArray(Variable variable) {
        return attributeConventions.readNumericArray(variable);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Number> T unsigned(Number value) {
        if  (value == null) {
            return null;
        }
        if (value instanceof Byte) {
            return (T) (Integer) Byte.toUnsignedInt((Byte)value);
        }
        if (value instanceof Short) {
            return (T) (Integer) Short.toUnsignedInt((Short) value);
        }
        if (value instanceof Integer) {
            return (T) (Long) Integer.toUnsignedLong((Integer) value);
        }
        if (value instanceof Long) {
            return (T) new BigInteger(Long.toUnsignedString((Long) value));
        }
        throw new IllegalArgumentException("Unsupported type " + value.getClass());
    }

}
