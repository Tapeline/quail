package me.tapeline.quailj;

import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.Node;
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
            System.out.println(tokens.toString());

            Parser parser = new Parser(tokens);
            Node root = parser.parse();

            System.out.println(root.toString());

            /*Runtime runtime = new Runtime(root);
            QType result = runtime.run();

            return result;*/
        } catch (RuntimeStriker striker) {
            if (striker.isNotException()) {
                return striker.val;
            } else {
                throw striker;
            }
        }
        return null;
    }

}
