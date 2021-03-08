package io.github.hlfsousa.ncml.io;

/*-
 * #%L
 * ncml-io
 * %%
 * Copyright (C) 2020 - 2021 Henrique L. F. de Sousa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ArrayUtils {

    public static boolean isEmpty(Object array) {
        if (array == null) {
            return true;
        }
        if (!array.getClass().isArray()) {
            return false;
        }
        int[] shape = shapeOf(array);
        for (int length : shape) {
            if (length == 0) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> getComponentType(Class<?> type) {
        while (type.isArray()) {
            type = type.getComponentType();
        }
        return type;
    }
    
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

    public static void setElement(Object array, int[] address, Object value) {
        Object element = array;
        for (int i = 0; i < address.length - 1; i++) {
            element = Array.get(element, address[i]);
        }
        Array.set(element, address[address.length - 1], value);
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

    /**
     * Assume array of three named dimensions A, B, C; this method takes reordered dimensions C, A, B (in any order) and
     * creates the corresponding dimension mapping ([A, B, C] to [C, A, B] yields [2, 0, 1]). This can be used to
     * transpose a array from {@code shape} to {@code desiredShape}.
     *
     * @param shape        named dimensions of a array
     * @param desiredShape reordered dimensions
     * @return reordered indexes for transposition
     */
    public static int[] createTransposition(String[] shape, String[] desiredShape) {
        assert shape.length == desiredShape.length;
        assert Arrays.asList(shape).containsAll(Arrays.asList(desiredShape)) : "Shapes don't match: "
                + Arrays.toString(shape) + " / " + Arrays.toString(desiredShape);
        Map<String, Integer> shapeMap = new HashMap<>();
        for (int i = 0; i < shape.length; i++) {
            Integer oldValue = shapeMap.put(shape[i], i);
            assert oldValue == null;
        }
        int[] result = new int[shape.length];
        for (int i = 0; i < shape.length; i++) {
            result[i] = shapeMap.get(desiredShape[i]);
        }
        return result;
    }

    public static boolean increment(int[] address, int[] shape) {
        int carryOver = shape.length - 1;
        do {
            address[carryOver]++;
            if (address[carryOver] == shape[carryOver]) {
                address[carryOver] = 0;
            } else {
                return true;
            }
        } while (carryOver-- > 0);
        return false;
    }

    public static Object createArray(int[] shape, Class<?> type) {
        LinkedList<Class<?>> componentTypes = new LinkedList<>();
        componentTypes.addFirst(type);
        for (int i = 0; i < shape.length - 1; i++) {
            componentTypes.addFirst(Array.newInstance(componentTypes.getFirst(), 0).getClass());
        }

        int dimension = 0;
        boolean empty = Arrays.stream(shape).filter(dim -> dim == 0).findAny().isPresent();
        Object array = Array.newInstance(componentTypes.removeFirst(), shape[dimension++]);
        while (!componentTypes.isEmpty()) {
            int[] address = new int[shape.length - componentTypes.size()];
            int[] currentShape = Arrays.copyOf(shape, address.length);
            Class<?> componentType = componentTypes.removeFirst();
            if (!empty) {
                do {
                    setElement(array, address, Array.newInstance(componentType, shape[dimension]));
                } while (increment(address, currentShape));
            }
            dimension++;
        }

        return array;
    }
    
    public static <R> Collector<Object, List<Object>, R> collectToArray(Class<R> arrayType) {
        assert arrayType.isArray();
        return new Collector<Object, List<Object>, R>() {

            @Override
            public Supplier<List<Object>> supplier() {
                return () -> new ArrayList<>();
            }

            @Override
            public BiConsumer<List<Object>, Object> accumulator() {
                return (list, element) -> list.add(element);
            }

            @Override
            public BinaryOperator<List<Object>> combiner() {
                return (listA, listB) -> {
                    listA.addAll(listB);
                    return listA;
                };
            }

            @Override
            public Function<List<Object>, R> finisher() {
                return list -> {
                    @SuppressWarnings("unchecked")
                    R array = (R) Array.newInstance(arrayType.getComponentType(), list.size());
                    for (int i = 0; i < list.size(); i++) {
                        Array.set(array, i, list.get(i));
                    }
                    return array;
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.emptySet();
            }

        };
    }

}
