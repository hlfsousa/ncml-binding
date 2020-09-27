package io.github.hlfsousa.ncml.io.arrayattribute;

// imports >>
// DEFAULT IMPORTS
import java.util.*;
import ucar.nc2.Group;
import ucar.nc2.Variable;
import ucar.nc2.Dimension;
// << imports

public class ArrayAttributeTestSubjectVO implements ArrayAttributeTestSubject {
    // additionalFields >>
    // << additionalFields
    private int[] arrayAttribute;

    @Override
    public int[] getArrayAttribute() {
        return arrayAttribute;
    }

    @Override
    public void setArrayAttribute(int[] arrayAttribute) {
        this.arrayAttribute = arrayAttribute;
    }

    private String strAttribute;

    @Override
    public String getStrAttribute() {
        return strAttribute;
    }

    @Override
    public void setStrAttribute(String strAttribute) {
        this.strAttribute = strAttribute;
    }

    // additionalMethods >>
    // << additionalMethods

}
