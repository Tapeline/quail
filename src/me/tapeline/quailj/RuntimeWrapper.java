package me.tapeline.quailj;

import me.tapeline.quailj.debugtools.AALFrame;
import me.tapeline.quailj.debugtools.AdvancedActionLogger;
import me.tapeline.quailj.debugtools.DebugGUIManager;
import me.tapeline.quailj.interpretertools.Runtime;
import me.tapeline.quailj.interpretertools.RuntimeConfig;
import me.tapeline.quailj.language.types.QType;
import me.tapeline.quailj.language.types.RuntimeStriker;
import me.tapeline.quailj.parsingtools.Parser;
import me.tapeline.quailj.parsingtools.nodes.Node;
import me.tapeline.quailj.tokenizetools.Lexer;
import me.tapeline.quailj.tokenizetools.tokens.Token;

import java.util.List;

public class RuntimeWrapper {

    private final boolean DEBUG;
    private final String code;
    private final String name;

    public RuntimeWrapper(String code, boolean d, String name) {
        this.code = code;
        this.DEBUG = d;
        this.name = name;
    }

    public QType run() throws RuntimeStriker {
        AdvancedActionLogger aal = new AdvancedActionLogger();
        Lexer lexer = new Lexer(code, aal, name);
        lexer.lex();
        List<Token> tokens = lexer.fixBooleans();
        DebugGUIManager debugGUI = new DebugGUIManager();
        debugGUI.setTokenList(tokens);
        if (tokens == null) System.exit(100);
        if (DEBUG) debugGUI.displayTokens();

        Parser parser = new Parser(tokens, aal, name, code);
        Node codeNode = parser.parseCode();
        debugGUI.setNodeTree(codeNode);
        if (codeNode == null) System.exit(101);
        if (DEBUG) debugGUI.displayTree();

        Runtime runtime = new Runtime(codeNode, new RuntimeConfig() , aal, name, code);
        QType result = runtime.runTree();
        if (DEBUG) new AALFrame(aal);
        return result;
    }

}
