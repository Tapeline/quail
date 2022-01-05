package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class InstructionNode extends Node {

    public final Token operator;

    public InstructionNode(Token op) {
        this.operator = op;
    }

    @Override
    public String toString() {
        return operator.toString();
    }

    public String srepr() {
        return "Instruction-" + operator.toString();
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return new DefaultMutableTreeNode("Instruction " + operator.c);
    }

}
