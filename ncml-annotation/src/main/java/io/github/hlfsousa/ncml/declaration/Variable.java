package io.github.hlfsousa.ncml.declaration;

import java.util.List;

import ucar.nc2.Dimension;

public interface Variable<T> {

    T getValue();

    void setValue(T value);
    
    List<Dimension> getDimensions();
    
    void setDimensions(List<Dimension> dimensions);

}
