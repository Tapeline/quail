package me.tapeline.quailj;

import me.tapeline.quailj.debugging.NodeSaver;
import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.preprocessor.Preprocessor;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.builtins.library_canvas.LibraryCanvas;
import me.tapeline.quailj.runtime.builtins.library_fs.LibraryFs;
import me.tapeline.quailj.runtime.builtins.library_random.LibraryRandom;
import me.tapeline.quailj.runtime.builtins.library_math.LibraryMath;
import me.tapeline.quailj.runtime.builtins.library_nest.LibraryNest;
import me.tapeline.quailj.runtime.builtins.library_internals.LibraryInternals;
import me.tapeline.quailj.translator.Translator;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.utils.Pair;
import me.tapeline.quailj.utils.Utilities;

import java.util.List;

public class RuntimeWrapper {

    private final boolean debug;
    private final IOManager io;
    private final String path;
    private final boolean translate;
    private String code;
    private boolean timings;

    public RuntimeWrapper(String code, boolean debug, IOManager ioManager, String path, boolean timings,
                          boolean translate) {
        this.code = code;
        this.debug = debug;
        this.io = ioManager;
        this.path = path;
        this.timings = timings;
        this.translate = translate;
    }

    public Pair<QType, Runtime> run() throws RuntimeStriker {
        Runtime runtime = null;
        long start = System.currentTimeMillis();
        try {
            Utilities.init();
            Preprocessor preprocessor = new Preprocessor();
            code = preprocessor.process(code);
            if (timings) System.out.println("[Timings] Preprocessor done in " +
                    (System.currentTimeMillis() - start) + "ms");

            start = System.currentTimeMillis();
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.lexAndFix();
            if (timings) System.out.println("[Timings] Lexer done in " + (System.currentTimeMillis() - start) + "ms");

            if (translate) {
                Translator translator = new Translator();
                String code = translator.translate(tokens);
                IOManager.fileSet(path + ".java", code);
                return null;
            }

            start = System.currentTimeMillis();
            Parser parser = new Parser(tokens);
            Node root = parser.parse();
            if (debug) io.consolePut(root.toString() + "*-=-=-=-*\n");
            if (timings) System.out.println("[Timings] Parser done in " + (System.currentTimeMillis() - start) + "ms\n");

            start = System.currentTimeMillis();
            Runtime.registerLibrary(new LibraryInternals());
            Runtime.registerLibrary(new LibraryCanvas());
            Runtime.registerLibrary(new LibraryNest());
            Runtime.registerLibrary(new LibraryMath());
            Runtime.registerLibrary(new LibraryFs());
            Runtime.registerLibrary(new LibraryRandom());
            runtime = new Runtime(root, io, path, timings);
            runtime.code = code;

            QType r = runtime.run(root, runtime.scope);
            if (timings) System.out.println("\n[Timings] Runtime done in " + (System.currentTimeMillis() - start) + "ms");
            if (timings)
                IOManager.fileSet("QuailProfilingReport-" + "start" + ".json",
                        "{\"nodeReport\": " + NodeSaver.save(root) + ", " +
                        "\"memoryReport\": " + NodeSaver.saveMemoryActions() + "}");

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
