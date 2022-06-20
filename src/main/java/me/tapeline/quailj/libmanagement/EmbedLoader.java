package me.tapeline.quailj.libmanagement;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    public static Embed loadFromJar(File file, String mainClass) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + file + "!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            Class c = cl.loadClass(className);
            if (className.equals(mainClass)) {
                return (Embed) c.newInstance();
            }
            if (Embed.class.isAssignableFrom(c)) {
                return (Embed) c.newInstance();
            }
        }

        return null;
    }

}

