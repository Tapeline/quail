package me.tapeline.quailj.translator;

import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.parser.nodes.*;
import me.tapeline.quailj.runtime.Runtime;

import java.util.List;

public class Translator {

    public String translate(List<Token> tokenList) {
        String s = "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import me.tapeline.quailj.lexer.Lexer;\n" +
                "import me.tapeline.quailj.lexer.Token;\n" +
                "import me.tapeline.quailj.lexer.TokenType;\n" +
                "import me.tapeline.quailj.parser.Parser;\n" +
                "import me.tapeline.quailj.parser.nodes.Node;\n" +
                "import me.tapeline.quailj.preprocessor.Preprocessor;\n" +
                "import me.tapeline.quailj.platformspecific.IOManager;\n" +
                "import me.tapeline.quailj.runtime.Runtime;\n" +
                "import me.tapeline.quailj.types.AbstractFunc;\n" +
                "import me.tapeline.quailj.types.RuntimeStriker;\n" +
                "import me.tapeline.quailj.utils.Utilities;\n" +
                "public class Main {\n\tpublic static void main(String[] args) throws RuntimeStriker {\n" +
                "\t\tList<Token> tokens = new ArrayList<>();\n";
        for (Token t : tokenList) {
            s += "\t\ttokens.add(new Token(TokenType." + t.t + ", \"" + t.c + "\", " + t.p + "));\n";
        }
        s += "\t\tParser parser = new Parser(tokens);\n" +
                "\t\tNode root = parser.parse();\n";
        s += "\t\tRuntime runtime = new Runtime(root, new IOManager(), \"\", false);\n" +
             "\t\truntime.code = \"\";\n" +
                "\t\truntime.run(root, runtime.scope);\n";
        s += "\t}\n}\n";
        return s;
    }

}
