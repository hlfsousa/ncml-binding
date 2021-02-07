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

import java.util.Properties;

public abstract class AbstractNode {

    protected AbstractAttributeContainer parent;
    protected final Properties properties;

    public AbstractNode(AbstractAttributeContainer parent, Properties properties) {
        this.parent = parent;
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (parent != null) {
            fullName.append(parent.getFullName()).append('/');
        }
        fullName.append(getName());
        return fullName.toString();
    }

    protected String substitute(String category, String name, String defaultValue) {
        String key = String.format("%s.%s", category, name);
        return properties.getProperty(key, defaultValue);
    }

    public abstract String getName();

    public String camelCase(String str) {
        StringBuilder result = new StringBuilder();
        for (String part : str.split("_")) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }
        }
        return result.toString();
    }

    public String dromedaryCase(String str) {
        String camelCase = camelCase(str);
        return Character.toLowerCase(camelCase.charAt(0)) + camelCase.substring(1);
    }
    
    public String escapeLineBreaks(String str) {
        return str.replace("\n", "\\n");
    }

    public void initializeConfiguration(Properties initialConfiguration) {
        // set self name (full name without initial slash as key, simple name as value)
        String key = getFullName();
        if (!key.isEmpty()) {
            key = key.substring(1);
            initialConfiguration.setProperty(key, getName());
        }
    }

}
