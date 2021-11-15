package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class EndNode extends Node {

    public final Token token;

    public EndNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "end";
    }

}
