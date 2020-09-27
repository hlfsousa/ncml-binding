package io.github.hlfsousa.ncml.io.wrapper;

import io.github.hlfsousa.ncml.io.AttributeConventions;
import ucar.ma2.Array;
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

    public NetcdfWrapper(Group group) {
        this.group = group;
    }

    public Group unwrap() {
        return group;
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
