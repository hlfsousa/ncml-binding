package io.github.hlfsousa.ncml.schemagen;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Properties;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLDimension;
import io.github.hlfsousa.ncml.annotation.CDLDimensions;
import io.github.hlfsousa.ncml.annotation.CDLGroup;
import io.github.hlfsousa.ncml.annotation.CDLRoot;
import io.github.hlfsousa.ncml.annotation.CDLVariable;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
        generateCode(sourcesDir, rootPackage, rootGroupName, "/sea_surface_salinity_v2.xml", properties);

        assertThat("Code was not generated", new File(sourcesDir,
                rootPackage.replace('.', File.separatorChar) + File.separator + rootGroupName + ".java").isFile(),
                is(true));
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
                assertThat(varAnnotation.type(), is(float.class));
                assertThat(varAnnotation.shape(), is(arrayWithSize(1)));
                assertThat(varAnnotation.shape(), is(arrayContaining("Latitude")));
            }
        }
    }

    @Test
    @Order(3)
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
                assertThat(varAnnotation.type(), is(long.class));
                assertThat(varAnnotation.shape(), is(arrayWithSize(1)));
                assertThat(varAnnotation.shape(), is(arrayContaining("revision_count")));
            }

        }
    }

}
