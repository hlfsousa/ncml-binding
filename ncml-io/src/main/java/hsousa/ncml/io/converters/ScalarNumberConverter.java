package hsousa.ncml.io.converters;

import hsousa.ncml.annotation.CDLVariable;
import hsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Index;

public class ScalarNumberConverter implements Converter<Number> {

    @Override
    public Array toArray(Number value, CDLVariable variableDecl) {
        DataType dataType = DataType.getType(value.getClass(), variableDecl.unsigned());
        Array scalarArray = Array.factory(dataType, new int[0]);
        scalarArray.setObject(Index.scalarIndexImmutable, value);
        return scalarArray;
    }

    @Override
    public Number toJavaObject(Array array, Class<? extends Number> toType) {
        Number value = (Number) array.getObject(Index.scalarIndexImmutable);
        /*
         * For now, we are assuming that value and toType are compatible. Value is necessarily not a primitive. toType
         * can be a primitive. What if the value is not compatible with toType? We will eventually get a
         * ClassCastException.
         */
        return value;
    }

    @Override
    public boolean isApplicable(Number value) {
        return true;
    }

    @Override
    public boolean isApplicable(Array array) {
        return array.getRank() == 0;
    }

}
