package io.github.hlfsousa.ncml.io.arrayattribute;

import java.util.Optional;

import io.github.hlfsousa.ncml.io.wrapper.NetcdfWrapper;
import ucar.nc2.Group;

public class ArrayAttributeTestSubjectWrapper extends NetcdfWrapper implements ArrayAttributeTestSubject {


    public ArrayAttributeTestSubjectWrapper(Group group) {
        super(group);
    }

    @Override
    public int[] getArrayAttribute() {
        return Optional.ofNullable(group.findAttribute("array_attribute"))
                .map(arrayAttribute -> (int[])arrayAttribute.getValues().copyTo1DJavaArray())
                .orElse(null);
    }

    @Override
    public void setArrayAttribute(int[] arrayAttribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStrAttribute() {
        return Optional.ofNullable(group.findAttribute("str_attribute"))
                .map(strAttribute -> strAttribute.getStringValue())
                .orElse(null);
    }

    @Override
    public void setStrAttribute(String strAttribute) {
        throw new UnsupportedOperationException();
    }

    // additionalMethods >>
    // << additionalMethods

}
