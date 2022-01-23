package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class VariableNode extends Node {

    public final Token token;

    public VariableNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token.c;
    }

    public String srepr() {
        return "Variable[" + token + "]";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return new DefaultMutableTreeNode("Variable " + token.c);
    }

}
