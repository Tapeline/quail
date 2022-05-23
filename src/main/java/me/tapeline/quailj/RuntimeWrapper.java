package me.tapeline.quailj;

import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.builtins.library_canvas.LibraryCanvas;
import me.tapeline.quailj.runtime.builtins.library_fs.LibraryFs;
import me.tapeline.quailj.runtime.builtins.library_math.LibraryMath;
import me.tapeline.quailj.runtime.builtins.library_nest.LibraryNest;
import me.tapeline.quailj.runtime.builtins.library_random.LibraryRandom;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.utils.Pair;
import me.tapeline.quailj.utils.Utilities;

import java.util.List;

public class RuntimeWrapper {

    private final boolean debug;
    private final IOManager io;
    private final String path;
    private String code;

    public RuntimeWrapper(String code, boolean debug, IOManager ioManager, String path) {
        this.code = code;
        this.debug = debug;
        this.io = ioManager;
        this.path = path;
    }

    public Pair<QType, Runtime> run() throws RuntimeStriker {
        Runtime runtime = null;
        try {
            Utilities.init();
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.lexAndFix();

            Parser parser = new Parser(tokens);
            Node root = parser.parse();
            if (debug) io.consolePut(root.toString() + "*-=-=-=-*\n");

            runtime = new Runtime(root, io, path);
            Runtime.registerLibrary(new LibraryRandom());
            Runtime.registerLibrary(new LibraryCanvas());
            Runtime.registerLibrary(new LibraryNest());
            Runtime.registerLibrary(new LibraryMath());
            Runtime.registerLibrary(new LibraryFs());

            QType r = runtime.run(root, runtime.scope);
            return new Pair<>(r, runtime);
        } catch (RuntimeStriker striker) {
            if (striker.isNotException()) {
                return new Pair<>(striker.val, runtime);
            } else {
                throw striker;
            }
        }
    }

}
