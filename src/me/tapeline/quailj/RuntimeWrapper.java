package me.tapeline.quailj;

import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.List;

public class RuntimeWrapper {

    private final boolean debug;
    private String code;

    public RuntimeWrapper(String code, boolean debug) {
        this.code = code;
        this.debug = debug;
    }

    public QType run() throws RuntimeStriker {
        try {
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.lexAndFix();

            Parser parser = new Parser(tokens);
            Node root = parser.parse();

            Runtime runtime = new Runtime(root, new IOManager());

            return runtime.run(root, runtime.scope);
        } catch (RuntimeStriker striker) {
            if (striker.isNotException()) {
                return striker.val;
            } else {
                throw striker;
            }
        }
    }

}
