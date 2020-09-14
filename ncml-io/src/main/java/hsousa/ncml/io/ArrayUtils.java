package hsousa.ncml.io;

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

}
