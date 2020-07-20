package hsousa.netcdf.schemagen;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class MappedPropertyTest extends AbstractCodeGenerationTest {

    protected final File sourcesDir = new File("target/test-gen-src");
    protected final File classesDir = new File("target/test-gen-classes");

    /**
     * This test verifies that groups that share definition are mapped to a single property of type map. Details in
     * issue #1 (https://github.com/hlfsousa/ncml-binding/issues/1).
     */
    @Test
    public void testMappedProperty() throws Exception {
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        Files.walkFileTree(classesDir.toPath(), DELETE_ALL);
        sourcesDir.mkdirs();
        classesDir.mkdirs();

        final String rootPackage = "hsousa.netcdf.schemagen.mappedprops";
        final String rootGroupName = "MappedProperties";

        Properties properties = new Properties();
        generateCode(sourcesDir, rootPackage, rootGroupName, "/mapped_properties.xml", properties);
        assertThat("Code was not generated", new File(sourcesDir,
                rootPackage.replace('.', File.separatorChar) + File.separator + rootGroupName + ".java").isFile(),
                is(true));
        int compilerResultCode = compileCode(sourcesDir, classesDir);
        assertThat(compilerResultCode, is(0));

        // code is generated, now we need to check the class
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { classesDir.toURI().toURL() })) {
            Class<?> mappedProperties = classLoader.loadClass(rootPackage + '.' + rootGroupName);
            Method getGroupMap = mappedProperties.getMethod("getGroupMap");
            assertThat(getGroupMap.getReturnType(), is(Map.class));
            Class<?> groupMap = classLoader.loadClass(rootPackage + '.' + "GroupMap");
            assertThat(((ParameterizedType)getGroupMap.getGenericReturnType()).getActualTypeArguments()[1], is(groupMap));
        }
    }

}
