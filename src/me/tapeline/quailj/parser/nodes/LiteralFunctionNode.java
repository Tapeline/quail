package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

public class LiteralFunctionNode implements Node {

    public int codePos = 0;
    public final Token name;
    public final Node args;
    public final BlockNode code;
    public boolean isStatic = false;

    public LiteralFunctionNode(Token name, Node args, BlockNode code) {
        this.name = name;
        this.args = args;
        this.code = code;
        this.codePos = name.p;
    }

    public LiteralFunctionNode(Token name, Node args, BlockNode code, boolean s) {
        this.name = name;
        this.args = args;
        this.code = code;
        this.isStatic = s;
        this.codePos = name.p;
    }

    @Override
    public String toString() {
        return "func " + name.c + args.toString() + code.toString();
    }
}
