package hsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import hsousa.ncml.io.read.NetcdfReader;
import hsousa.ncml.io.write.NetcdfWriter;
import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;

public class FileWritingTest extends IOTest {

    private Throwable failure;
    
    @ParameterizedTest
    @ValueSource(strings = { /*"ECMWF_ERA-40_subset", */"mapped_properties" })
    public void testLifecycle(String referenceName) throws Throwable {
        // locate all files
        URL schemaURL = getClass().getResource(String.format("/samples/%s.xml", referenceName));
        File sourcesDir = new File("target/gen-src-" + referenceName);
        sourcesDir.mkdirs();
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        String rootClassName = "hsousa.ncml.io.test.TestNetcdf";
        URL scriptURL = getClass().getResource(String.format("/samples/%s_write.js", referenceName));
        assertThat(schemaURL, is(notNullValue()));
        assertThat(scriptURL, is(notNullValue()));

        // generate and compile model
        File classesDir = new File("target", referenceName + "-classes");
        Files.walkFileTree(classesDir.toPath(), DELETE_ALL);
        classesDir.mkdirs();
        generateModel(schemaURL, sourcesDir, classesDir, rootClassName);

        Thread execThread = new Thread(() -> {
            try {
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
                Class<? super Object> rootType = (Class<? super Object>) Thread.currentThread()
                        .getContextClassLoader().loadClass(rootClassName);
                File fileFromScratch = new File(sourcesDir, "newContent.nc");
                NetcdfReader<?> reader = new NetcdfReader<>(rootType);
                Object modelObject = reader.create();
                javascriptEngine.put("model", modelObject);
                Object editedModel = javascriptEngine.eval("createModel(model)");
                NetcdfWriter writer = new NetcdfWriter();
                NetcdfFile file = writer.write(editedModel, fileFromScratch);
                file.close();

                modelObject = reader.read(fileFromScratch, false);
                javascriptEngine.put("netcdf", modelObject);
                javascriptEngine.put("model", editedModel);
                javascriptEngine.eval("verifyCreatedFile(netcdf, model)");

                editedModel = javascriptEngine.eval("editModel(netcdf)");
                writer.write(editedModel, fileFromScratch).close();

                javascriptEngine.put("netcdf", reader.read(fileFromScratch, true));
                javascriptEngine.put("model", editedModel);
                javascriptEngine.eval("verifyEditedFile(netcdf, model)");
            } catch (Exception e) {
                e.printStackTrace();
                Assertions.fail(e);
            }
        });
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { classesDir.toURI().toURL() })) {
            execThread.setContextClassLoader(classLoader);
            execThread.setUncaughtExceptionHandler((thread, cause) -> this.failure = cause);
            execThread.start();
            execThread.join();
        }
        if (failure != null) {
            throw failure;
        }
    }
    
}
