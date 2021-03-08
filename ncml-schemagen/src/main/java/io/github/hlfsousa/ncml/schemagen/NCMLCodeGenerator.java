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

import static org.apache.velocity.runtime.RuntimeConstants.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucar.unidata.netcdf.ncml.Netcdf;

/**
 * Coordinates generation of code from an NCML XML header. This generator is created with a minimal set of templates,
 * and custom templates can be added for additional features (see {@link #setTemplates(Map)}). A single instance of the
 * generator can write to a number of output directories. Generating sources to different locations takes alternating
 * calls between {@link #setTemplates(Map)} and {@link #generateSources(File)}. It is recommended to generate the code
 * to its final location; the templates support adding code after the generation that will be preserved in subsequent
 * generations. To do so, add a block to the template that follows the following (set {@code block_name} to an arbitrary
 * word:
 * <p style="font-family: monospace">
 * // block_name &gt;&gt;<br>
 * #if($customContent.block_name)${customContent.block_name}#end<br>
 * // &lt;&lt; block_name
 * </p>
 * <p>This is going to be read by the generation process and any code added between these comments is going to be
 * preserved.</p>
 * <p>Properties can be used to change the Java name of any element, by creating a property prefixed "{@code substitute.}" and followed by 
 * the full name of the element. For instance if there is a group named {@code group} and it contains a variable named {@code v1}, this entry would change
 * the variable name to {@code myVariable}: {@code substitute.group.v1=myVariable}.</p>
 * 
 * @author Henrique Sousa
 */
public class NCMLCodeGenerator {

    public static final String CFG_PROPERTIES_LOCATION = "propertiesLocation";
    public static final String DEFAULT_PROPERTIES_LOCATION = "ncml-binding.properties";
    public static final String SCALAR_DIMENSION = "lengthEquals1AsScalar";
    // TODO move constants to interface, add implementations: plain java, ucar references; add standard to constructor
    public static final String TEMPLATE_VALUE_OBJECT = "/templates/ValueObject.java.vtl";
    public static final String TEMPLATE_NETCDF_WRAPPER = "/templates/NetcdfWrapper.java.vtl";
    public static final String TEMPLATE_DATA_INTERFACE = "/templates/DataInterface.java.vtl";
    public static final String TEMPLATE_INITIALIZER = "/templates/Initializer.java.vtl";
    
    private static final Logger LOG = LoggerFactory.getLogger(NCMLCodeGenerator.class);

    private final Netcdf schema;
    private final Properties properties;
    private String modelPackage;
    private String rootGroupName;
    private VelocityEngine velocity;
    private Map<String, BiFunction<AbstractGroupWrapper, File, File>> templates = new HashMap<>();
    private Properties initialConfiguration;
    private Map<String, Object> contextValues;

    /**
     * Creates a code generator based on a CDL XML Header.
     * 
     * @param headerLocation location of the XML header
     * @param properties     properties for further customization
     * @throws JAXBException if any error results from reading the provided XML
     */
    public NCMLCodeGenerator(URL headerLocation, Properties properties) throws JAXBException {
        this.properties = properties;
        JAXBContext jaxbContext = JAXBContext.newInstance(Netcdf.class);
        Unmarshaller schemaReader = jaxbContext.createUnmarshaller();
        schema = (Netcdf) schemaReader.unmarshal(headerLocation);

        String loaderClassFmt = RESOURCE_LOADER + ".%s." + RESOURCE_LOADER_CLASS;
        Map<String, Class<? extends ResourceLoader>> resourceLoaders = new LinkedHashMap<>();
        Map<String, Map<String, Object>> configuration = new HashMap<>();
        resourceLoaders.put("file", FileResourceLoader.class);
        configuration.computeIfAbsent("file", key -> {
           Map<String, Object> config = new HashMap<>();
           config.put(RESOURCE_LOADER_PATHS, ".,/,src/main/resources,src/test/resources");
           return config;
        });
        resourceLoaders.put("classpath", ClasspathResourceLoader.class);

        velocity = new VelocityEngine();
        velocity.setProperty(RESOURCE_LOADERS, resourceLoaders.keySet().stream().collect(Collectors.joining(",")));
        resourceLoaders.forEach((name, type) -> {
            velocity.setProperty(String.format(loaderClassFmt, name), type.getName());
            if (configuration.containsKey(name)) {
                configuration.get(name).forEach((cfgKey, value) -> {
                    velocity.setProperty(RESOURCE_LOADER + "." + name + "." + cfgKey, value);
                });
            }
        });
        velocity.setProperty("velocimacro.inline.local_scope", true);
        //velocity.setProperty(RESOURCE_LOADER_PATHS, ".,/");
        velocity.init();

        templates.put(TEMPLATE_DATA_INTERFACE,
                (group, destDir) -> new File(destDir, group.getTypeName() + ".java"));
        templates.put(TEMPLATE_NETCDF_WRAPPER,
                (group, destDir) -> new File(destDir, group.getTypeName() + "Wrapper.java"));
        templates.put(TEMPLATE_VALUE_OBJECT,
                (group, destDir) -> new File(destDir, group.getTypeName() + "VO.java"));
        templates.put(TEMPLATE_INITIALIZER,
                (group, destDir) -> new File(destDir, group.getTypeName() + "Initializer.java"));
    }

    /**
     * Provides an unmodifiable view of the templates that are going to be used during generation. The keys of this map
     * are paths to templates (to be searched for in the current classpath) and values are functions that determine the
     * output file name from the destination location (up to the package) and the group to be written. Should this
     * function return null, no generation will be performed for the group.
     * 
     * @return unmodifiable view of templates to be used in generation
     */
    public Map<String, BiFunction<AbstractGroupWrapper, File, File>> getTemplates() {
        return Collections.unmodifiableMap(templates);
    }

    /**
     * Sets the templates that are going to be used during the next generation. See {@link #getTemplates()} for details.
     * 
     * @param templates templates to be used in the next call to {@link #generateSources(File)}
     */
    public void setTemplates(Map<String, BiFunction<AbstractGroupWrapper, File, File>> templates) {
        this.templates = templates;
    }

    /**
     * Sets additional context values to be made available to the templates. This class does not prevent name
     * collisions.
     * 
     * @param contextValues map containing all values that will be added to the the Velocity context passed to templates
     */
    public void setContextValues(Map<String, Object> contextValues) {
        this.contextValues = contextValues;
    }

    /**
     * Sets the package in which code will be generated.
     * 
     * @param modelPackage package for the generation of code
     */
    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    /**
     * Sets the name of the root group. A CDL header file does not name the root group, so this is necessary to keep
     * consistency.
     * 
     * @param rootGroupName name of the root group
     */
    public void setRootGroupName(String rootGroupName) {
        this.rootGroupName = rootGroupName;
    }

    /**
     * Generates code to a selected destination.
     * 
     * @param destination the base directory where code should be written
     * @throws IOException if the files cannot be written to
     */
    public void generateSources(File destination) throws IOException {
        initialConfiguration = new Properties();
        AbstractGroupWrapper rootGroup = new SchemaWrapper(schema, modelPackage, rootGroupName, properties);
        rootGroup.initializeConfiguration(initialConfiguration);
        generate(rootGroup, destination);
        try (FileWriter writer = new FileWriter(properties.getProperty(
                CFG_PROPERTIES_LOCATION, DEFAULT_PROPERTIES_LOCATION))) {
            initialConfiguration.store(writer, "Initial configuration");
        }
        initialConfiguration = null;
    }

    private void generate(AbstractGroupWrapper group, File destination) throws IOException {
        // limit scope of variables to insulate from recursion and save some memory
        {
            VelocityContext context = new VelocityContext();
            // TODO generator tag information?
            if (contextValues != null) {
                for (Entry<String, Object> entry : contextValues.entrySet()) {
                    context.put(entry.getKey(), entry.getValue());
                }
            }
            context.put("group", group);
            context.put("escapeString", (Function<String,String>) str -> str.replaceAll("[\"\\\\]", "\\\\$0").replace("\n", "\\n"));

            File packageDir = new File(destination, group.getPackageName().replace('.', File.separatorChar));
            packageDir.mkdirs();

            for (Entry<String, BiFunction<AbstractGroupWrapper, File, File>> entry : templates.entrySet()) {
                String templatePath = entry.getKey();
                LOG.debug("Generating code using template {} for group {}", templatePath, group.getName());

                Map<String, String> customContent = new HashMap<>();
                context.put("customContent", customContent);
                context.put("configuration", properties);
                File destFile = entry.getValue().apply(group, packageDir);
                if (destFile == null) {
                    continue; // skip requested
                }
                if (destFile.isFile()) {
                    readCustomContent(destFile, customContent);
                }

                Template dataModelTemplate = velocity.getTemplate(templatePath);
                try (FileWriter writer = new FileWriter(destFile)) {
                    dataModelTemplate.merge(context, writer);
                }
            }
        }
        for (AbstractGroupWrapper childGroup : group.getGroups()) {
            generate(childGroup, destination);
        }
    }

    private void readCustomContent(File destFile, Map<String, String> customContent) throws IOException {
        Pattern sectionStart = Pattern.compile("\\s*+//\\s*+(\\w++)\\s*+>>");
        Pattern sectionStop = Pattern.compile("\\s*+//\\s*+<<\\s*+(\\w++)");
        try (BufferedReader reader = new BufferedReader(new FileReader(destFile))) {
            String line;
            StringBuffer section = null;
            String sectionName = null;
            while ((line = reader.readLine()) != null) {
                if (section != null) {
                    Matcher stopper = sectionStop.matcher(line);
                    if (stopper.matches()) {
                        if (stopper.group(1).equals(sectionName)) {
                            customContent.put(sectionName, section.toString());
                            section = null;
                            sectionName = null;
                            continue;
                        } else {
                            LOG.warn("section terminator missing for {} at {}", sectionName, destFile);
                        }
                    }
                }
                Matcher starter = sectionStart.matcher(line);
                if (starter.matches()) {
                    sectionName = starter.group(1);
                    section = new StringBuffer();
                    continue;
                }

                if (section != null) {
                    section.append(line).append('\n');
                }
            }
            if (section != null) {
                LOG.warn("section terminator missing for {} at {}", sectionName, destFile);
            }
        }
    }

}
