package hsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
import hsousa.ncml.io.write.NetcdfWriter;
import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;

public class FileWritingTest extends IOTest {

    // Part 1: file creation
    // locate all files
    // generate and compile model
    // create and fill model (script function: createModel)
    // write file from scratch
    // close writer
    // re-open file and verify (script function: verifyCreatedFile)

    // Part 2: file editing
    // open file for editing (proxy)
    // edit properties (script function: editFile)
    // close file
    // re-open and verify (script function: verifyEditedFile)

    @ParameterizedTest
    @ValueSource(strings = { "ECMWF_ERA-40_subset" })
    public void testLifecycle(String referenceName) throws Exception {
        // locate all files
        URL schemaURL = getClass().getResource(String.format("/samples/%s.xml", referenceName));
        File sourcesDir = new File("target/gen-src-" + referenceName);
        sourcesDir.mkdirs();
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        String rootClassName = "hsousa.ncml.io.test.TestNetcdf" + ++testCounter;
        URL scriptURL = getClass().getResource(String.format("/samples/%s_write.js", referenceName));
        assertThat(schemaURL, is(notNullValue()));
        assertThat(scriptURL, is(notNullValue()));

        // generate and compile model
        generateModel(schemaURL, sourcesDir, rootClassName);

        // read script (expect declarations only)
        ScriptEngineManager scriptManager = new ScriptEngineManager();
        ScriptEngine javascriptEngine = scriptManager.getEngineByName("javascript");
        try (InputStreamReader common = new InputStreamReader(
                getClass().getResourceAsStream("/samples/commonFunctions.js"))) {
            javascriptEngine.eval(common);
        }
        try (InputStreamReader reader = new InputStreamReader(scriptURL.openStream())) {
            javascriptEngine.eval(reader);
        }

        // make sure all required functions are present
        assertThat(javascriptEngine.eval("createModel"), is(notNullValue()));
        assertThat(javascriptEngine.eval("verifyCreatedFile"), is(notNullValue()));
        assertThat(javascriptEngine.eval("editModel"), is(notNullValue()));
        assertThat(javascriptEngine.eval("verifyEditedFile"), is(notNullValue()));
        
        @SuppressWarnings("unchecked")
        final Class<? super Object> rootType = (Class<? super Object>) Class.forName(rootClassName);
        File fileFromScratch = new File(sourcesDir, "newContent.nc");
        NetcdfReader<?> reader = new NetcdfReader<>(rootType);
        Object modelObject = reader.create();
        javascriptEngine.put("model", modelObject);
        Object editedModel = javascriptEngine.eval("createModel(model)");
        System.out.println(editedModel);
        NetcdfWriter writer = new NetcdfWriter();
        NetcdfFile file = writer.write(editedModel, fileFromScratch);
        file.close();
        
        modelObject = reader.read(fileFromScratch, false); // TODO open for writing
        javascriptEngine.put("netcdf", modelObject);
        javascriptEngine.put("model", editedModel);
        javascriptEngine.eval("verifyCreatedFile(netcdf, model)");
        
        editedModel = javascriptEngine.eval("editModel(netcdf)");
        Object varWrapper = editedModel.getClass().getInterfaces()[0].getMethod("getLongitude").invoke(editedModel);
        Array longitude = (Array) varWrapper.getClass().getMethod("getValue").invoke(varWrapper);
        System.out.println("edited longitude: " + longitude);
        writer.write(editedModel, fileFromScratch).close();
        
        javascriptEngine.put("netcdf", reader.read(fileFromScratch, true));
        javascriptEngine.put("model", editedModel);
        javascriptEngine.eval("verifyEditedFile(netcdf, model)");
    }
    
}
