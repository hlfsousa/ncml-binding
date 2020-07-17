package hsousa.ncml.io;

import hsousa.ncml.annotation.CDLVariable;

public interface ChildGroup {

    @CDLVariable(name = "some_variable", shape = "dim1", type = double.class)
    public ChildGroupVariable getSomeVariable();

}