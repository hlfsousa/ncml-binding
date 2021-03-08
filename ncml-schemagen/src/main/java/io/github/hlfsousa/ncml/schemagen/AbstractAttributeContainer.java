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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;

public abstract class AbstractAttributeContainer extends AbstractNode {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected String typeName;
    
    public AbstractAttributeContainer(AbstractAttributeContainer parent, Properties properties) {
        super(parent, properties);
    }
    
    public String getTypeName() {
        if (typeName == null) {
            typeName = camelCase(substitute(
                    "type", (parent != null ? parent.getFullName() : "") + '/' + getName(), getName()));
        }
        return typeName;
    }

    public abstract String getPackageName();

    public abstract String getNameTag();

    public abstract List<Dimension> getDimensions();

    public List<AttributeWrapper> getAttributes() {
        return getAttributesImpl().stream().map(attribute -> new AttributeWrapper(this, properties, attribute))
                .collect(Collectors.toList());
    }

    protected abstract List<Attribute> getAttributesImpl();

    @Override
    public void initializeConfiguration(Properties initialConfiguration) {
        super.initializeConfiguration(initialConfiguration);
        getAttributes().forEach(attribute -> attribute.initializeConfiguration(initialConfiguration));
    }

}
