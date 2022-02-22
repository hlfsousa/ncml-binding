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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.annotation.CDLVariable;
import ucar.ma2.Array;
import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayInt;
import ucar.ma2.ArrayObject;
import ucar.ma2.DataType;
import ucar.ma2.Index;
import ucar.ma2.IndexIterator;

public class ConvertUtilsTest {

    private static interface TestNetcdf {

        @CDLVariable(dataType = "long", unsigned = false)
        Long getScalarLong();
        
        @CDLVariable(dataType = "int", unsigned = true)
        Long getUnsignedInt();
        
        @CDLVariable(dataType = "string")
        String getScalarString();
        
        @CDLVariable(dataType = "string", shape = { "n" })
        String getArrayString();
        
        @CDLVariable(dataType = "char", shape = { "x", "y" })
        char[][] getCharArray();
        
        @CDLVariable(dataType = "char")
        Character getScalarChar();

    }

    private ConvertUtils convertUtils = ConvertUtils.getInstance();

    @Test
    public void testScalarLong() throws Exception {
        Long value = 2l * Integer.MAX_VALUE;
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getScalarLong").getAnnotation(CDLVariable.class);
        
        Array scalarArray = convertUtils.toArray(value, variableDecl);
        assertThat(scalarArray.getObject(Index.scalarIndexImmutable), is(value));

        Long revertedValue = convertUtils.toJavaObject(scalarArray, Long.class);
        assertThat(revertedValue, is(value));
    }
    
    @Test
    public void testScalarChar() throws Exception {
        char javaValue = 'A';
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getScalarChar").getAnnotation(CDLVariable.class);
        
        Array ncArray = convertUtils.toArray(javaValue, variableDecl);
        assertThat(ncArray.getDataType(), is(DataType.CHAR));
        assertThat(ncArray.getShape(), is(new int[0]));
        
        Character revertedValue = convertUtils.toJavaObject(ncArray, Character.class);
        assertThat(revertedValue, is(javaValue));
        revertedValue = convertUtils.toJavaObject(ncArray, char.class);
        assertThat(revertedValue, is(javaValue));
    }
    
    @Test
    public void testCharArray() throws Exception {
        char[][] javaArray = { "abcdefghij".toCharArray(), "jihgfedcba".toCharArray() };
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getCharArray").getAnnotation(CDLVariable.class);

        Array ncArray = convertUtils.toArray(javaArray, variableDecl);
        assertThat(ncArray.getDataType(), is(DataType.CHAR));
        assertThat(ncArray.getShape(), is(new int[] { javaArray.length, javaArray[0].length }));

        char[][] revertedValue = convertUtils.toJavaObject(ncArray, char[][].class);
        assertThat(revertedValue, is(javaArray));
    }
    
    @Test
    public void testScalarStringFromChar() throws Exception {
        String value = "testScalarString";
        // value read from NetCDF, does not match declaration strictly
        Array scalarArray = ArrayChar.makeFromString(value, value.length());
        String revertedValue = convertUtils.toJavaObject(scalarArray, String.class);
        assertThat(revertedValue, is(value));
    }
    
    @Test
    public void testScalarString() throws Exception {
        String value = "testScalarString";
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getScalarString").getAnnotation(CDLVariable.class);

        Array scalarArray = convertUtils.toArray(value, variableDecl);
        assertThat(scalarArray.getObject(Index.scalarIndexImmutable), is(value));
        assertThat(scalarArray.getRank(), is(0));

        String revertedValue = convertUtils.toJavaObject(scalarArray, String.class);
        assertThat(revertedValue, is(value));

        Array readResult = new ArrayObject.D1(Object.class, 1);
        readResult.setObject(0, value);
        String lenientRevertedValue = convertUtils.toJavaObject(readResult, String.class);
        assertThat(lenientRevertedValue, is(value));
    }

    @Test
    public void testArrayLong() throws Exception {
        long value = 2l * Integer.MAX_VALUE;
        long[] javaArray = new long[100];
        Arrays.fill(javaArray, value);
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getScalarLong").getAnnotation(CDLVariable.class);

        Array ncArray = convertUtils.toArray(javaArray, variableDecl);
        assertThat(ncArray.getDataType(), is(DataType.LONG));
        assertThat(ncArray.getShape(), is(new int[] { javaArray.length }));

        long[] revertedValue = convertUtils.toJavaObject(ncArray, long[].class);
        assertThat(revertedValue, is(javaArray));

    }

    @Test
    public void testUnsignedArray() throws Exception {
        long value = 2l * Integer.MAX_VALUE;
        long[] javaArray = new long[100];
        Arrays.fill(javaArray, value);
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getUnsignedInt").getAnnotation(CDLVariable.class);

        Array ncArray = convertUtils.toArray(javaArray, variableDecl);
        assertThat(ncArray.getDataType(), is(DataType.INT));
        assertThat(ncArray.getShape(), is(new int[] { javaArray.length }));

        long[] revertedValue = convertUtils.toJavaObject(ncArray, long[].class);
        assertThat(revertedValue, is(javaArray));

    }

    @Test
    public void testUnsignedScalar() throws Exception {
        Long value = 2l * Integer.MAX_VALUE;
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getUnsignedInt").getAnnotation(CDLVariable.class);
        
        Array scalarArray = convertUtils.toArray(value, variableDecl);
        assertThat(scalarArray.getObject(Index.scalarIndexImmutable), is(value.intValue()));

        Long revertedValue = convertUtils.toJavaObject(scalarArray, Long.class);
        assertThat(revertedValue, is(value));
    }
    
    @Test
    public void testArrayString() throws Exception {
        String[] javaArray = new String[100];
        Arrays.fill(javaArray, "testArrayString");
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getArrayString").getAnnotation(CDLVariable.class);

        Array ncArray = convertUtils.toArray(javaArray, variableDecl);
        assertThat(ncArray.getDataType(), is(DataType.STRING));
        assertThat(ncArray.getShape(), is(new int[] { javaArray.length }));

        String[] revertedValue = convertUtils.toJavaObject(ncArray, String[].class);
        assertThat(revertedValue, is(javaArray));
        
        ncArray = new ArrayObject.D1(Object.class, 100);
        IndexIterator idx = ncArray.getIndexIterator();
        while (idx.hasNext()) {
            idx.setObjectNext("testArrayString");
        }
        revertedValue = convertUtils.toJavaObject(ncArray, String[].class);
        assertThat(revertedValue, is(javaArray));
    }
    
    @Test
    public void testScalarToArrayString() throws Exception {
        String scalarValue = "scalarValue";
        Array ncArray = new ArrayObject.D0(Object.class);
        ncArray.setObject(Index.scalarIndexImmutable, scalarValue);
        String[] revertedValue = convertUtils.toJavaObject(ncArray, String[].class);
        assertThat(revertedValue, is(new String[] { scalarValue }));
    }
    
    @Test
    public void testScalarToArrayInteger() throws Exception {
        int scalarValue = 42;
        Array ncArray = new ArrayInt.D0();
        ncArray.setObject(Index.scalarIndexImmutable, scalarValue);
        int[] revertedValue = convertUtils.toJavaObject(ncArray, int[].class);
        assertThat(revertedValue, is(new int[] { scalarValue }));
    }
    
    @Test
    public void testVLenLong() throws Exception {
        long[][] javaArray = new long[10][];
        for (int i = 0; i < javaArray.length; i++) {
            javaArray[i] = new long[1 + i];
            for (int j = 0; j < javaArray[i].length; j++) {
                javaArray[i][j] = j;
            }
        }
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getScalarLong").getAnnotation(CDLVariable.class);

        Array ncArray = convertUtils.toArray(javaArray, variableDecl);
        assertThat(ncArray.getDataType(), is(DataType.OBJECT));
        assertThat(ncArray.getShape(), is(new int[] { javaArray.length }));

        long[][] revertedValue = convertUtils.toJavaObject(ncArray, long[][].class);
        assertThat(revertedValue, is(javaArray));
    }

}
