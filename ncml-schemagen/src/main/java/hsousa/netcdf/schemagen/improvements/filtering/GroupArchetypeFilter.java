package hsousa.netcdf.schemagen.improvements.filtering;

import static hsousa.netcdf.schemagen.improvements.filtering.NameUtils.*;
import static java.util.stream.Collectors.*;
import static hsousa.ncml.schema.NcmlSchemaUtil.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class GroupArchetypeFilter implements ElementFilter<Group> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupArchetypeFilter.class);

    @Override
    public List<Group> apply(List<Group> groupList) {
        LOGGER.debug("filtering {} groups", groupList.size());
        List<Group> archetypes = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            Group prototypeGroup = groupList.get(i);
            LOGGER.debug("looking into {}", prototypeGroup.getName());
            List<Group> matchingGroups = new ArrayList<>();
            for (ListIterator<Group> matchIterator = groupList.listIterator(i + 1); matchIterator.hasNext(); ) {
                Group possibleMatch = matchIterator.next();
                if (structureMatch(prototypeGroup, possibleMatch)) {
                    LOGGER.debug("match found: {}", possibleMatch.getName());
                    matchIterator.remove(); // consume variable into archetype
                    matchingGroups.add(possibleMatch);
                }
            }
            if (matchingGroups.isEmpty()) {
                archetypes.add(prototypeGroup);
            } else {
                matchingGroups.add(0, prototypeGroup);
                archetypes.add(createArchetype(matchingGroups));
            }
        }
        return archetypes;
    }

    private boolean structureMatch(Group prototype, Group candidate) {
        return attributesMatch(prototype, candidate)
                && dimensionsMatch(prototype, candidate)
                && variablesMatch(prototype, candidate);
    }

    private boolean attributesMatch(Group prototype, Group candidate) {
        Map<String, Attribute> prototypeAttributes = mapChildren(prototype.getEnumTypedefOrDimensionOrVariable(),
                Attribute.class, att -> att.getName());
        Map<String, Attribute> candidateAttributes = mapChildren(candidate.getEnumTypedefOrDimensionOrVariable(),
                Attribute.class, att -> att.getName());
        return attributesMatch(prototypeAttributes, candidateAttributes);
    }
    
    private boolean attributesMatch(Map<String, Attribute> prototypeAttributes, Map<String, Attribute> candidateAttributes) {
        if (!prototypeAttributes.keySet().equals(candidateAttributes.keySet())) {
            return false;
        }

        for (String name : prototypeAttributes.keySet()) {
            Attribute prototypeAttribute = prototypeAttributes.get(name);
            Attribute candidateAttribute = candidateAttributes.get(name);

            if (!inferAttributeType(prototypeAttribute).equalsIgnoreCase(inferAttributeType(candidateAttribute))) {
                return false;
            }

        }

        return true;
    }

    private boolean dimensionsMatch(Group prototype, Group candidate) {
        Map<String, Dimension> prototypeDimensions = mapChildren(prototype.getEnumTypedefOrDimensionOrVariable(),
                Dimension.class, att -> att.getName());
        Map<String, Dimension> candidateDimensions = mapChildren(candidate.getEnumTypedefOrDimensionOrVariable(),
                Dimension.class, att -> att.getName());

        if (!prototypeDimensions.keySet().equals(candidateDimensions.keySet())) {
            return false;
        }

        for (String name : prototypeDimensions.keySet()) {
            Dimension prototypeDimension = prototypeDimensions.get(name);
            Dimension candidateDimension = candidateDimensions.get(name);

            if (prototypeDimension.isIsShared() != candidateDimension.isIsShared()
                    || prototypeDimension.isIsVariableLength() != candidateDimension.isIsVariableLength()
                    || prototypeDimension.isIsUnlimited() != candidateDimension.isIsUnlimited()) {
                return false;
            }

        }

        return true;
    }

    private boolean variablesMatch(Group prototype, Group candidate) {
        Map<String, Variable> prototypeVariables = mapChildren(prototype.getEnumTypedefOrDimensionOrVariable(),
                Variable.class, att -> att.getName());
        Map<String, Variable> candidateVariables = mapChildren(candidate.getEnumTypedefOrDimensionOrVariable(),
                Variable.class, att -> att.getName());

        if (!prototypeVariables.keySet().equals(candidateVariables.keySet())) {
            return false;
        }

        for (String name : prototypeVariables.keySet()) {
            Variable prototypeVariable = prototypeVariables.get(name);
            Variable candidateVariable = candidateVariables.get(name);

            if (!variableRankMatch(prototypeVariable, candidateVariable)
                    || !variableAttributesMatch(prototypeVariable, candidateVariable)) {
                return false;
            }
        }

        return true;
    }

    private boolean variableRankMatch(Variable prototypeVariable, Variable candidateVariable) {
        String prototypeShape = prototypeVariable.getShape();
        int prototypeRank = prototypeShape == null || prototypeShape.isEmpty() ? 0
                : prototypeShape.split("\\s++").length;
        String candidateShape = candidateVariable.getShape();
        int candidateRank = candidateShape == null || candidateShape.isEmpty() ? 0
                : candidateShape.split("\\s++").length;
        
        return prototypeRank == candidateRank;
    }

    private boolean variableAttributesMatch(Variable prototypeVariable, Variable candidateVariable) {
        Map<String, Attribute> prototypeAttributes = mapChildren(prototypeVariable.getAttribute(),
                Attribute.class, att -> att.getName());
        Map<String, Attribute> candidateAttributes = mapChildren(prototypeVariable.getAttribute(),
                Attribute.class, att -> att.getName());
        
        return attributesMatch(prototypeAttributes, candidateAttributes);
    }

    private Group createArchetype(List<Group> matchingGroups) {
        // definition: "to clear" something is to set the value (attribute) or length (dimension) to null
        String archetypeName = createArchetypeName(matchingGroups);
        Group archetype = matchingGroups.get(0);
        List<Group> otherGroups = matchingGroups.subList(1, matchingGroups.size());
        clearAttributes(archetype, otherGroups);
        clearDimensions(archetype, otherGroups);
        clearVariables(archetype, otherGroups);
        archetype.setName(archetypeName);
        return archetype;
    }

    private String createArchetypeName(List<Group> matchingGroups) {
        List<String> names = matchingGroups.stream().map(group -> group.getName()).collect(toList());
        String regex = names.stream().collect(joining("|"));
        String commonPart = findCommonPrefix(names);
        if (commonPart == null || commonPart.isEmpty()) {
            commonPart = findCommonSuffix(names);
        }
        return commonPart + ":" + regex;
    }

    private void clearAttributes(Group archetype, List<Group> otherGroups) {
        List<Attribute> archetypeAttributes = archetype.getEnumTypedefOrDimensionOrVariable().stream()
                .filter(element -> element instanceof Attribute).map(element -> (Attribute) element)
                .filter(attribute -> attribute.getValue() != null)
                .collect(toList());
        if (archetypeAttributes.isEmpty()) {
            return; // no attribute to clear
        }
        for (Group otherGroup : otherGroups) {
            Map<String, Attribute> otherAttributes = mapChildren(otherGroup.getEnumTypedefOrDimensionOrVariable(),
                    Attribute.class, attribute -> attribute.getName());
            for (Iterator<Attribute> iterator = archetypeAttributes.iterator(); iterator.hasNext();) {
                Attribute archetypeAttribute = iterator.next();
                Attribute otherAttribute = otherAttributes.get(archetypeAttribute.getName());
                if (!Objects.equals(archetypeAttribute.getValue(), otherAttribute.getValue())) {
                    iterator.remove(); // we no longer need to check this attribute
                    archetypeAttribute.setValue(null);
                }
            }
            if (archetypeAttributes.isEmpty()) {
                break; // no need to check other groups, no more attributes to clear
            }
        }
    }

    private void clearDimensions(Group archetype, List<Group> otherGroups) {
        // clear dimensions with different lengths
        // TODO Auto-generated method stub
        List<Dimension> archetypeDimensions = archetype.getEnumTypedefOrDimensionOrVariable().stream()
                .filter(element -> element instanceof Dimension).map(element -> (Dimension) element)
                .collect(toList());
        for (Group otherGroup : otherGroups) {
            Map<String, Dimension> otherDimensions = mapChildren(otherGroup.getEnumTypedefOrDimensionOrVariable(),
                    Dimension.class, dimension -> dimension.getName());
            for (Iterator<Dimension> iterator = archetypeDimensions.iterator(); iterator.hasNext();) {
                Dimension archetypeDimension = iterator.next();
                Dimension otherDimension = otherDimensions.get(archetypeDimension.getName());
                if (!Objects.equals(archetypeDimension.getLength(), otherDimension.getLength())) {
                    iterator.remove();
                    archetypeDimension.setLength(null);
                }
            }
            if (archetypeDimensions.isEmpty()) {
                break;
            }
        }
    }

    private void clearVariables(Group archetype, List<Group> otherGroups) {
        // for each variable:
        List<Variable> archetypeVariables = archetype.getEnumTypedefOrDimensionOrVariable().stream()
                .filter(element -> element instanceof Variable).map(element -> (Variable) element)
                .collect(toList());
        Map<String, List<Variable>> otherVariablesMap = new HashMap<>();
        for (Group otherGroup : otherGroups) {
            Map<String, Variable> otherVariables = mapChildren(otherGroup.getEnumTypedefOrDimensionOrVariable(),
                    Variable.class, variable -> variable.getName());
            for (Entry<String,Variable> variableEntry : otherVariables.entrySet()) {
                otherVariablesMap.computeIfAbsent(variableEntry.getKey(), key -> new ArrayList<Variable>())
                        .add(variableEntry.getValue());
            }
        }
        for (Variable archetypeVariable : archetypeVariables) {
            List<Variable> otherVariables = otherVariablesMap.get(archetypeVariable.getName());
            clearAttributes(archetypeVariable, otherVariables);
        }
    }

    private void clearAttributes(Variable archetype, List<Variable> otherVariables) {
        for (Attribute attribute : archetype.getAttribute()) {
            if (attribute.getValue() != null) {
                for (Variable otherVariable : otherVariables) {
                    Attribute matchingAttribute = otherVariable.getAttribute().stream()
                            .filter(otherAttribute -> otherAttribute.getName().equals(attribute.getName()))
                            .findAny().get();
                    if (!attribute.getValue().equals(matchingAttribute.getValue())) {
                        attribute.setValue(null);
                        LOGGER.debug("variable attribute cleared: {}:{}", archetype.getName(), attribute.getName());
                        break;
                    }
                }
            }
        }
    }

}
