package io.github.hlfsousa.ncml.schemagen.improvements.filtering;

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

import static io.github.hlfsousa.ncml.schemagen.improvements.filtering.NameUtils.findCommonPrefix;
import static io.github.hlfsousa.ncml.schemagen.improvements.filtering.NameUtils.findCommonSuffix;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class VariableAttributeFilter implements ElementFilter<Variable> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected List<String> commonValues = new ArrayList<>();
    protected List<String> absentValues = new ArrayList<>();
    protected String archetypeNameAttribute;
    private boolean sequentialMatching;
    
    @Override
    public List<Variable> apply(List<Variable> variableList, Properties properties) {
        logger.debug("filtering {} variables", variableList.size());
        List<Variable> archetypes = new ArrayList<>();
        for (int i = 0; i < variableList.size(); i++) {
            Variable baseVariable = variableList.get(i);
            logger.debug("looking into variable {}", baseVariable.getName());
            List<Variable> matchingVariables = new ArrayList<>();
            for (ListIterator<Variable> matchIterator = variableList.listIterator(i + 1); matchIterator.hasNext(); ) {
                Variable possibleMatch = matchIterator.next();
                if (attributesMatch(baseVariable, possibleMatch) && canCreateArchetypeName(baseVariable, possibleMatch, matchingVariables)) {
                    logger.debug("match found: {}", possibleMatch.getName());
                    matchIterator.remove(); // consume variable into archetype
                    matchingVariables.add(possibleMatch);
                } else if (sequentialMatching) {
                    break;
                }
            }
            if (matchingVariables.isEmpty()) {
                archetypes.add(baseVariable);
            } else {
                matchingVariables.add(0, baseVariable);
                archetypes.add(createArchetype(matchingVariables));
            }
        }
        return archetypes;
    }

    protected boolean canCreateArchetypeName(Variable baseVariable, Variable possibleMatch,
            List<Variable> matchingVariables) {
        if (archetypeNameAttribute != null) {
            Function<Variable, String> extractName = variable -> variable.getAttribute().stream()
                .filter(attribute -> attribute.getName().equals(archetypeNameAttribute))
                .findAny().map(attribute -> attribute.getValue()).orElse(null);
            
            List<String> names = new ArrayList<>(matchingVariables.size() + 2);
            names.add(extractName.apply(baseVariable));
            names.add(extractName.apply(possibleMatch));
            matchingVariables.stream().map(extractName).forEach(names::add);
            String prefix = findCommonPrefix(names);
            String suffix = findCommonSuffix(names);
            return !prefix.isEmpty() || !suffix.isEmpty();
        }
        return true;
    }

    protected boolean attributesMatch(Variable baseVariable, Variable possibleMatch) {
        return haveSameRank(baseVariable, possibleMatch)
                && declareSameAttributes(baseVariable, possibleMatch)
                && commonAttributeValuesMatch(baseVariable, possibleMatch)
                && absentAttributesNotFound(baseVariable, possibleMatch);
    }

    protected boolean absentAttributesNotFound(Variable baseVariable, Variable possibleMatch) {
        return absentValues.isEmpty() ||
                !(baseVariable.getAttribute().stream()
                        .filter(attribute -> absentValues.contains(attribute.getName()))
                        .findAny().isPresent()
                  || !possibleMatch.getAttribute().stream()
                        .filter(attribute -> absentValues.contains(attribute.getName()))
                        .findAny().isPresent());
    }

    protected boolean haveSameRank(Variable baseVariable, Variable possibleMatch) {
        String baseShape = baseVariable.getShape();
        if (baseShape == null) {
            baseShape = "";
        }
        String matchShape = possibleMatch.getShape();
        if (matchShape == null) {
            matchShape = "";
        }
        return baseShape.split("\\s++").length == matchShape.split("\\s++").length;
    }

    protected boolean commonAttributeValuesMatch(Variable baseVariable, Variable possibleMatch) {
        Map<String, String> baseAttributes = baseVariable.getAttribute().stream()
                .filter(attribute -> commonValues.contains(attribute.getName()))
                .collect(toMap(attribute -> attribute.getName(), attribute -> attribute.getValue()));
        Map<String, String> matchAttributes = possibleMatch.getAttribute().stream()
                .filter(attribute -> commonValues.contains(attribute.getName()))
                .collect(toMap(attribute -> attribute.getName(), attribute -> attribute.getValue()));
        
        boolean hasArchetypeNameAttribute = archetypeNameAttribute != null;
        if (hasArchetypeNameAttribute) {
            hasArchetypeNameAttribute = baseVariable.getAttribute().stream()
                    .filter(att -> att.getName().equals(archetypeNameAttribute)).findAny().isPresent();
            hasArchetypeNameAttribute &= possibleMatch.getAttribute().stream()
                    .filter(att -> att.getName().equals(archetypeNameAttribute)).findAny().isPresent();
        }
        
        if (!baseAttributes.keySet().containsAll(commonValues) || !hasArchetypeNameAttribute) {
            return false;
        }
        return baseAttributes.equals(matchAttributes);
    }

    protected boolean declareSameAttributes(Variable baseVariable, Variable possibleMatch) {
        Set<String> expectedAttributes = baseVariable.getAttribute().stream()
                .map(attribute -> attribute.getName()).collect(toSet());
        Set<String> matchAttributes = possibleMatch.getAttribute().stream()
                .map(attribute -> attribute.getName()).collect(toSet());
        return expectedAttributes.equals(matchAttributes);
    }

    protected Variable createArchetype(List<Variable> matchingVariables) {
        Variable archetype = matchingVariables.get(0);
        logger.debug("creating archetype based on {}", archetype.getName());
        // FIXME attribute types might be implicit, so they will be unknown when cleared
        //setAttributeTypes(matchingVariables);
        setMappedName(archetype, matchingVariables);
        clearUnequalAttributes(archetype, matchingVariables);
        return archetype;
    }

    private void setMappedName(Variable archetype, List<Variable> matchingVariables) {
        String archetypeName = null;
        if (archetypeNameAttribute != null) {
            List<String> archetypeNameValues = new ArrayList<>(matchingVariables.size());
            for (Variable var : matchingVariables) {
                for (Attribute att : var.getAttribute()) {
                    if (att.getName().equals(archetypeNameAttribute)) {
                        archetypeNameValues.add(att.getValue());
                        break;
                    }
                }
            }
            assert archetypeNameValues.size() == matchingVariables.size() : "some variable is missing "
                    + archetypeNameAttribute;
                    
            String prefix = findCommonPrefix(archetypeNameValues);
            if (prefix.isEmpty()) {
                String suffix = findCommonSuffix(archetypeNameValues);
                if (!suffix.isEmpty()) {
                    archetypeName = suffix;
                }
            } else {
                archetypeName = prefix;
            }
        }
        String regex = matchingVariables.stream().map(var -> var.getName()).collect(joining("|"));
        if (archetypeName == null) {
            archetypeName = regex.replace("|", "_or_");
        }
        archetype.setName(archetypeName.replace(' ', '_') + ":" + regex);
        logger.debug("archetype name is {}", archetype.getName());
    }
    
    protected void clearUnequalAttributes(Variable archetype, List<Variable> matchingVariables) {
        for (Attribute attribute : archetype.getAttribute()) {
            if (attribute.getValue() != null) {
                for (Variable otherVariable : matchingVariables) {
                    Attribute matchingAttribute = otherVariable.getAttribute().stream()
                            .filter(otherAttribute -> otherAttribute.getName().equals(attribute.getName()))
                            .findAny().get();
                    if (!attribute.getValue().equals(matchingAttribute.getValue())) {
                        attribute.setValue(null);
                        logger.debug("attribute cleared: {}", attribute.getName());
                        break;
                    }
                }
            }
        }
    }

    public VariableAttributeFilter withCommonValue(String attributeName) {
        commonValues.add(attributeName);
        return this;
    }

    public VariableAttributeFilter withNameBasedOn(String attributeName) {
        archetypeNameAttribute = attributeName;
        return this;
    }

    public VariableAttributeFilter withSequentialMatching(boolean sequentialMatching) {
        this.sequentialMatching = sequentialMatching;
        return this;
    }

    public VariableAttributeFilter withAbsentValue(String absentAttributeName) {
        this.absentValues.add(absentAttributeName);
        return this;
    }

}
