package io.github.hlfsousa.ncml.io.arrayattribute;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import io.github.hlfsousa.ncml.io.ConvertUtils;
import io.github.hlfsousa.ncml.io.wrapper.NetcdfWrapper;

import java.util.regex.Matcher;
import ucar.ma2.*;
import ucar.nc2.Dimension;
import ucar.nc2.Group;
import ucar.nc2.Variable;
// << imports

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
