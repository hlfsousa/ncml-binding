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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class VariableWrapper extends AbstractAttributeContainer {

    private final Variable variable;
    private final String name;
    private boolean mapped = false;

    public VariableWrapper(AbstractAttributeContainer parent, Properties properties, Variable variable) {
        super(parent, properties);
        this.variable = variable;
        if (variable.getName().matches("[a-zA-Z_0-9]+:.*")) {
            // mapped group
            String baseName = variable.getName().substring(0, variable.getName().indexOf(':'));
            this.name = substitute("substitution", parent.getFullName() + '/' + baseName, baseName);
            mapped = true;
        } else {
            this.name = substitute("substitution", parent.getFullName() + '/' + variable.getName(), variable.getName());
        }
    }
    
    public String getMapExpression() {
        return variable.getName().substring(variable.getName().indexOf(':') + 1);
    }

    public boolean isMapped() {
        return mapped;
    }

    public boolean isUnsigned() {
        return variable.getType().startsWith("u") || variable.getAttribute().stream()
                .filter(att -> att.getName().equals("_Unsigned")).findAny().isPresent();
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public String getPackageName() {
        return parent.getPackageName();
    }

    @Override
    public String getNameTag() {
        return variable.getName();
    }

    @Override
    public List<Dimension> getDimensions() {
        return findDimensions(variable.getShape());
    }

    private List<Dimension> findDimensions(String shape) {
        if (shape == null || shape.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String[] dimNames = shape.trim().split("\\s++");
        List<Dimension> dimensionList = new ArrayList<>(dimNames.length);
        for (String dimensionName : dimNames) {
            dimensionList.add(findDimension(dimensionName));
        }
        return dimensionList;
    }

    private Dimension findDimension(String name) {
        Dimension dimension = null;
        if (name.matches("\\d+")) {
            // anonymous dimension
            dimension = new Dimension();
            dimension.setIsShared(false);
            dimension.setIsUnlimited(false);
            dimension.setIsVariableLength(false);
            dimension.setLength(name);
            return dimension;
        }
        AbstractAttributeContainer parent = this.parent;
        do {
            for (Dimension candidate : parent.getDimensions()) {
                if (name.equals(candidate.getName())) {
                    return candidate;
                }
            }
        } while ((parent = parent.parent) != null);
        // dimension is not declared; this cannot happen with ncdump -h -x
        throw new IllegalArgumentException("Not a valid dimension in schema: " + name);
    }
    
    public String getShapeBrackets() {
        if (variable.getShape() == null) {
            return "";
        }
        if (Boolean.parseBoolean(properties.getProperty(NCMLCodeGenerator.SCALAR_DIMENSION, "false"))) {
            List<Dimension> dimensions = getDimensions();
            if (dimensions.size() == 1) {
                Dimension dim = dimensions.get(0);
                if (!dim.isIsUnlimited() && !dim.isIsVariableLength()) {
                    try {
                        int length = Integer.parseInt(dim.getLength());
                        if (length == 1) {
                            return "";
                        }
                    } catch (NumberFormatException | NullPointerException e) {
                        // unable to parse length (comment or undeclared), assume not 1
                        logger.debug("Unparseable length {} for dimension {} at {}",
                                dim.getLength(), dim.getName(), getFullName());
                    }
                }
            }
        }
        int rank = variable.getShape().split("\\s++").length;
        StringBuilder str = new StringBuilder(rank * 2);
        for (int i = 0; i < rank; i++) {
            str.append("[]");
        }
        return str.toString();
    }

    @Override
    protected List<Attribute> getAttributesImpl() {
        return variable.getAttribute();
    }

    @Override
    public String getName() {
        return name;
    }
    
    public String getScaledType() {
        final String scaleFactorAttributeName = properties.getProperty("attribute_conventions.scale_factor_attribute", "scale_factor");
        return getAttributes().stream()
                .filter(attributeWrapper -> attributeWrapper.getName().equals(scaleFactorAttributeName))
                .findAny().map(attributeWrapper -> attributeWrapper.getAttribute().getType())
                .orElse(getVariable().getType());
    }

    public boolean isScalar() {
        if (getDimensions().isEmpty()) {
            return true;
        } else if (getDimensions().size() == 1) {
            Dimension singleDimension = getDimensions().get(0);
            if ("1".equals(singleDimension.getLength()) && !singleDimension.isIsUnlimited() && !singleDimension.isIsVariableLength()
                    && Boolean.parseBoolean(properties.getProperty(NCMLCodeGenerator.SCALAR_DIMENSION))) {
                return true;
            }
        }
        return false;
    }

}
