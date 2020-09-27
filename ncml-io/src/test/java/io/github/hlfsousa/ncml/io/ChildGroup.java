package io.github.hlfsousa.ncml.io;

import io.github.hlfsousa.ncml.annotation.CDLVariable;

public interface ChildGroup {

    @CDLVariable(name = "some_variable", shape = "dim1", type = double.class)
    public ChildGroupVariable getSomeVariable();

}