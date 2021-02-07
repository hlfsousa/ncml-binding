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
    protected final String rootPackage = "io.github.hlfsousa.ncml.schemagen.mappedprops";
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
