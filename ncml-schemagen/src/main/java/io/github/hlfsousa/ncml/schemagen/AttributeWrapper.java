package io.github.hlfsousa.ncml.schemagen;

/*-
 * #%L
 * ncml-schemagen
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
import java.util.List;
import java.util.Properties;

import edu.ucar.unidata.netcdf.ncml.Attribute;

public class AttributeWrapper extends AbstractNode {

    private static final List<String> SPACE_SEPARATED_TYPES = Arrays.asList("boolean", "byte", "short", "int", "long", "float",
            "double");
    private static final String SPACE_SEPARATOR = " ";

    private Attribute attribute;
    private String name;

    public AttributeWrapper(AbstractAttributeContainer parent, Properties properties, Attribute attribute) {
        super(parent, properties);
        this.attribute = attribute;
        this.name = substitute("substitution", parent.getFullName() + "/@" + attribute.getName(), attribute.getName());
        // separator has to be set manually
        String value = attribute.getValue();
        if (value != null && !value.isEmpty()) {
            if (SPACE_SEPARATED_TYPES.contains(attribute.getType()) && value.contains(SPACE_SEPARATOR)
                    && (attribute.getSeparator() == null || attribute.getSeparator().isEmpty())) {
                attribute.setSeparator(SPACE_SEPARATOR);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getShapeBrackets() {
        if (attribute.getSeparator() == null) {
            return "";
        }
        // TBC can an attribute have more than one dimension?
        int rank = 1;
        StringBuilder str = new StringBuilder(rank * 2);
        for (int i = 0; i < rank; i++) {
            str.append("[]");
        }
        return str.toString();
    }

}
