package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

public class VariableNode extends Node {

    public final Token token;

    public VariableNode(Token token) {
        this.token = token;
        this.codePos = token.p;
    }

    @Override
    public String toString() {
        return "" + token.c + "";
    }
}
