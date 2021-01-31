package io.github.hlfsousa.ncml.io.wrapper;

import java.util.Map;

import io.github.hlfsousa.ncml.io.AttributeConventions;
import io.github.hlfsousa.ncml.io.RuntimeConfiguration;
import ucar.ma2.Array;
import ucar.nc2.CDMNode;
import ucar.nc2.Group;
import ucar.nc2.Variable;

/**
 * Wraps a group, granting access to the variables and attributes it contains.
 * 
 * @author Henrique Sousa
 */
public abstract class NetcdfWrapper {

    protected final Group group;
    protected static AttributeConventions attributeConventions = new AttributeConventions();
    protected final RuntimeConfiguration runtimeConfiguration;

    public NetcdfWrapper(Group group, RuntimeConfiguration runtimeConfiguration) {
        this.group = group;
        this.runtimeConfiguration = runtimeConfiguration;
    }

    public Group unwrap() {
        return group;
    }
    
    public static String getRuntimeName(CDMNode node, String childName, Map<String, String> runtimeProperties) {
        if (runtimeProperties != null) {
            String fullName = node.getFullName();
            String key = fullName.isEmpty() ? childName : (fullName + '/' + childName);
            if (runtimeProperties.containsKey(key)) {
                return runtimeProperties.get(key);
            }
        }
        return childName;
    }

    protected Array getNumericArray(Variable variable) {
        return attributeConventions.readNumericArray(variable);
    }

}
