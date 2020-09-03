package hsousa.ncml.io;

import hsousa.ncml.annotation.CDLVariable;
import ucar.ma2.Array;

public interface Converter<T> {

    Array toArray(T value, CDLVariable variableDecl);
    
    T toJavaObject(Array array, Class<? extends T> toType);

    boolean isApplicable(T value);
    
    boolean isApplicable(Array array);
    
}
