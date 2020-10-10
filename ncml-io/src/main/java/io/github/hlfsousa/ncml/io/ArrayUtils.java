package io.github.hlfsousa.ncml.io;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtils {

    public static int[] shapeOf(Object array) {
        assert array != null;
        int[] shape = new int[0];
        Object value = array;
        while (value.getClass().isArray()) {
            shape = addRank(shape, Array.getLength(value));
            if (shape[shape.length - 1] == 0) {
                // the length of all remaining ranks is 0
                Class<?> componentType = value.getClass().getComponentType();
                while (componentType.isArray()) {
                    shape = addRank(shape, 0);
                    componentType = componentType.getComponentType();
                }
                break;
            } else {
                value = Array.get(value, 0);
            }
        }
        return shape;
    }

    private static int[] addRank(int[] shape, int length) {
        int[] newShape = Arrays.copyOf(shape, shape.length + 1);
        newShape[shape.length] = length;
        return newShape;
    }

    public static Object getElement(Object array, int[] address) {
        Object result = array;
        for (int i = 0; i < address.length; i++) {
            result = Array.get(result, address[i]);
        }
        return result;
    }
    
    public static int getLength(Object array, int rank) {
        if (array.getClass().isArray()) {
            if (rank == 0) {
                return Array.getLength(array);
            } else {
                array = Array.get(array, 0);
                return getLength(array, rank - 1);
            }
        } else if (array instanceof ucar.ma2.Array) {
            return ((ucar.ma2.Array) array).getShape()[rank];
        } else {
            throw new IllegalArgumentException("array is neither ucar.ma2.Array nor Java array: " + array);
        }
    }

}
