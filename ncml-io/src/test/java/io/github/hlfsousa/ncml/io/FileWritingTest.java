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
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.hlfsousa.ncml.io.read.NetcdfReader;
import io.github.hlfsousa.ncml.io.write.NetcdfWriter;
import io.github.hlfsousa.ncml.schemagen.NCMLCodeGenerator;
import ucar.nc2.NetcdfFile;

public class FileWritingTest extends IOTest {

    private Throwable failure;
    
    @ParameterizedTest
    @ValueSource(strings = { "ECMWF_ERA-40_subset", "mapped_properties", "custom_properties", "attribute_conventions",
            "mapped_variable_shape_bug" })
    public void testLifecycle(String referenceName) throws Throwable {
        // locate all files
        URL schemaURL = getClass().getResource(String.format("/samples/%s.xml", referenceName));
        File sourcesDir = new File("target/gen-src-" + referenceName);
        sourcesDir.mkdirs();
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        String rootClassName = "io.github.hlfsousa.ncml.io.test.TestNetcdf";
        URL scriptURL = getClass().getResource(String.format("/samples/%s_write.js", referenceName));
        assertThat(schemaURL, is(notNullValue()));
        assertThat(scriptURL, is(notNullValue()));
        URL runtimePropertiesURL = getClass().getResource(String.format("/samples/%s_runtime.properties", referenceName));
        RuntimeConfiguration runtimeConfiguration = loadRuntimeConfiguration(runtimePropertiesURL);

        // generate and compile model
        File classesDir = new File("target", referenceName + "-classes");
        Files.walkFileTree(classesDir.toPath(), DELETE_ALL);
        classesDir.mkdirs();
        
        Properties properties = new Properties();
        // initial configuration is ignored
        properties.setProperty(NCMLCodeGenerator.CFG_PROPERTIES_LOCATION,
                new File(classesDir, "runtime.properties").getPath());
        InputStream propertiesIn = getClass().getResourceAsStream(String.format("/samples/%s_config.xml", referenceName));
        if (propertiesIn != null) {
            properties.loadFromXML(propertiesIn);
            propertiesIn.close();
        }
        
        generateModel(schemaURL, sourcesDir, classesDir, rootClassName, properties);

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
                NetcdfReader<?> reader = new NetcdfReader<>(rootType, runtimeConfiguration);
                Object modelObject = reader.create();
                javascriptEngine.put("model", modelObject);
                Object editedModel = javascriptEngine.eval("createModel(model)");
                NetcdfWriter writer = new NetcdfWriter(runtimeConfiguration);
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

    private RuntimeConfiguration loadRuntimeConfiguration(URL runtimePropertiesURL) throws IOException {
        Properties runtimeProperties = null;
        if (runtimePropertiesURL != null) {
            runtimeProperties = new Properties();
            try (InputStream in = runtimePropertiesURL.openStream()) {
                runtimeProperties.load(in);
            }
        }
        if (runtimeProperties == null) {
            return new RuntimeConfiguration(Collections.emptyMap());
        }
        Map<String, String> asMap = new HashMap<>();
        for (Enumeration<?> propertyNames = runtimeProperties.propertyNames(); propertyNames.hasMoreElements(); ) {
            String propertyName = (String) propertyNames.nextElement();
            asMap.put(propertyName, runtimeProperties.getProperty(propertyName));
        }
        return new RuntimeConfiguration(asMap);
    }
    
}
