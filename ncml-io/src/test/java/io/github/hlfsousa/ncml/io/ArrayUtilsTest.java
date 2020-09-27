package io.github.hlfsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.io.ArrayUtils;

public class ArrayUtilsTest {

    @Test
    public void testShapeOfD1() {
        int[] array1 = new int[100];
        assertThat(ArrayUtils.shapeOf(array1), is(new int[] { array1.length }));

        String[] array2 = {};
        assertThat(ArrayUtils.shapeOf(array2), is(new int[] { array2.length }));
    }
    
    @Test
    public void testShapeOfD2() {
        long[][] array1 = new long[10][100];
        assertThat(ArrayUtils.shapeOf(array1), is(new int[] { array1.length, array1[0].length }));
        
        String[][] array2 = new String[0][];
        assertThat(ArrayUtils.shapeOf(array2), is(new int[] { 0, 0 }));
    }
    
    @Test
    public void testShapeOfD3() {
        byte[][][] array1 = new byte[10][100][1000];
        assertThat(ArrayUtils.shapeOf(array1), is(new int[] { array1.length, array1[0].length, array1[0][0].length }));
        
        Object[][] array2 = new Object[10][0][];
        assertThat(ArrayUtils.shapeOf(array2), is(new int[] { array2.length, 0, 0 }));
    }
    
    @Test
    public void testGetElement() {
        String[][][] array = new String[10][10][10];
        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 10; b++) {
                for (int c = 0; c < 10; c++) {
                    array[a][b][c] = String.format("%s, %s, %s", a, b, c);
                }
            }
        }
        
        assertThat(ArrayUtils.getElement(array, new int[] { 1, 2, 3 }), is("1, 2, 3"));
        assertThat(ArrayUtils.getElement(array, new int[] { 9, 4, 0 }), is("9, 4, 0"));
    }
    
}
