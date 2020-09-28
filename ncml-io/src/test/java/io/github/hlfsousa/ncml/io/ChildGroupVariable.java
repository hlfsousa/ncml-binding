package io.github.hlfsousa.ncml.io;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.declaration.Variable;
import ucar.ma2.Array;

public interface ChildGroupVariable extends Variable<Array> {

    @CDLAttribute(name = "long_name")
    public String getLongName();

}