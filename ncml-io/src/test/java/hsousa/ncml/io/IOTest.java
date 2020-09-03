package hsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import hsousa.netcdf.schemagen.AbstractGroupWrapper;
import hsousa.netcdf.schemagen.NCMLCodeGenerator;

public class IOTest {

    protected static final FileVisitor<Path> DELETE_ALL = new FileVisitor<Path>() {
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
    private static final FileFilter SOURCE_FILTER = pathname -> pathname.isFile()
            && pathname.getName().endsWith(".java");
    private static final FileFilter DIR_FILTER = pathname -> pathname.isDirectory();

    public void generateModel(URL schemaURL, File sourcesDir, File classesDir, String rootClassName, Properties properties) throws Exception {
        String rootPackage = rootClassName.substring(0, rootClassName.lastIndexOf('.'));
        String rootGroupName = rootClassName.substring(rootPackage.length() + 1);

        NCMLCodeGenerator generator = new NCMLCodeGenerator(schemaURL, properties);
        Map<String, BiFunction<AbstractGroupWrapper, File, File>> templates = new HashMap<>(generator.getTemplates());
        templates.put(NCMLCodeGenerator.TEMPLATE_NETCDF_WRAPPER,
                (group, destDir) -> new File(destDir, group.getTypeName() + "Wrapper.java"));
        generator.setTemplates(templates);
        generator.setModelPackage(rootPackage);
        generator.setRootGroupName(rootGroupName);
        generator.generateSources(sourcesDir);

        // compile to the current classpath to simplify class loading
        compileModel(sourcesDir, classesDir);
    }

    protected void compileModel(File sourcesDir, File classesDir) {
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

    private void addSources(File dir, List<File> result) {
        Arrays.asList(dir.listFiles(SOURCE_FILTER)).forEach(file -> result.add(file));
        Arrays.asList(dir.listFiles(DIR_FILTER)).forEach(subDir -> addSources(subDir, result));
    }

}
