package hsousa.ncml.io;

import hsousa.ncml.annotation.CDLAttribute;
import hsousa.ncml.annotation.CDLDimension;
import hsousa.ncml.annotation.CDLDimensions;
import hsousa.ncml.annotation.CDLGroup;
import hsousa.ncml.annotation.CDLRoot;

@CDLRoot
@CDLDimensions(@CDLDimension(name = "dim1"))
public interface TestFile {

    @CDLAttribute(name = "description")
    public String getDescription();

    @CDLGroup(name = "child_group")
    public ChildGroup getChildGroup();

}