package hsousa.netcdf.schemagen;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class AbstractCodeGenerationTest {

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
    protected final FileFilter sourcesFilter = pathname -> pathname.isFile() && pathname.getName().endsWith(".java");
    protected final FileFilter dirFilter = pathname -> pathname.isDirectory();

    public AbstractCodeGenerationTest() {
        super();
    }

    protected void generateCode(File sourcesDir, String rootPackage, String rootGroupName, String headerPath, Properties properties) throws Exception {
        NCMLCodeGenerator generator = new NCMLCodeGenerator(getClass().getResource(headerPath),
                properties);
        HashMap<String, BiFunction<AbstractGroupWrapper, File, File>> templates = new HashMap<>(generator.getTemplates());
        templates.remove(NCMLCodeGenerator.TEMPLATE_NETCDF_WRAPPER);
        generator.setTemplates(templates);
        generator.setModelPackage(rootPackage); // TODO required? move to constructor or snake config
        generator.setRootGroupName(rootGroupName); // TODO required? move to constructor or snake config
        generator.generateSources(sourcesDir);
    }

    protected int compileCode(File sourcesDir, File classesDir) {
        List<File> sources = new ArrayList<>();
        addSources(sourcesDir, sources);
        assertThat("Nothing to compile", sources.isEmpty(), is(false));
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> arguments = new ArrayList<>();
        arguments.addAll(Arrays.asList("-cp", System.getProperty("java.class.path"),
                "-g", "-d", classesDir.getAbsolutePath()));
    
        arguments.addAll(sources.stream().map(file -> file.getPath()).collect(Collectors.toList()));
        final int compilerResultCode = compiler.run(null, null, null, arguments.toArray(new String[0]));
        return compilerResultCode;
    }

    private void addSources(File dir, List<File> result) {
        Arrays.asList(dir.listFiles(sourcesFilter)).forEach(file -> result.add(file));
        Arrays.asList(dir.listFiles(dirFilter)).forEach(subDir -> addSources(subDir, result));
    }

}