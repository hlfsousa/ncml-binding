package hsousa.ncml.io;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.ucar.unidata.netcdf.ncml.Netcdf;
import hsousa.ncml.io.arrayattribute.ArrayAttributeTestSubject;
import hsousa.ncml.io.arrayattribute.ArrayAttributeTestSubjectVO;
import hsousa.ncml.io.arrayattribute.ArrayAttributeTestSubjectWrapper;
import hsousa.ncml.io.read.NetcdfReader;
import hsousa.ncml.io.write.NetcdfWriter;
import hsousa.netcdf.schemagen.task.CodeGenerationTask;
import ucar.ma2.Array;
import ucar.ma2.ArrayString;
import ucar.ma2.IndexIterator;
import ucar.nc2.Attribute;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;

/**
 * Verifies <a href="https://github.com/hlfsousa/ncml-binding/issues/35">Issue #35</a>.
 */
public class ArrayAttributeTest {

    private static final String STRING_ATTRIBUTE_NAME = "str_attribute";
    private static final String[] ARRAY_STRING = { "foo", "bar" };

    private static final String INT_ATTRIBUTE_NAME = "array_attribute";
    private static final int[] ARRAY_INT = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    private static final File RES_DIR = new File("src/test/resources/array_attribute");
    private static final File NC_FILE = new File(RES_DIR, "ArrayAttributeTest.nc");

    /**
     * Manually create a file that contains an array attribute, then generates code for the other tests.
     */
    @Test
    @SuppressWarnings("deprecation")
    @Disabled("Enable manually when the model has to be re-generated, run this method only")
    public void createFile() throws Exception {
        RES_DIR.mkdirs();
        NetcdfFileWriter writer = NetcdfFileWriter.createNew(Version.netcdf4_classic, NC_FILE.getAbsolutePath());
        Group rootGroup = writer.addGroup(null, null);

        Attribute.Builder intAttributeBuilder = Attribute.builder(INT_ATTRIBUTE_NAME);
        intAttributeBuilder.setValues(Array.makeFromJavaArray(ARRAY_INT));
        writer.addGroupAttribute(rootGroup, intAttributeBuilder.build());

        // This is written as a single string value -- string array attributes are not supported
        Attribute.Builder stringAttributeBuilder = Attribute.builder(STRING_ATTRIBUTE_NAME);
        Array strNcArray = new ArrayString.D1(ARRAY_STRING.length);
        int idx = 0;
        for (IndexIterator it = strNcArray.getIndexIterator(); it.hasNext(); idx++) {
            it.setObjectNext(ARRAY_STRING[idx]);
        }
        stringAttributeBuilder.setValues(strNcArray);
        writer.addGroupAttribute(rootGroup, stringAttributeBuilder.build());

        writer.create();
        writer.getNetcdfFile().close();

        File logFile = new File("target/ArrayAttributeTest.err");

        File ncmlFile = new File(RES_DIR, "ArrayAttributeTest.xml");
        assertThat(new ProcessBuilder(Arrays.asList("ncdump", "-h", "-x", NC_FILE.getAbsolutePath()))
                .redirectOutput(Redirect.to(ncmlFile))
                .redirectError(Redirect.to(logFile))
                .start().waitFor(), is(0));

        CodeGenerationTask codeGeneration = new CodeGenerationTask();
        codeGeneration.setHeaderURL(ncmlFile.toURI().toURL());
        codeGeneration.setRootClassName("hsousa.ncml.io.arrayattribute.ArrayAttributeTestSubject");
        codeGeneration.setSourcesDir(new File("src/test/java"));
        codeGeneration.generateCode();
    }

    @Test
    public void writeAndReadFile() throws IOException {
        ArrayAttributeTestSubject testSubject = new ArrayAttributeTestSubjectVO();
        int[] intArray = new int[] { 1, 2, 3, 4, 5};
        testSubject.setArrayAttribute(intArray);
        
        NetcdfWriter writer = new NetcdfWriter();
        File dest = File.createTempFile("ArrayAttributeTest-readFile", ".nc");
        NetcdfFile netcdf = writer.write(testSubject, dest);
        ArrayAttributeTestSubjectWrapper wrapper = new ArrayAttributeTestSubjectWrapper(netcdf.getRootGroup());
        assertThat(wrapper.getArrayAttribute(), is(intArray));
        netcdf.close();
        
        NetcdfReader<ArrayAttributeTestSubject> reader = new NetcdfReader<>(ArrayAttributeTestSubject.class);
        ArrayAttributeTestSubject proxy = reader.read(dest, false);
        assertThat(proxy.getArrayAttribute(), is(intArray));
    }

}
