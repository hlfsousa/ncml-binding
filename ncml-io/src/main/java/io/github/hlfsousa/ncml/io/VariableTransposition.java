package io.github.hlfsousa.ncml.io;

import static io.github.hlfsousa.ncml.io.ArrayUtils.increment;
import static io.github.hlfsousa.ncml.io.ArrayUtils.setElement;
import static io.github.hlfsousa.ncml.io.ArrayUtils.shapeOf;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.hlfsousa.ncml.declaration.Variable;
import ucar.nc2.Dimension;

/**
 * Transposes a variable to a desired shape. This is especially useful in cases that require flexibility in the arriving
 * data: should the NetCDF file declare any given variable with dimensions in an uncertain order, transposition can make
 * the shape predictable. While this implementation provides transposition for Java arrays and a method that transposes
 * any value given callbacks (see {@link #transpose(String[], BiFunction, Function, BiFunction, TriConsumer)}), this can
 * be subclassed so that custom types of values can be transposed less verbosely. Any new shape must contain the same
 * dimension names as the original variable.
 * 
 * @author Henrique Sousa
 *
 * @param <T> The type of variable value to be transposed.
 */
public class VariableTransposition<T> {

    private final Variable<T> variable;

    /**
     * Prepares transposition for a given variable.
     * 
     * @param variable the variable to transpose.
     */
    public VariableTransposition(Variable<T> variable) {
        this.variable = variable;
    }

    /**
     * Transposes a Java array value and assigns it to a new variable along with reordered dimensions.
     * 
     * @param newVariable  the newly initialized variable to become the transposed representation
     * @param desiredShape the desired shape of the variable.
     * @return {@code newVariable}, with value and dimensions set (attributes must be copied somehow else if you so
     *         wish).
     */
    public Variable<T> transposeJavaArray(Variable<T> newVariable, String... desiredShape) {
        assert newVariable != variable : "do not transpose to the same variable";
        T newValue = transposeJavaArray(desiredShape);
        transposeDimensions(newVariable, desiredShape, newValue);
        return newVariable;
    }

    /**
     * Transposes the value of a variable, given it is a Java array.
     * 
     * @param desiredShape the new shape.
     * @return the transposed array value
     */
    public T transposeJavaArray(String... desiredShape) {
        Object array = variable.getValue();
        @SuppressWarnings("unchecked")
        BiFunction<Variable<T>, int[], T> arrayCreation = (var, shape) -> {
            // component type is mandated by shape
            Class<?> componentType = array.getClass();
            for (int i = 0; i < shape.length; i++) {
                assert componentType.isArray();
                componentType = componentType.getComponentType();
            }
            return (T) createArray(shape, componentType);
        };

        Function<T, int[]> getShape = value -> {
            int[] rawShape = shapeOf(value); // very small overhead if variable length dimension is present
            if (rawShape.length > desiredShape.length) { // variable length protection
                return Arrays.copyOf(rawShape, desiredShape.length);
            }
            return rawShape;
        };

        BiFunction<T, int[], Object> getElement = ArrayUtils::getElement;
        TriConsumer<T, int[], Object> setElement = ArrayUtils::setElement;
        return transpose(desiredShape, arrayCreation, getShape, getElement, setElement);
    }

    /**
     * Transposes a variable to a different shape. This method is specifically designed for variables that do not hold array values.
     * While this method is verbose when used with callbacks; to prevent this, extend this class and create an overload that does not
     * use callbacks for a specific variable type.
     * 
     * @param newVariable instance of the new variable where the transposed value will be held.
     * @param desiredShape the new shape.
     * @param arrayCreation    callback to create an array, given a new shape and the initial variable.
     * @param getVariableShape callback to retrieve the current shape of a variable (dimensions may be unlimited and
     *                         will be used only if this argument is null).
     * @param getElement       callback to retrieve an element from the value, given its address (index for each
     *                         dimension in the current shape).
     * @param setElement       callback to set an element to the variable value, given its address and the new value.
     * @return the transposed variable (same reference as {@code newVariable}.
     */
    public Variable<T> transpose(Variable<T> newVariable, String[] desiredShape, BiFunction<Variable<T>, int[], T> arrayCreation,
            Function<T, int[]> getVariableShape, BiFunction<T, int[], Object> getElement,
            TriConsumer<T, int[], Object> setElement) {
        assert newVariable != variable : "do not transpose to the same variable";
        T newValue = transpose(desiredShape, arrayCreation, getVariableShape, getElement, setElement);
        transposeDimensions(newVariable, desiredShape, newValue);
        return newVariable;
    }

    private void transposeDimensions(Variable<T> newVariable, String[] desiredShape, T newValue) {
        String[] variableDimNames = variable.getDimensions().stream()
                .map(dimension -> dimension.getShortName())
                .collect(Collectors.toList())
                .toArray(new String[0]);
        newVariable.setValue(newValue);
        int[] transposition = ArrayUtils.createTransposition(variableDimNames, desiredShape);
        Dimension[] newDimensions = new Dimension[desiredShape.length];
        for (int i = 0; i < transposition.length; i++) {
            newDimensions[i] = variable.getDimensions().get(transposition[i]);
        }
        newVariable.setDimensions(new ArrayList<>(Arrays.asList(newDimensions)));
    }

    /**
     * Transposes any given type of variable value.
     * 
     * @param desiredShape     the new shape.
     * @param arrayCreation    callback to create an array, given a new shape and the initial variable.
     * @param getVariableShape callback to retrieve the current shape of a variable (dimensions may be unlimited and
     *                         will be used only if this argument is null).
     * @param getElement       callback to retrieve an element from the value, given its address (index for each
     *                         dimension in the current shape).
     * @param setElement       callback to set an element to the variable value, given its address and the new value.
     * @return the transposed value
     */
    public T transpose(String[] desiredShape, BiFunction<Variable<T>, int[], T> arrayCreation,
            Function<T, int[]> getVariableShape, BiFunction<T, int[], Object> getElement,
            TriConsumer<T, int[], Object> setElement) {
        assert variable.getDimensions() != null : "Dimensions must be set to transpose";
        assert variable.getDimensions().size() == desiredShape.length : "Shape sizes must match to transpose";
        String[] variableDimNames = variable.getDimensions().stream()
                .map(dimension -> dimension.getShortName())
                .collect(Collectors.toList())
                .toArray(new String[0]);
        assert Arrays.asList(variableDimNames).containsAll(Arrays.asList(desiredShape)) : "Variable and desired shapes don't match";
        T variableValue = variable.getValue();
        if (Arrays.equals(desiredShape, variableDimNames)) {
            return variableValue;
        }

        int[] transposition = ArrayUtils.createTransposition(variableDimNames, desiredShape);
        // we can't always rely on the dimensions (unlimited or null at this point)
        if (getVariableShape == null) {
            getVariableShape = value -> variable.getDimensions().stream()
                .map(dimension -> dimension.getLength())
                .collect(ArrayUtils.collectToArray(int[].class));
        }
        int[] variableShape = getVariableShape.apply(variableValue);

        int[] newShape = getNewShape(variableShape, transposition);
        T result = arrayCreation.apply(variable, newShape);
        if (!Arrays.stream(newShape).filter(dim -> dim == 0).findAny().isPresent()) {
            int[] srcAddress = new int[variableShape.length];
            do {
                int[] dstAddress = new int[variableShape.length];
                for (int i = 0; i < variableShape.length; i++) {
                    dstAddress[i] = srcAddress[transposition[i]];
                }
                Object element = getElement.apply(variableValue, srcAddress);
                setElement.accept(result, dstAddress, element);
            } while (increment(srcAddress, variableShape));
        }
        return result;
    }

    private static int[] getNewShape(int[] sourceShape, int[] transposition) {
        assert sourceShape.length == transposition.length;
        int[] destShape = new int[sourceShape.length];
        for (int i = 0; i < sourceShape.length; i++) {
            destShape[i] = sourceShape[transposition[i]];
        }
        return destShape;
    }

    private static Object createArray(int[] shape, Class<?> type) {
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

}
