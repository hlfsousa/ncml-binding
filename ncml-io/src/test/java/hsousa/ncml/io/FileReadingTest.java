package hsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import hsousa.ncml.io.read.NetcdfReader;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;

/**
 * This is an integration test, and it takes all steps in the process: generates the model from an NCML, compiles the
 * generated classes, reads a NetCDF file to the model, and checks that the model is correct. Model verification is
 * performed via Javascript, since we cannot compile anything that can be checked against the not-yet-generated model
 * classes.
 */
public class FileReadingTest extends IOTest {

    @ParameterizedTest
    @ValueSource(strings = { "ECMWF_ERA-40_subset", "tos_O1_2001-2002" })
    public void testWrapperReading(String referenceName) throws Exception {
        // locate all files
        URL schemaURL = getClass().getResource(String.format("/samples/%s.xml", referenceName));
        File sourcesDir = new File("target/gen-src-" + referenceName);
        sourcesDir.mkdirs();
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        String rootClassName = "hsousa.ncml.io.test.TestNetcdf" + ++testCounter;
        String wrapperClassName = "hsousa.ncml.io.test.TestNetcdf" + testCounter + "Wrapper";
        URL verificationScript = getClass().getResource(String.format("/samples/%s_read.js", referenceName));

        // locate netcdf
        URL ncURL = getClass().getResource(String.format("/samples/%s.nc", referenceName));
        File ncFile = new File(ncURL.toURI());

        // generate and compile model
        generateModel(schemaURL, sourcesDir, rootClassName);

        // read file
        final Class<?> rootType = Class.forName(rootClassName);
        NetcdfFile netcdf = NetcdfFiles.open(ncFile.getAbsolutePath());
        Object wrapper = Class.forName(wrapperClassName).getConstructor(Group.class).newInstance(netcdf.getRootGroup());
        assertThat(wrapper, isA(rootType));
        
        // execute verification script
        verifyResult(wrapper, verificationScript);
    }

    /**
     * This is the actual test. The test parameter is the name of a NCML definition ({@code name.xml}), a NetCDF file
     * that follows it ({@code name.nc}) and a script that verifies the validity of the model ({@code name.js}).
     *
     * @param referenceName base name. Each value ({@code @ValueSource} annotation) is a test
     */
    @ParameterizedTest
    @ValueSource(strings = { "ECMWF_ERA-40_subset", "tos_O1_2001-2002" })
    public void testLifecycle(String referenceName) throws Exception {
        // locate all files
        URL schemaURL = getClass().getResource(String.format("/samples/%s.xml", referenceName));
        File sourcesDir = new File("target/gen-src-" + referenceName);
        sourcesDir.mkdirs();
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        String rootClassName = "hsousa.ncml.io.test.TestNetcdf" + ++testCounter;
        URL verificationScript = getClass().getResource(String.format("/samples/%s_read.js", referenceName));
        assertThat(schemaURL, is(notNullValue()));
        assertThat(verificationScript, is(notNullValue()));

        // locate netcdf
        URL ncURL = getClass().getResource(String.format("/samples/%s.nc", referenceName));
        File ncFile = new File(ncURL.toURI());

        // generate and compile model
        generateModel(schemaURL, sourcesDir, rootClassName);

        // read file
        final Class<?> rootType = Class.forName(rootClassName);
        NetcdfReader<?> ncReader = new NetcdfReader<>(rootType);
        Object obj = ncReader.read(ncFile, true);
        assertThat(obj, isA(rootType));

        // execute verification script
        verifyResult(obj, verificationScript);
    }

    private void verifyResult(Object obj, URL verificationScript) throws Exception {
        ScriptEngineManager scriptManager = new ScriptEngineManager();
        ScriptEngine javascriptEngine = scriptManager.getEngineByName("javascript");
        try (InputStreamReader common = new InputStreamReader(
                getClass().getResourceAsStream("/samples/commonFunctions.js"))) {
            javascriptEngine.eval(common);
        }
        try (InputStreamReader reader = new InputStreamReader(verificationScript.openStream())) {
            javascriptEngine.put("netcdf", obj);
            javascriptEngine.eval(reader);
        }
    }

}
