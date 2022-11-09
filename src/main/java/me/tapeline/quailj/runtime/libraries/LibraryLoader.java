package me.tapeline.quailj.runtime.libraries;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.platforms.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.utils.ErrorFormatter;
import me.tapeline.quailj.utils.Utilities;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class LibraryLoader {

    public static QObject loadLibrary(Runtime runtime, LibraryRegistry registry, String name) throws RuntimeStriker {
        QObject cached = registry.getLibrary(name);
        if (cached == null) {
            for (String pathWildcard : registry.libraryRoots) {
                File possibleFile = new File(pathWildcard.replaceAll("\\?", name));
                if (possibleFile.exists()) {
                    String code = IOManager.fileInput(possibleFile.getAbsolutePath());
                    QObject result = null;
                    try {
                        Lexer lexer = new Lexer(code);
                        List<Token> tokens = lexer.scan();
                        Parser parser = new Parser(code, tokens);
                        Node root = parser.parse();
                        Runtime inner = new Runtime(code, root, runtime.io, false);
                        result = inner.run(root, inner.memory);
                    } catch (RuntimeStriker striker) {
                        if (striker.type == RuntimeStriker.Type.EXCEPTION) {
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
                        runtime.error("Library " + possibleFile + " does not return (provide) anything");
                        return null;
                    }
                    registry.cacheLibrary(name, result);
                    return result;
                }
            }
            runtime.error("Unable to find library " + name + "\n" +
                    "Library wasn't found neither in registry or in following directories:\n" +
                    Utilities.collectionToString(registry.libraryRoots, "\n") + "\n" +
                    "Please check library installation and/or correct library root dir(s) definition");
            return null;
        } else return registry.getLibrary(name);
    }

}
