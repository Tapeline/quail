package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class LiteralDefinitionNode extends Node {

    public final Token variable;
    public final Token type;

    public LiteralDefinitionNode(Token token, Token type) {
        this.variable = token;
        this.type = type;
    }

    @Override
    public String toString() {
        return "LiteralDefinitionNode:" + type + "[" + variable + "]";
    }

}
