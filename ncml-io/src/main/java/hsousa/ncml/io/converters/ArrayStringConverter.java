package hsousa.ncml.io.converters;

import hsousa.ncml.annotation.CDLVariable;
import hsousa.ncml.io.ArrayUtils;
import hsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;

public class ArrayStringConverter implements Converter<Object> {

    @Override
    public Array toArray(Object value, CDLVariable variableDecl) {
        Array array = Array.factory(DataType.STRING, ArrayUtils.shapeOf(value));
        IndexIterator indexIterator = array.getIndexIterator();
        while (indexIterator.hasNext()) {
            indexIterator.next();
            indexIterator.setObjectCurrent(ArrayUtils.getElement(value, indexIterator.getCurrentCounter()));
        }
        return array;
    }

    @Override
    public Object toJavaObject(Array array, Class<? extends Object> toType) {
        return array.copyToNDJavaArray();
    }

    @Override
    public boolean isApplicable(Object value) {
        if (value instanceof Array) {
            return isApplicable((Array) value);
        }
        if (value == null || !value.getClass().isArray()) {
            return false;
        }
        Class<?> componentType = value.getClass();
        while (componentType.isArray()) {
            componentType = componentType.getComponentType();
        }
        return componentType == String.class;
    }

    @Override
    public boolean isApplicable(Array array) {
        return array.getRank() > 0 && array.getDataType() == DataType.STRING;
    }

}
