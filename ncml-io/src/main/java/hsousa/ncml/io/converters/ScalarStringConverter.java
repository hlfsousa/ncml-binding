package hsousa.ncml.io.converters;

import hsousa.ncml.annotation.CDLAttribute;
import hsousa.ncml.annotation.CDLVariable;
import hsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Index;

public class ScalarStringConverter implements Converter<String> {

    @Override
    public Array toArray(String value, CDLVariable variableDecl) {
        return toArray(value);
    }

    @Override
    public Array toArray(String value, CDLAttribute attributeDecl) {
        return toArray(value);
    }

    private Array toArray(String value) {
        DataType dataType = DataType.STRING;
        Array scalarArray = Array.factory(dataType, new int[0]);
        scalarArray.setObject(Index.scalarIndexImmutable, value);
        return scalarArray;
    }

    @Override
    public String toJavaObject(Array array, Class<? extends String> toType) {
        return (String) array.getObject(Index.scalarIndexImmutable);
    }

    @Override
    public boolean isApplicable(String value) {
        return true;
    }

    @Override
    public boolean isApplicable(Array array) {
        return array.getDataType() == DataType.STRING;
    }

}
