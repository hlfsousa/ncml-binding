package io.github.hlfsousa.ncml.io;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLDimension;
import io.github.hlfsousa.ncml.annotation.CDLDimensions;
import io.github.hlfsousa.ncml.annotation.CDLGroup;
import io.github.hlfsousa.ncml.annotation.CDLRoot;

@CDLRoot
@CDLDimensions(@CDLDimension(name = "dim1"))
public interface TestFile {

    @CDLAttribute(name = "description")
    String getDescription();

    @CDLGroup(name = "child_group")
    ChildGroup getChildGroup();

}