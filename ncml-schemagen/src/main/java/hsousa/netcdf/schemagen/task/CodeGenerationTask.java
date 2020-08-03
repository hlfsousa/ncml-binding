package hsousa.netcdf.schemagen.task;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;

import hsousa.netcdf.schemagen.AbstractGroupWrapper;
import hsousa.netcdf.schemagen.NCMLCodeGenerator;

/**
 * Executable class for generating code. Run with no arguments for usage.
 * 
 * @author Henrique Sousa
 */
public class CodeGenerationTask {

    private static final String USAGE_TEXT = "usage: java CodeGenerationTask header root_name [-o out_directory] templates\n"
            + "\theader: path or url to the header file\n"
            + "\troot_name: canonical name of the type that should represent the generated classes"
            + "\tout_directory: optional location in the filesystem where code will be generated. Default to the current directory"
            + "\ttemplates: (template)*; an optional space-separated sequence of additional templates to use in generation\n"
            + "\t\ttemplate: path/suffix; definition of an additional template to use in generation\n"
            + "\t\t\tpath: a file path or url pointing to an NcML header\n"
            + "\t\t\tsuffix: the name of each file generated with the template will be formed using the type name "
            + "followed by this suffix, which should include file extension.";

    private URL headerURL;
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
                (group, destDir) -> new File(destDir, group.camelCase(group.getName()) + suffix));
    }

    /**
     * Generates the code based on parameters set.
     */
    public void generateCode() throws Exception {
        String rootPackage = rootClassName.substring(0, rootClassName.lastIndexOf('.'));
        String rootGroupName = rootClassName.substring(rootPackage.length() + 1);

        Properties properties = new Properties();
        NCMLCodeGenerator generator = new NCMLCodeGenerator(headerURL, properties);
        Map<String, BiFunction<AbstractGroupWrapper, File, File>> templates = new HashMap<>(generator.getTemplates());
        templates.put("/templates/NetcdfWrapper.java.vtl",
                (group, destDir) -> new File(destDir, group.camelCase(group.getName()) + "Wrapper.java"));
        templates.putAll(additionalTemplates);
        generator.setTemplates(templates);
        generator.setModelPackage(rootPackage);
        generator.setRootGroupName(rootGroupName);
        generator.generateSources(sourcesDir);
    }

    private static void help() {
        System.out.println(USAGE_TEXT);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            help();
            System.exit(1);
        }

        CodeGenerationTask task = new CodeGenerationTask();
        task.setHeaderURL(parseLocation(args[0]));
        task.setRootClassName(args[1]);
        int templateIdx = 2;
        if ("-o".equals(args[2])) {
            task.setSourcesDir(new File(args[3]));
            templateIdx = 4;
        } else {
            task.setSourcesDir(new File("."));
        }
        for (int i = templateIdx; i < args.length; i++) {
            final int separatorIndex = args[i].lastIndexOf('/');
            String location = args[i].substring(0, separatorIndex);
            String suffix = args[i].substring(separatorIndex + 1);
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
