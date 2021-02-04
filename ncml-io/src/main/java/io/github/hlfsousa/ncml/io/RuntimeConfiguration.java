package io.github.hlfsousa.ncml.io;

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
