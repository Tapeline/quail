package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class LiteralEventNode extends Node {

    public final Token name;
    public final Token var;
    public final BlockNode code;
    public final Token mod;

    public LiteralEventNode(Token name, Token var, BlockNode code, Token mod) {
        this.name = name;
        this.var  = var ;
        this.code = code;
        this.mod  = mod ;
    }

    @Override
    public String toString() {
        return "LiteralEventNode-" + name + "-" + var + "=" + code;
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode(
                "Event Handler (" + name.c + ": " + (mod.c != null? mod.c : "no filter")  + ") => " + var.c);
        self.add(code.toTree());
        return self;
    }

}
