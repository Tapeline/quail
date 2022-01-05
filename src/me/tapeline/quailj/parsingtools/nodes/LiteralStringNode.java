package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class LiteralStringNode extends Node {

    public final Token token;

    public LiteralStringNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "\"" + token + "\"";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return new DefaultMutableTreeNode("String " + token.c);
    }

}
