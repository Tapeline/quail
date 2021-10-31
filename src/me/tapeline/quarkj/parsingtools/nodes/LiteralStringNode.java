package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class LiteralStringNode extends Node {

    public final Token token;

    public LiteralStringNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LiteralStringNode[" + token + "]";
    }

}
