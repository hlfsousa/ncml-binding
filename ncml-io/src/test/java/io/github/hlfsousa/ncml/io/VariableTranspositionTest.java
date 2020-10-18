package io.github.hlfsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.declaration.Variable;
import ucar.nc2.Dimension;

public class VariableTranspositionTest {

    private static final Random RNG = new Random();
    
    private <T> T randomizeInt(T array) {
        int[] shape = ArrayUtils.shapeOf(array);
        int[] address = new int[shape.length];
        do {
            ArrayUtils.setElement(array, address, RNG.nextInt());
        } while (ArrayUtils.increment(address, shape));
        return array;
    }
    
    @Test
    public void testCustomVariableArray2D() {
        Variable<NamedArray<int[][]>> testVariable = new ArrayVariable<>();

        testVariable.setDimensions(new ArrayList<>());
        
        String[] dimensions = { "x", "y" };
        int[] length = { 50, 100 };
        for (int i = 0; i < dimensions.length; i++) {
            testVariable.getDimensions().add(new Dimension(dimensions[i], length[i]));
        }
        testVariable.setValue(new NamedArray<>(new int[length[0]][length[1]], dimensions));
        
        String[] transposedDimensions = { "y", "x" };
        VariableTransposition<NamedArray<int[][]>> transposition = new VariableTransposition<>(testVariable);
        Variable<NamedArray<int[][]>> transposed = transposition.transpose(new ArrayVariable<>(), transposedDimensions, 
                (var, shape) -> new NamedArray<int[][]>((int[][])ArrayUtils.createArray(shape, int.class)),
                variable -> ArrayUtils.shapeOf(variable.getArray()),
                (value, address) -> ArrayUtils.getElement(value.getArray(), address),
                (value, address, element) -> ArrayUtils.setElement(value.getArray(), address, element));
                
        assertThat(transposed.getValue().getArray().length, is(length[1]));
        assertThat(transposed.getValue().getArray()[0].length, is(length[0]));

        int sampleCount = 50;
        for (int i = 0; i < sampleCount; i++) {
            int[] originalAddress = { RNG.nextInt(length[0]), RNG.nextInt(length[1]) };
            assertThat(
                    transposed.getValue().getArray()[originalAddress[1]][originalAddress[0]],
                    is(testVariable.getValue().getArray()[originalAddress[0]][originalAddress[1]]));
        }
    }
    
    @Test
    public void testVariableArray2D() {
        Variable<int[][]> testVariable = new ArrayVariable<>();

        testVariable.setDimensions(new ArrayList<>());
        
        String[] dimensions = { "x", "y" };
        int[] length = { 50, 100 };
        for (int i = 0; i < dimensions.length; i++) {
            testVariable.getDimensions().add(new Dimension(dimensions[i], length[i]));
        }
        testVariable.setValue(randomizeInt(new int[length[0]][length[1]]));
        
        VariableTransposition<int[][]> transposition = new VariableTransposition<>(testVariable);
        Variable<int[][]> transposed = transposition.transposeJavaArray(new ArrayVariable<>(), "y", "x");
        assertThat(transposed.getValue().length, is(length[1]));
        assertThat(transposed.getValue()[0].length, is(length[0]));
        
        assertThat(transposed.getDimensions().size(), is(testVariable.getDimensions().size()));
        assertThat(transposed.getDimensions().get(0), is(testVariable.getDimensions().get(1)));
        assertThat(transposed.getDimensions().get(1), is(testVariable.getDimensions().get(0)));

        int sampleCount = 50;
        for (int i = 0; i < sampleCount; i++) {
            int[] originalAddress = { RNG.nextInt(length[0]), RNG.nextInt(length[1]) };
            assertThat(
                    transposed.getValue()[originalAddress[1]][originalAddress[0]],
                    is(testVariable.getValue()[originalAddress[0]][originalAddress[1]]));
        }

    }
    
    @Test
    public void testJavaArray2D() {
        Variable<int[][]> testVariable = new ArrayVariable<>();

        testVariable.setDimensions(new ArrayList<>());
        
        String[] dimensions = { "x", "y" };
        int[] length = { 50, 100 };
        for (int i = 0; i < dimensions.length; i++) {
            testVariable.getDimensions().add(new Dimension(dimensions[i], length[i]));
        }
        testVariable.setValue(randomizeInt(new int[length[0]][length[1]]));
        
        VariableTransposition<int[][]> transposition = new VariableTransposition<>(testVariable);
        int[][] transposed = transposition.transposeJavaArray("y", "x");
        assertThat(transposed.length, is(length[1]));
        assertThat(transposed[0].length, is(length[0]));

        int sampleCount = 50;
        for (int i = 0; i < sampleCount; i++) {
            int[] originalAddress = { RNG.nextInt(length[0]), RNG.nextInt(length[1]) };
            assertThat(
                    transposed[originalAddress[1]][originalAddress[0]],
                    is(testVariable.getValue()[originalAddress[0]][originalAddress[1]]));
        }

    }
    
    @Test
    public void testJavaArray3D() {
        Variable<int[][][]> testVariable = new ArrayVariable<>();

        testVariable.setDimensions(new ArrayList<>());
        
        String[] dimensions = { "x", "y", "z" };
        int[] length = { 50, 100, 200 };
        for (int i = 0; i < dimensions.length; i++) {
            testVariable.getDimensions().add(new Dimension(dimensions[i], length[i]));
        }
        testVariable.setValue(randomizeInt(new int[length[0]][length[1]][length[2]]));
        
        VariableTransposition<int[][][]> transposition = new VariableTransposition<>(testVariable);
        int[][][] transposed = transposition.transposeJavaArray("y", "z", "x");
        assertThat(transposed.length, is(length[1]));
        assertThat(transposed[0].length, is(length[2]));
        assertThat(transposed[0][0].length, is(length[0]));

        int sampleCount = 50;
        for (int i = 0; i < sampleCount; i++) {
            int[] originalAddress = { RNG.nextInt(length[0]), RNG.nextInt(length[1]), RNG.nextInt(length[2]) };
            assertThat(
                    transposed[originalAddress[1]][originalAddress[2]][originalAddress[0]],
                    is(testVariable.getValue()[originalAddress[0]][originalAddress[1]][originalAddress[2]]));
        }

    }
    
    @Test
    public void testCustomValue2D() {
        Variable<NamedArray<int[][]>> testVariable = new ArrayVariable<>();

        testVariable.setDimensions(new ArrayList<>());
        
        String[] dimensions = { "x", "y" };
        int[] length = { 50, 100 };
        for (int i = 0; i < dimensions.length; i++) {
            testVariable.getDimensions().add(new Dimension(dimensions[i], length[i]));
        }
        testVariable.setValue(new NamedArray<>(new int[length[0]][length[1]], dimensions));
        
        String[] transposedDimensions = { "y", "x" };
        VariableTransposition<NamedArray<int[][]>> transposition = new VariableTransposition<>(testVariable);
        NamedArray<int[][]> transposed = transposition.transpose(transposedDimensions, 
                (var, shape) -> new NamedArray<int[][]>((int[][])ArrayUtils.createArray(shape, int.class)),
                variable -> ArrayUtils.shapeOf(variable.getArray()),
                (value, address) -> ArrayUtils.getElement(value.getArray(), address),
                (value, address, element) -> ArrayUtils.setElement(value.getArray(), address, element));
                
        assertThat(transposed.getArray().length, is(length[1]));
        assertThat(transposed.getArray()[0].length, is(length[0]));

        int sampleCount = 50;
        for (int i = 0; i < sampleCount; i++) {
            int[] originalAddress = { RNG.nextInt(length[0]), RNG.nextInt(length[1]) };
            assertThat(
                    transposed.getArray()[originalAddress[1]][originalAddress[0]],
                    is(testVariable.getValue().getArray()[originalAddress[0]][originalAddress[1]]));
        }
    }
    
    private final class ArrayVariable<T> implements Variable<T> {

        private T value;
        private List<Dimension> dimensions;

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

    }

    /**
     * This is a custom type of object for testing. This would be something used in a custom template. Besides the
     * template, a new {@link Converter} would have to be registered ({@link ConvertUtils#register(Class, Converter)}). We
     * are only interested in transposition here.
     *
     * @param <T> type of array that this holds
     */
    private class NamedArray<T> {
        
        private T array;
        private List<String> dimensions;
        
        public NamedArray(T array, String... dimensions) {
            this.array = array;
            this.dimensions = Arrays.asList(dimensions);
        }
        
        public T getArray() {
            return array;
        }
        
        public void setArray(T array) {
            this.array = array;
        }
        
        public List<String> getDimensions() {
            return dimensions;
        }

        public void setDimensions(List<String> dimensions) {
            this.dimensions = dimensions;
        }
        
    }
    
}
