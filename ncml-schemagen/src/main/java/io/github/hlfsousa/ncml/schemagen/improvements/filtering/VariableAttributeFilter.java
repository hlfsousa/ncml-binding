package io.github.hlfsousa.ncml.schemagen.improvements.filtering;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(VariableAttributeFilter.class);

    private List<String> commonValues = new ArrayList<>();
    private List<String> absentValues = new ArrayList<>();
    private String archetypeNameAttribute;
    private boolean sequentialMatching;
    
    @Override
    public List<Variable> apply(List<Variable> variableList, Properties properties) {
        LOGGER.debug("filtering {} variables", variableList.size());
        List<Variable> archetypes = new ArrayList<>();
        for (int i = 0; i < variableList.size(); i++) {
            Variable baseVariable = variableList.get(i);
            LOGGER.debug("looking into variable {}", baseVariable.getName());
            List<Variable> matchingVariables = new ArrayList<>();
            for (ListIterator<Variable> matchIterator = variableList.listIterator(i + 1); matchIterator.hasNext(); ) {
                Variable possibleMatch = matchIterator.next();
                if (attributesMatch(baseVariable, possibleMatch) && canCreateArchetypeName(baseVariable, possibleMatch, matchingVariables)) {
                    LOGGER.debug("match found: {}", possibleMatch.getName());
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

    private boolean canCreateArchetypeName(Variable baseVariable, Variable possibleMatch,
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

    private boolean attributesMatch(Variable baseVariable, Variable possibleMatch) {
        return haveSameRank(baseVariable, possibleMatch)
                && declareSameAttributes(baseVariable, possibleMatch)
                && commonAttributeValuesMatch(baseVariable, possibleMatch)
                && absentAttributesNotFound(baseVariable, possibleMatch);
    }

    private boolean absentAttributesNotFound(Variable baseVariable, Variable possibleMatch) {
        return absentValues.isEmpty() ||
                !(baseVariable.getAttribute().stream()
                        .filter(attribute -> absentValues.contains(attribute.getName()))
                        .findAny().isPresent()
                  || !possibleMatch.getAttribute().stream()
                        .filter(attribute -> absentValues.contains(attribute.getName()))
                        .findAny().isPresent());
    }

    private boolean haveSameRank(Variable baseVariable, Variable possibleMatch) {
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

    private boolean commonAttributeValuesMatch(Variable baseVariable, Variable possibleMatch) {
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

    private boolean declareSameAttributes(Variable baseVariable, Variable possibleMatch) {
        Set<String> expectedAttributes = baseVariable.getAttribute().stream()
                .map(attribute -> attribute.getName()).collect(toSet());
        Set<String> matchAttributes = possibleMatch.getAttribute().stream()
                .map(attribute -> attribute.getName()).collect(toSet());
        return expectedAttributes.equals(matchAttributes);
    }

    private Variable createArchetype(List<Variable> matchingVariables) {
        Variable archetype = matchingVariables.get(0);
        LOGGER.debug("creating archetype based on {}", archetype.getName());
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
        LOGGER.debug("archetype name is {}", archetype.getName());
    }
    
    private void clearUnequalAttributes(Variable archetype, List<Variable> matchingVariables) {
        for (Attribute attribute : archetype.getAttribute()) {
            if (attribute.getValue() != null) {
                for (Variable otherVariable : matchingVariables) {
                    Attribute matchingAttribute = otherVariable.getAttribute().stream()
                            .filter(otherAttribute -> otherAttribute.getName().equals(attribute.getName()))
                            .findAny().get();
                    if (!attribute.getValue().equals(matchingAttribute.getValue())) {
                        attribute.setValue(null);
                        LOGGER.debug("attribute cleared: {}", attribute.getName());
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
