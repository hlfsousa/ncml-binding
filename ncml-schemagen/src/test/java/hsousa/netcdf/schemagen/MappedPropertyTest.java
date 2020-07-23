package hsousa.netcdf.schemagen;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

@TestMethodOrder(OrderAnnotation.class)
public class MappedPropertyTest extends AbstractCodeGenerationTest {

    protected final File sourcesDir = new File("target/test-gen-src");
    protected final File classesDir = new File("target/test-gen-classes");
    protected final String rootPackage = "hsousa.netcdf.schemagen.mappedprops";
    protected final String rootGroupName = "MappedProperties";

    /**
     * This test verifies that groups that share definition are mapped to a single property of type map. Details in
     * issue #1 (https://github.com/hlfsousa/ncml-binding/issues/1).
     */
    @Test
    @Order(1)
    public void testMappedProperty() throws Exception {
        Files.walkFileTree(sourcesDir.toPath(), DELETE_ALL);
        Files.walkFileTree(classesDir.toPath(), DELETE_ALL);
        sourcesDir.mkdirs();
        classesDir.mkdirs();

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

    /**
     * This test verifies that variables that share definition are mapped to a single property of type map. Details in
     * issue #7 (https://github.com/hlfsousa/ncml-binding/issues/7).
     */
    @Test
    @Order(2)
    public void testMappedVariable() throws Exception {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { classesDir.toURI().toURL() })) {
            Class<?> mappedProperties = classLoader.loadClass(rootPackage + '.' + rootGroupName);
            Method getTemperatureMap = mappedProperties.getMethod("getTemperatureMap");
            assertThat(getTemperatureMap.getReturnType(), is(Map.class));
            Class<?> temperatureMap = classLoader.loadClass(rootPackage + '.' + rootGroupName + '$' + "TemperatureMapVariable");
            assertThat(((ParameterizedType) getTemperatureMap.getGenericReturnType()).getActualTypeArguments()[1]
                    .getTypeName(), matchesPattern("\\Q" + temperatureMap.getName() + "\\E(<.*>)?"));
        }
    }
    
}
