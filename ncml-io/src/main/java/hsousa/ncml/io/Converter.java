package hsousa.ncml.io;

import hsousa.ncml.annotation.CDLAttribute;
import hsousa.ncml.annotation.CDLVariable;
import ucar.ma2.Array;

public interface Converter<T> {

    Array toArray(T value, CDLVariable variableDecl);

    Array toArray(T value, CDLAttribute attributeDecl);

    T toJavaObject(Array array, Class<? extends T> toType);

    boolean isApplicable(T value);

    boolean isApplicable(Array array);

}
