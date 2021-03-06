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
import io.github.hlfsousa.ncml.io.ConvertUtils;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Index;

public class ConvertUtilsTest {

    private static interface TestNetcdf {

        @CDLVariable(dataType = "long", unsigned = false)
        Long getScalarLong();
        
        @CDLVariable(dataType = "int", unsigned = true)
        Long getUnsignedInt();
        
        @CDLVariable(dataType = "string")
        String getScalarString();

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
    public void testScalarString() throws Exception {
        String value = "testScalarString";
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getScalarString").getAnnotation(CDLVariable.class);

        Array scalarArray = convertUtils.toArray(value, variableDecl);
        assertThat(scalarArray.getObject(Index.scalarIndexImmutable), is(value));

        String revertedValue = convertUtils.toJavaObject(scalarArray, String.class);
        assertThat(revertedValue, is(value));
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
        CDLVariable variableDecl = TestNetcdf.class.getMethod("getScalarString").getAnnotation(CDLVariable.class);

        Array ncArray = convertUtils.toArray(javaArray, variableDecl);
        assertThat(ncArray.getDataType(), is(DataType.STRING));
        assertThat(ncArray.getShape(), is(new int[] { javaArray.length }));

        String[] revertedValue = convertUtils.toJavaObject(ncArray, String[].class);
        assertThat(revertedValue, is(javaArray));

    }

}
