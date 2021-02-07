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

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class SchemaWrapper extends AbstractGroupWrapper {

    private Netcdf schema;
    private String packageName;
    private String groupName;

    public SchemaWrapper(Netcdf schema, String packageName, String groupName, Properties properties) {
        super(null, properties);
        this.schema = schema;
        this.packageName = packageName;
        this.groupName = substitute("substitution", "/", groupName);
    }

    @Override
    public String getName() {
        return groupName;
    }
    
    @Override
    public String getFullName() {
        return "";
    }

    @Override
    public String getNameTag() {
        throw new IllegalStateException("This should not have been called for the root group!");
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public List<AbstractGroupWrapper> getGroups() {
        return schema.getEnumTypedefOrGroupOrDimension().stream().filter(element -> element instanceof Group)
                .map(element -> new GroupWrapper(this, (Group) element, packageName, properties))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dimension> getDimensions() {
        return schema.getEnumTypedefOrGroupOrDimension().stream().filter(element -> element instanceof Dimension)
                .map(element -> (Dimension) element).collect(Collectors.toList());
    }

    @Override
    protected List<Attribute> getAttributesImpl() {
        return schema.getEnumTypedefOrGroupOrDimension().stream().filter(element -> element instanceof Attribute)
                .map(element -> (Attribute) element).collect(Collectors.toList());
    }

    @Override
    public List<VariableWrapper> getVariables() {
        return schema.getEnumTypedefOrGroupOrDimension().stream().filter(element -> element instanceof Variable)
                .map(variable -> new VariableWrapper(this, properties, (Variable) variable))
                .collect(Collectors.toList());
    }

}
