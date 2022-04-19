package me.tapeline.quailj.libmanagement;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EmbedLoader {

    public static Embed loadEmbed(File file, String mainClass, String scriptHome) {
        try {
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics,
                    null, null);

            // This sets up the class path that the compiler will use.
            // I've added the .jar file that contains the DoStuff interface within in it...
            List<String> optionList = new ArrayList<>();
            optionList.add("-classpath");
            optionList.add(System.getProperty("java.class.path") + File.pathSeparator +
                    "dist/InlineCompiler.jar");

            Iterable<? extends JavaFileObject> compilationUnit
                    = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(file));
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null,
                    fileManager,
                    diagnostics,
                    optionList,
                    null,
                    compilationUnit);
            if (task.call()) {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{
                        new File(scriptHome).toURI().toURL()
                });
                Class<?> loadedClass = classLoader.loadClass(mainClass);
                Object obj = loadedClass.newInstance();
                if (obj instanceof Embed) return (Embed) obj;
            } else {
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    System.out.format("Embed Error on line %d in %s%n: " + diagnostic.getMessage(Locale.ENGLISH),
                            diagnostic.getLineNumber(),
                            diagnostic.getSource().toUri());
                }
            }
            fileManager.close();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exp) {
            exp.printStackTrace();
        }
        return null;
    }

}

