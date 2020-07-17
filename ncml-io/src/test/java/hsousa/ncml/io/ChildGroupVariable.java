package hsousa.ncml.io;

import hsousa.ncml.annotation.CDLAttribute;
import hsousa.ncml.declaration.Variable;
import ucar.ma2.Array;

public interface ChildGroupVariable extends Variable<Array> {

    @CDLAttribute(name = "long_name")
    public String getLongName();

}