package io.github.hlfsousa.ncml.io.converters;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.ArrayChar;
import ucar.ma2.DataType;
import ucar.ma2.Index;

public class ScalarCharConverter implements Converter<Character> {

    @Override
    public Array toArray(Character value, CDLVariable variableDecl) {
        ArrayChar array;
        if (variableDecl.shape().length == 0) {
            array = toScalarArray(value);
        } else {
            array = toArrayD1(value);
        }
        return array;
    }

    @Override
    public Array toArray(Character value, CDLAttribute attributeDecl) {
        return toScalarArray(value);
    }

    private ArrayChar toArrayD1(Character value) {
        ArrayChar array;
        array = new ArrayChar.D1(1);
        array.getIndexIterator().setCharNext(value);
        return array;
    }

    private ArrayChar toScalarArray(Character value) {
        ArrayChar array;
        array = new ArrayChar.D0();
        array.setChar(Index.scalarIndexImmutable, value);
        return array;
    }

    @Override
    public Character toJavaObject(Array array, Class<? extends Character> toType) {
        return array.getIndexIterator().getCharNext();
    }

    @Override
    public boolean isApplicable(Object value, CDLVariable variableDecl) {
        return value instanceof Character && DataType.getType(variableDecl.dataType()) == DataType.CHAR;
    }

    @Override
    public boolean isApplicable(Object value, CDLAttribute attributeDecl) {
        return value instanceof Character && DataType.getType(attributeDecl.dataType()) == DataType.CHAR;
    }

    @Override
    public boolean isApplicable(Array array, Class<?> toType) {
        return toType == char.class || toType == Character.class;
    }

}
