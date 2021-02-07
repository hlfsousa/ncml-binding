package io.github.hlfsousa.ncml.io;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ucar.nc2.CDMNode;

public class RuntimeConfiguration {

    private final Map<String, String> simpleSubstitutions;
    private final Map<Pattern, String> regexSubstitutions;
    
    public RuntimeConfiguration(Map<String, String> runtimeProperties) {
        simpleSubstitutions = new HashMap<>();
        regexSubstitutions = new HashMap<>();

        for (Entry<String, String> entry : runtimeProperties.entrySet()) {
            if (entry.getKey().startsWith("regex.")) {
                Pattern regex = Pattern.compile(entry.getKey().substring("regex.".length()));
                regexSubstitutions.put(regex, entry.getValue());
            } else {
                simpleSubstitutions.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Retrieves the runtime name of a node, given its parent node.
     * 
     * @param node      the parent node
     * @param childName name of the child
     * @return substituted name, if present in the runtime configuration
     */
    public String getRuntimeName(CDMNode node, String childName) {
        /*
         * Substituting a property name is cheap unless there are numerous nodes in the NetCDF file, very few nodes are
         * present in the configuration -- most are left unmodified -- and a significant number of nodes are mapped
         * through regular expressions. However, regular expressions are the only way to change mapped properties.
         */
        String fullName = node.getFullName();
        String key = fullName.isEmpty() ? childName : (fullName + '/' + childName);
        if (simpleSubstitutions.containsKey(key)) {
            return simpleSubstitutions.get(key);
        } else {
            for (Entry<Pattern, String> regexEntry : regexSubstitutions.entrySet()) {
                Matcher matcher = regexEntry.getKey().matcher(key);
                if (matcher.matches()) {
                    return matcher.replaceAll(regexEntry.getValue());
                }
            }
        }
        return childName;
    }

}
