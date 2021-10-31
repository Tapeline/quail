package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class LiteralNumNode extends Node {

    public final Token token;

    public LiteralNumNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LiteralNumNode[" + token + "]";
    }

}
