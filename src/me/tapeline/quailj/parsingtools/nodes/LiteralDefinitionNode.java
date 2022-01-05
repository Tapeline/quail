package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class LiteralDefinitionNode extends Node {

    public final Token variable;
    public final Token type;

    public LiteralDefinitionNode(Token token, Token type) {
        this.variable = token;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Definition:" + type + " " + variable;
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return new DefaultMutableTreeNode("Pending Definition " + type.c + " " + variable.c);
    }

}
