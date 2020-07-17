package hsousa.netcdf.schemagen;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import hsousa.ncml.annotation.CDLAttribute;
import hsousa.ncml.annotation.CDLDimension;
import hsousa.ncml.annotation.CDLDimensions;
import hsousa.ncml.annotation.CDLGroup;
import hsousa.ncml.annotation.CDLRoot;
import hsousa.ncml.annotation.CDLVariable;
import ucar.ma2.Array;

/**
 * Generates code from a schema and verifies its correctness.
 *
 * @author Henrique Sousa
 *
 */
@TestMethodOrder(OrderAnnotation.class)
public class CodeGenerationTest {

    private static final FileVisitor<Path> DELETE_ALL = new FileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            return file.toFile().delete() ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.TERMINATE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return dir.toFile().delete() ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
        }
    };
    private final FileFilter sourcesFilter = pathname -> pathname.isFile() && pathname.getName().endsWith(".java");
    private final FileFilter dirFilter = pathname -> pathname.isDirectory();

    private final File sourcesDir = new File("target/test-gen-src");
    private final File classesDir = new File("target/test-gen-classes");

    @Test
    @Order(1)
    public void testGenerationRuns() throws Exception {
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        Files.walkFileTree(classesDir.toPath(), DELETE_ALL);
        sourcesDir.mkdirs();

        final String rootPackage = "hsousa.netcdf.schemagen.salinity.v2";
        final String rootGroupName = "SeaSurfaceSalinity";

        Properties properties = new Properties();
        NCMLCodeGenerator generator = new NCMLCodeGenerator(getClass().getResource("/sea_surface_salinity_v2.xml"),
                properties);
        generator.setModelPackage(rootPackage); // TODO required? move to constructor or snake config
        generator.setRootGroupName(rootGroupName); // TODO required? move to constructor or snake config
        generator.generateSources(sourcesDir);

        assertThat("Code was not generated", new File(sourcesDir,
                rootPackage.replace('.', File.separatorChar) + File.separator + rootGroupName + ".java").isFile(),
                is(true));
    }

    @Test
    @Order(2)
    public void testGeneratedCodeCompiles() {
        List<File> sources = new ArrayList<>();
        addSources(sourcesDir, sources);
        classesDir.mkdirs();
        assertThat("Nothing to compile", sources.isEmpty(), is(false));
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> arguments = new ArrayList<>();
        arguments.addAll(Arrays.asList("-cp", System.getProperty("java.class.path"),
                "-g", "-d", classesDir.getAbsolutePath()));

        arguments.addAll(sources.stream().map(file -> file.getPath()).collect(Collectors.toList()));

        assertThat(compiler.run(null, null, null, arguments.toArray(new String[0])), is(0));
    }

    @Test
    @Order(3)
    public void testRootGroup() throws Exception {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { classesDir.toURI().toURL() })) {
            Class<?> seaSurfaceSalinity = classLoader
                    .loadClass("hsousa.netcdf.schemagen.salinity.v2.SeaSurfaceSalinity");
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
                        is(Array.class));
                CDLVariable varAnnotation = latitude.getAnnotation(CDLVariable.class);
                assertThat(varAnnotation, is(notNullValue()));
                assertThat(varAnnotation.name(), is("Latitude"));
                assertThat(varAnnotation.type(), is(Float.class));
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
                    .loadClass("hsousa.netcdf.schemagen.salinity.v2.Metadata");
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
            }
            { // variables
                Method latitude = metadata.getMethod("getRevisionDate");
                // TBC assertThat(latitude.getType(), is(Array.class));
                CDLVariable varAnnotation = latitude.getAnnotation(CDLVariable.class);
                assertThat(varAnnotation, is(notNullValue()));
                assertThat(varAnnotation.name(), is("revision_date"));
                assertThat(varAnnotation.type(), is(Long.class));
                assertThat(varAnnotation.shape(), is(arrayWithSize(1)));
                assertThat(varAnnotation.shape(), is(arrayContaining("revision_count")));
            }

        }
    }

    private void addSources(File dir, List<File> result) {
        Arrays.asList(dir.listFiles(sourcesFilter)).forEach(file -> result.add(file));
        Arrays.asList(dir.listFiles(dirFilter)).forEach(subDir -> addSources(subDir, result));
    }

}
