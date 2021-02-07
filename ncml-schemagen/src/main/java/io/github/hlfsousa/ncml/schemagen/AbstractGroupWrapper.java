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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class AbstractGroupWrapper extends AbstractAttributeContainer {

    private List<AttributeWrapper> consolidatedVariableAttributes;
    private static final String[] TYPE_ORDER = { "boolean", "ubyte", "byte", "char", "ushort", "short", "int", "uint",
            "ulong", "long", "float", "double", "string", "String" };

    public AbstractGroupWrapper(AbstractGroupWrapper parent, Properties properties) {
        super(parent, properties);
    }

    public abstract List<AbstractGroupWrapper> getGroups();

    public abstract List<VariableWrapper> getVariables();

    public boolean isMapped() {
        return false;
    }

    public List<AttributeWrapper> getConsolidatedVariableAttributes() {
        if (consolidatedVariableAttributes == null) {
            List<AttributeWrapper> allAttributes = getVariables().stream()
                    .flatMap(varContainer -> varContainer.getAttributes().stream()).collect(Collectors.toList());
            Map<String, List<AttributeWrapper>> attributeMap = new LinkedHashMap<>();
            for (AttributeWrapper attribute : allAttributes) {
                attributeMap.computeIfAbsent(attribute.getName(), k -> new ArrayList<>()).add(attribute);
            }
            consolidatedVariableAttributes = new ArrayList<>(attributeMap.size());
            for (Entry<String, List<AttributeWrapper>> mapEntry : attributeMap.entrySet()) {
                consolidatedVariableAttributes.add(pickWidestAttribute(mapEntry.getValue()));
            }
        }
        return consolidatedVariableAttributes;

    }

    private AttributeWrapper pickWidestAttribute(List<AttributeWrapper> attributeList) {
        int minOrder = TYPE_ORDER.length;
        AttributeWrapper selectedAttribute = null;
        for (AttributeWrapper wrapper : attributeList) {
            String type = wrapper.getAttribute().getType();
            int idx = Arrays.binarySearch(TYPE_ORDER, type);
            if (minOrder > idx) {
                selectedAttribute = wrapper;
                minOrder = idx;
            }
        }
        return selectedAttribute;
    }

    @Override
    public void initializeConfiguration(Properties initialConfiguration) {
        super.initializeConfiguration(initialConfiguration);
        getGroups().forEach(group -> group.initializeConfiguration(initialConfiguration));
        getVariables().forEach(variable -> variable.initializeConfiguration(initialConfiguration));
    }

}
