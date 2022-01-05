package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class LiteralBoolNode extends Node {

    public final Token token;

    public LiteralBoolNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LiteralBoolNode[" + token + "]";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return new DefaultMutableTreeNode("Boolean " + token.c);
    }

}
