package hsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hsousa.ncml.annotation.CDLVariable;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Index;

public class ConvertUtilsTest {

    private static interface TestNetcdf {

        @CDLVariable(type = Long.class, unsigned = false)
        Long getScalarLong();
        
        @CDLVariable(type = String.class)
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
