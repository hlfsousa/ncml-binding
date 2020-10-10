package io.github.hlfsousa.ncml.schemagen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
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

    // TODO move constants to interface, add implementations: plain java, ucar references; add standard to constructor
    public static final String TEMPLATE_VALUE_OBJECT = "/templates/plain_java/ValueObject.java.vtl";
    public static final String TEMPLATE_NETCDF_WRAPPER = "/templates/plain_java/NetcdfWrapper.java.vtl";
    public static final String TEMPLATE_DATA_INTERFACE = "/templates/plain_java/DataInterface.java.vtl";
    public static final String TEMPLATE_INITIALIZER = "/templates/plain_java/Initializer.java.vtl";
    
    private static final Logger LOG = LoggerFactory.getLogger(NCMLCodeGenerator.class);

    private final Netcdf schema;
    private final Properties properties;
    private String modelPackage;
    private String rootGroupName;
    private VelocityEngine velocity;
    private Map<String, BiFunction<AbstractGroupWrapper, File, File>> templates = new HashMap<>();

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

        velocity = new VelocityEngine();
        velocity.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        velocity.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        velocity.setProperty("velocimacro.inline.local_scope", true);
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
     * output file name from the destination location (up to the package) and the group to be written.
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
        AbstractGroupWrapper rootGroup = new SchemaWrapper(schema, modelPackage, rootGroupName, properties);
        generate(rootGroup, destination);
    }

    private void generate(AbstractGroupWrapper group, File destination) throws IOException {
        // limit scope of variables to insulate from recursion and save some memory
        {
            VelocityContext context = new VelocityContext();
            // TODO generator tag information
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
