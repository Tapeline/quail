package me.tapeline.quailj.runtime.libraries;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.effect.LibraryPath;
import me.tapeline.quailj.parsing.nodes.operators.FieldReferenceNode;
import me.tapeline.quailj.platforms.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.libraries.domain.DomainNode;
import me.tapeline.quailj.runtime.libraries.domain.ExtractDomainNode;
import me.tapeline.quailj.runtime.libraries.domain.LibraryDomain;
import me.tapeline.quailj.runtime.libraries.domain.SimpleDomainNode;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.utils.ErrorFormatter;
import me.tapeline.quailj.utils.Utilities;

import java.io.File;
import java.util.*;

public class LibraryLoader {

    public static void loadLibrary(Runtime runtime, LibraryPath libraryPath, String alias) throws RuntimeStriker {
        String actualLibrary = libraryPath.getActualLibraryPath();
        String[] actualLibraryPathComponents = actualLibrary.split("\\.");
        LibraryDomain targetDomain = Runtime.libraryRegistry.getDomain(actualLibrary);
        QObject library = null;
        if (targetDomain == null) {
            File libraryFile = getLibraryFile(new LibraryPath(actualLibraryPathComponents, false));
            if (libraryFile == null)
                runtime.error("Unable to find library " + libraryPath.toString() + "\n" +
                        "Library wasn't found neither in registry or in following directories:\n" +
                        Utilities.collectionToString(Runtime.libraryRegistry.libraryRoots, "\n") + "\n" +
                        "Please check library installation and/or correct library root dir(s) definition");
            else
                library = loadComponent(
                        runtime,
                        new LibraryPath(actualLibraryPathComponents, false),
                        libraryFile
                );
        } else {
            library = Runtime.libraryRegistry.getLibrary(actualLibrary);
        }

        if (library == null)
            runtime.error("Unable to find library " + libraryPath.toString() + "\n" +
                    "Library wasn't found neither in registry or in following directories:\n" +
                    Utilities.collectionToString(Runtime.libraryRegistry.libraryRoots, "\n") + "\n" +
                    "Please check library installation and/or correct library root dir(s) definition");

        HashMap<String, QObject> extract = null;
        if (libraryPath.isExtracting())
            extract = extractLibraryContents(library);

        if (extract != null) {
            for (Map.Entry<String, QObject> entry : extract.entrySet())
                runtime.memory.set(entry.getKey(), entry.getValue(), new ArrayList<>());
        } else {
            runtime.memory.set(
                    alias == null?
                        actualLibraryPathComponents[actualLibraryPathComponents.length - 1]
                        :
                        alias,
                    library,
                    new ArrayList<>()
            );
        }
    }

    public static HashMap<String, QObject> extractLibraryContents(QObject library) {
        return library.getNonDefaultFields();
    }

    public static File getLibraryFile(LibraryPath libraryPath) {
        String libraryFilePathPart = libraryPath.getActualLibraryPath();
        File libraryFile = null;
        for (String libraryRoot : Runtime.libraryRegistry.libraryRoots) {
            String commonPath = libraryRoot.endsWith("/")?
                    libraryRoot + libraryFilePathPart : libraryRoot + "/" + libraryFilePathPart;
            File possibleLibraryDir = new File(commonPath);
            File possibleLibraryFile = new File(commonPath + ".q");
            if (possibleLibraryFile.exists()) {
                libraryFile = possibleLibraryFile;
                break;
            } else if (possibleLibraryDir.exists()) {
                libraryFile = possibleLibraryDir;
                break;
            }
        }
        return libraryFile;
    }

    public static QObject loadComponent(Runtime runtime, LibraryPath componentPath, File component)
            throws RuntimeStriker {
        if (!component.exists())
            runtime.error("Unable to find library (while importing another) " + componentPath.toString() + "\n" +
                    "Library wasn't found neither in registry or in following directories:\n" +
                    Utilities.collectionToString(Runtime.libraryRegistry.libraryRoots, "\n") + "\n" +
                    "Please check library installation and/or correct library root dir(s) definition");
        if (component.isDirectory()) {
            HashMap<String, QObject> packageContents = new HashMap<>();
            if (component.listFiles() == null)
                return QObject.Val(packageContents);
            for (File subFile : Objects.requireNonNull(component.listFiles())) {
                String subEntryId = subFile.getName().split("\\.")[0];
                QObject loadedSubEntry = loadComponent(
                        runtime,
                        new LibraryPath(
                                Utilities.arrayAppend(componentPath.pathComponents, subEntryId),
                                false
                        ),
                        subFile
                );
                packageContents.put(subEntryId, loadedSubEntry);
            }
            QObject lib = QObject.Val(packageContents);
            lib.setObjectMetadata(Runtime.superObject);
            lib.isDict = true;
            lib.setPrototypeFlag(true);
            return lib;
        } else {
            String code = IOManager.fileInput(component.getPath());
            QObject result = null;
            try {
                Lexer lexer = new Lexer(code);
                List<Token> tokens = lexer.scan();
                Parser parser = new Parser(code, tokens);
                Node root = parser.parse();
                Runtime inner = new Runtime(code, root, runtime.io, false);
                result = inner.run(root, inner.memory);
            } catch (RuntimeStriker striker) {
                if (striker.type == RuntimeStriker.Type.EXCEPTION || 
striker.type == RuntimeStriker.Type.EXIT) {
                    System.err.println(ErrorFormatter.formatError(code, striker.error));
                    System.exit(1);
                } else if (striker.type == RuntimeStriker.Type.RETURN) {
                    result = striker.returnValue;
                }
            } catch (ParserException exception) {
                System.err.println(exception.getMessage());
                System.exit(1);
            }
            if (result == null) {
                runtime.error("Library " + component.toString() + " does not return (provide) anything");
                return null;
            }
            Runtime.libraryRegistry.cacheLibrary(componentPath.getActualLibraryDomain(), result);
            return result;
        }
    }


    public static Object recursiveLoad(Runtime runtime, QObject container, DomainNode node, DomainNode superNode) {
        if (node instanceof ExtractDomainNode) {
            // container == null => error
            // Extract
            // return extracted
        } else if (node instanceof SimpleDomainNode) {
            // Run
            // if container == null => loadfile(supernode)
            // else                 => container.node
            // register
        }
    }

}
