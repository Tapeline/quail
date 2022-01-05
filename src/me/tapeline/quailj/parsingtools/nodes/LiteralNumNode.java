package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class LiteralNumNode extends Node {

    public final Token token;

    public LiteralNumNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token.toString();
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return new DefaultMutableTreeNode("Number " + token.c);
    }

}
