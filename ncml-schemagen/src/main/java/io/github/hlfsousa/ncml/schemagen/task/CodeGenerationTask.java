package io.github.hlfsousa.ncml.schemagen.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;

import io.github.hlfsousa.ncml.schemagen.AbstractGroupWrapper;
import io.github.hlfsousa.ncml.schemagen.NCMLCodeGenerator;

/**
 * Executable class for generating code. Run with no arguments for usage.
 * 
 * @author Henrique Sousa
 */
public class CodeGenerationTask {

    private static final String USAGE_TEXT = "usage: java CodeGenerationTask header [-p properties] root_name [-o out_directory] templates\n"
            + "\theader: path or url to the header file\n"
            + "\tproperties: configuration file (.xml or .properties) for customizations\n"
            + "\troot_name: canonical name of the type that should represent the generated classes\n"
            + "\tout_directory: optional location in the filesystem where code will be generated. Default to the current directory\n"
            + "\ttemplates: (template)*; an optional space-separated sequence of additional templates to use in generation\n"
            + "\t\ttemplate: path/suffix; definition of an additional template to use in generation\n"
            + "\t\t\tpath: a file path or url pointing to an NcML header\n"
            + "\t\t\tsuffix: the name of each file generated with the template will be formed using the type name "
            + "followed by this suffix, which should include file extension.";

    private URL headerURL;
    private File propertiesFile;
    private File sourcesDir;
    private String rootClassName;

    private Map<String, BiFunction<AbstractGroupWrapper, File, File>> additionalTemplates;

    public CodeGenerationTask() {
        additionalTemplates = new HashMap<>();
    }

    /**
     * URL for the location of the header file from which code will be generated.
     * 
     * @return the header location URL
     */
    public URL getHeaderURL() {
        return headerURL;
    }

    public void setHeaderURL(URL headerURL) {
        this.headerURL = headerURL;
    }

    public File getPropertiesFile() {
        return propertiesFile;
    }
    
    public void setPropertiesFile(File propertiesFile) {
        this.propertiesFile = propertiesFile;
    }
    
    /**
     * Destination root for generating source code.
     * 
     * @return the base directory where source code will be generated
     */
    public File getSourcesDir() {
        return sourcesDir;
    }

    public void setSourcesDir(File sourcesDir) {
        this.sourcesDir = sourcesDir;
    }

    /**
     * Canonical name of the root class that represent the NetCDF.
     * 
     * @return the canonical name of the root class
     */
    public String getRootClassName() {
        return rootClassName;
    }

    public void setRootClassName(String rootClassName) {
        this.rootClassName = rootClassName;
    }

    /**
     * Adds a template in the classpath for generation.
     * 
     * @param path   location of the template, relative to a classpath location
     * @param suffix the file will be placed at {@link #getSourcesDir()}, in a subdirectory that represents the package
     *               of {@link #getRootClassName()}. Then {@code suffix} is appended to the file name. It should include
     *               file extension.
     */
    public void addTemplate(String path, String suffix) {
        additionalTemplates.put(path,
                (group, destDir) -> new File(destDir, group.getTypeName() + suffix));
    }

    /**
     * Generates the code based on parameters set.
     */
    public void generateCode() throws Exception {
        String rootPackage = rootClassName.substring(0, rootClassName.lastIndexOf('.'));
        String rootGroupName = rootClassName.substring(rootPackage.length() + 1);
        
        NCMLCodeGenerator generator = new NCMLCodeGenerator(headerURL, readProperties());
        Map<String, BiFunction<AbstractGroupWrapper, File, File>> templates = new HashMap<>(generator.getTemplates());
        templates.putAll(additionalTemplates);
        generator.setTemplates(templates);
        generator.setModelPackage(rootPackage);
        generator.setRootGroupName(rootGroupName);
        generator.generateSources(sourcesDir);
    }

    private Properties readProperties() {
        Properties properties = new Properties();
        if (propertiesFile != null) {
            if (propertiesFile.getName().endsWith(".xml")) {
                try (FileInputStream in = new FileInputStream(propertiesFile)) {
                    properties.loadFromXML(in);
                } catch (IOException e) {
                    throw new IllegalArgumentException(propertiesFile.getAbsolutePath(), e);
                }
            } else {
                try (FileReader reader = new FileReader(propertiesFile)) {
                    properties.load(reader);
                } catch (IOException e) {
                    throw new IllegalArgumentException(propertiesFile.getAbsolutePath(), e);
                }
            }
        }
        return properties;
    }

    private static void help() {
        System.out.println(USAGE_TEXT);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            help();
            System.exit(1);
        }
        int argIdx = 0;
        CodeGenerationTask task = new CodeGenerationTask();
        task.setHeaderURL(parseLocation(args[argIdx++]));
        if ("-p".equals(args[argIdx])) {
            ++argIdx;
            File file = new File(args[argIdx++]);
            if (!file.isFile()) {
                throw new IllegalArgumentException("Properties file does not exist: " + file.getPath());
            }
            task.setPropertiesFile(file);
        }
        task.setRootClassName(args[argIdx++]);
        if ("-o".equals(args[argIdx])) {
            task.setSourcesDir(new File(args[++argIdx]));
            argIdx++;
        } else {
            task.setSourcesDir(new File("."));
        }
        while (argIdx < args.length) {
            String templateArg = args[argIdx++];
            final int separatorIndex = templateArg.lastIndexOf('/');
            String location = templateArg.substring(0, separatorIndex);
            String suffix = templateArg.substring(separatorIndex + 1);
            task.addTemplate(location, suffix);
        }

        task.generateCode();
    }

    private static URL parseLocation(String location) throws MalformedURLException {
        File localFile = new File(location);
        if (localFile.exists()) {
            return localFile.toURI().toURL();
        }
        return new URL(location);
    }

}
