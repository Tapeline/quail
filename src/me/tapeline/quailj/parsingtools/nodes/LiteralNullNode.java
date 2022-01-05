package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class LiteralNullNode extends Node {

    public final Token token;

    public LiteralNullNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LiteralNull";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return new DefaultMutableTreeNode("Null");
    }
}
