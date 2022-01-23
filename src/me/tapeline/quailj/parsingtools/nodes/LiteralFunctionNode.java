package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class LiteralFunctionNode extends Node {

    public final Token name;
    public final Node args;
    public final BlockNode code;
    public boolean s = false;

    public LiteralFunctionNode(Token name, Node args, BlockNode code) {
        this.name = name;
        this.args = args;
        this.code = code;
    }

    public LiteralFunctionNode(Token name, Node args, BlockNode code, boolean s) {
        this.name = name;
        this.args = args;
        this.code = code;
        this.s = s;
    }

    @Override
    public String toString() {
        return "LiteralFunctionNode-" + name + "-" + args + "=" + code;
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Function " + name.c);
        self.add(args.toTree());
        self.add(code.toTree());
        return self;
    }

}
