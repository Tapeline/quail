package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class VariableNode extends Node {

    public final Token token;

    public VariableNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Variable " + token;
    }

    public String srepr() {
        return "Variable[" + token + "]";
    }

}
