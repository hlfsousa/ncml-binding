package io.github.hlfsousa.ncml.io.converters;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.Converter;
import ucar.ma2.Array;
import ucar.ma2.DataType;

/**
 * Converts <strong>primitive</strong> arrays.
 */
public class ArrayNumberConverter implements Converter<Object> {

    @Override
    public Array toArray(Object value, CDLVariable variableDecl) {
        return Array.makeFromJavaArray(value, variableDecl.unsigned());
    }
    
    @Override
    public Array toArray(Object value, CDLAttribute attributeDecl) {
        return Array.makeFromJavaArray(value, attributeDecl.unsigned());
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
        return componentType.isPrimitive();
    }

    @Override
    public boolean isApplicable(Array array) {
        return array.getRank() > 0 && array.getDataType().isNumeric() || array.getDataType() == DataType.CHAR;
    }

}
