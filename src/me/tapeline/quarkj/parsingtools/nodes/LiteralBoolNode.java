package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class LiteralBoolNode extends Node {

    public final Token token;

    public LiteralBoolNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LiteralBoolNode[" + token + "]";
    }

}
