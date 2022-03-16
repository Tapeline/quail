package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

public class LiteralBoolNode implements Node {

    public int codePos = 0;
    public final Token token;

    public LiteralBoolNode(Token token) {
        this.token = token;
        this.codePos = token.p;
    }

    @Override
    public String toString() {
        return "Bool<" + token.c + ">";
    }
}
