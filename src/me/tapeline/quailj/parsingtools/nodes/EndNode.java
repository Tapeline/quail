package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class EndNode extends Node {

    public final Token token;

    public EndNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "end";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("End");
        return self;
    }

}
