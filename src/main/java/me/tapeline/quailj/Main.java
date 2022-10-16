package me.tapeline.quailj;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.typing.utils.Utilities;

public class Main {
    public static void main(String[] args) throws Exception {
        Utilities.init();
        String code = "a = 10;((a...) -> {print(a)})(a)\nclass A{}";
        Lexer l = new Lexer(code);
        try {
            System.out.println();
            Parser p = new Parser(code, l.scan());
            Node node = p.parse();
            System.out.println(node);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }
}