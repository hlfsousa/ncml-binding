package io.github.hlfsousa.ncml.io.wrapper;

import java.util.Properties;

import io.github.hlfsousa.ncml.io.AttributeConventions;
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
    protected Properties runtimeProperties;

    public NetcdfWrapper(Group group) {
        this.group = group;
    }

    public Group unwrap() {
        return group;
    }
    
    public void setRuntimeProperties(Properties properties) {
        this.runtimeProperties = properties;
    }
    
    public static String getRuntimeName(CDMNode node, String childName, Properties runtimeProperties) {
        if (runtimeProperties != null) {
            String fullName = node.getFullName();
            String key = fullName.isEmpty() ? childName : (fullName.replace('/', '.') + '.' + childName);
            return runtimeProperties.getProperty(key, childName);
        }
        return childName;
    }

    protected Array getNumericArray(String shortName) {
        Variable variable = group.findVariable(shortName);
        if (variable == null) {
            return null;
        }
        return getNumericArray(variable);
    }
    
    protected Array getNumericArray(Variable variable) {
        return attributeConventions.readNumericArray(variable);
    }

}
