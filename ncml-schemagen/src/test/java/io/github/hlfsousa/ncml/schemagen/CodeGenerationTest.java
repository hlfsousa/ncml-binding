package io.github.hlfsousa.ncml.schemagen;

/*-
 * #%L
 * ncml-schemagen
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
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Properties;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLDimension;
import io.github.hlfsousa.ncml.annotation.CDLDimensions;
import io.github.hlfsousa.ncml.annotation.CDLGroup;
import io.github.hlfsousa.ncml.annotation.CDLRoot;
import io.github.hlfsousa.ncml.annotation.CDLVariable;

/**
 * Generates code from a schema and verifies its correctness.
 *
 * @author Henrique Sousa
 *
 */
@TestMethodOrder(OrderAnnotation.class)
public class CodeGenerationTest extends AbstractCodeGenerationTest {

    protected final File sourcesDir = new File("target/test-gen-src");
    protected final File classesDir = new File("target/test-gen-classes");

    @Test
    @Order(1)
    public void testGenerationRuns() throws Exception {
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        Files.walkFileTree(classesDir.toPath(), DELETE_ALL);
        sourcesDir.mkdirs();

        final String rootPackage = "io.github.hlfsousa.ncml.schemagen.salinity.v2";
        final String rootGroupName = "SeaSurfaceSalinity";
        
        Properties properties = new Properties();
        properties.setProperty(NCMLCodeGenerator.CFG_PROPERTIES_LOCATION, "target/ncml-binding.properties");
        generateCode(sourcesDir, rootPackage, rootGroupName, "/sea_surface_salinity_v2.xml", properties);

        assertThat("Code was not generated", new File(sourcesDir,
                rootPackage.replace('.', File.separatorChar) + File.separator + rootGroupName + ".java").isFile(),
                is(true));
        
        File propertiesFile = new File(properties.getProperty(NCMLCodeGenerator.CFG_PROPERTIES_LOCATION));
        assertThat("Runtime properties file was not generated", propertiesFile.isFile(), is(true));
        Properties runtimeProperties = new Properties();
        try (FileReader propertiesReader = new FileReader(propertiesFile)) {
            runtimeProperties.load(propertiesReader);
        }
        assertThat(runtimeProperties.getProperty("metadata/authors"), is("authors"));
        assertThat(runtimeProperties.getProperty("metadata"), is("metadata"));
        assertThat(runtimeProperties.getProperty("Latitude"), is("Latitude"));
    }

    @Test
    @Order(2)
    public void testGeneratedCodeCompiles() {
        classesDir.mkdirs();
        int compilerResultCode = compileCode(sourcesDir, classesDir);
        assertThat(compilerResultCode, is(0));
    }

    @Test
    @Order(3)
    public void testRootGroup() throws Exception {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { classesDir.toURI().toURL() })) {
            Class<?> seaSurfaceSalinity = classLoader
                    .loadClass("io.github.hlfsousa.ncml.schemagen.salinity.v2.SeaSurfaceSalinity");
            assertThat(seaSurfaceSalinity.isAnnotationPresent(CDLRoot.class), is(true));
            { // groups
                Method metadata = seaSurfaceSalinity.getMethod("getMetadata");
                metadata.setAccessible(true);
                CDLGroup groupAnnotation = metadata.getAnnotation(CDLGroup.class);
                assertThat(groupAnnotation, is(notNullValue()));
                assertThat(groupAnnotation.name(), is("metadata"));
                assertThat(metadata.getReturnType().getSimpleName(), is("Metadata"));
            }
            { // dimensions
                CDLDimensions dimensions = seaSurfaceSalinity.getAnnotation(CDLDimensions.class);
                assertThat(dimensions, is(notNullValue()));
                CDLDimension latitude = dimensions.value()[0];
                assertThat(latitude.name(), is("Latitude"));
                assertThat(latitude.length(), is(180));
                CDLDimension longitude = dimensions.value()[1];
                assertThat(longitude.name(), is("Longitude"));
                assertThat(longitude.length(), is(360));
                assertThat(longitude.variableLength(), is(false));
                assertThat(longitude.unlimited(), is(false));
            }
            { // variables
                Method latitude = seaSurfaceSalinity.getMethod("getLatitude");
                assertThat(latitude.getReturnType(), is(classLoader.loadClass(
                        seaSurfaceSalinity.getName() + "$LatitudeVariable")));
                assertThat((Class<?>) ((ParameterizedType) latitude.getGenericReturnType()).getActualTypeArguments()[0],
                        is(float[].class));
                CDLVariable varAnnotation = latitude.getAnnotation(CDLVariable.class);
                assertThat(varAnnotation, is(notNullValue()));
                assertThat(varAnnotation.name(), is("Latitude"));
                assertThat(varAnnotation.dataType(), is("float"));
                assertThat(varAnnotation.shape(), is(arrayWithSize(1)));
                assertThat(varAnnotation.shape(), is(arrayContaining("Latitude")));
            }
        }
    }

    @Test
    @Order(4)
    public void testMetadata() throws Exception {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { classesDir.toURI().toURL() })) {
            Class<?> metadata = classLoader
                    .loadClass("io.github.hlfsousa.ncml.schemagen.salinity.v2.Metadata");
            assertThat(metadata.isAnnotationPresent(CDLRoot.class), is(false));

            { // dimensions
                CDLDimensions dimensions = metadata.getAnnotation(CDLDimensions.class);
                assertThat(dimensions, is(notNullValue()));
                assertThat(dimensions.value().length, is(1));
                CDLDimension revisionCount = dimensions.value()[0];
                assertThat(revisionCount.name(), is("revision_count"));
                assertThat(revisionCount.length(), is(0));
                assertThat(revisionCount.unlimited(), is(true));
            }
            { // attributes
                Method authors = metadata.getMethod("getAuthors");
                assertThat(authors.getReturnType(), is(String.class));
                CDLAttribute attAnnotation = authors.getAnnotation(CDLAttribute.class);
                assertThat(attAnnotation.name(), is("authors"));
                assertThat(attAnnotation.dataType(), is("string"));
                
                Method version = metadata.getMethod("getVersion");
                assertThat(version.getReturnType(), is(Integer.class));
            }
            { // variables
                Class<?> revisionDateType = classLoader
                        .loadClass("io.github.hlfsousa.ncml.schemagen.salinity.v2.Metadata$RevisionDateVariable");
                Method revisionDate = metadata.getMethod("getRevisionDate");
                assertThat(revisionDate.getReturnType(), is(revisionDateType));
                assertThat(((ParameterizedType) revisionDate.getGenericReturnType()).getActualTypeArguments()[0],
                        is(long[].class));
                CDLVariable varAnnotation = revisionDate.getAnnotation(CDLVariable.class);
                assertThat(varAnnotation, is(notNullValue()));
                assertThat(varAnnotation.name(), is("revision_date"));
                assertThat(varAnnotation.dataType(), is("long"));
                assertThat(varAnnotation.shape(), is(arrayWithSize(1)));
                assertThat(varAnnotation.shape(), is(arrayContaining("revision_count")));
            }

        }
    }

}
