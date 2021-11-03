package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class LiteralFunctionNode extends Node {

    public final Token name;
    public final Node args;
    public final BlockNode code;

    public LiteralFunctionNode(Token name, Node args, BlockNode code) {
        this.name = name;
        this.args = args;
        this.code = code;
    }

    @Override
    public String toString() {
        return "LiteralFunctionNode-" + name + "-" + args + "=" + code;
    }

}
