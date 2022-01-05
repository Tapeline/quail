package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class RawTokenNode extends Node {

    public final Token token;

    public RawTokenNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RawToken " + token;
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return new DefaultMutableTreeNode("Raw Token " + token.toString());
    }

}
