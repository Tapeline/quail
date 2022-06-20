package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class VariableNode extends Node {

    public final Token token;
    public boolean isConsumer = false;
    public List<String> modifiers = new ArrayList<>();

    public VariableNode(Token token) {
        this.token = token;
        this.codePos = token.p;
    }

    @Override
    public String toString() {
        return "" + token.c + (isConsumer? "..." : "");
    }
}
