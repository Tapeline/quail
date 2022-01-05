package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class UnaryOperatorNode extends Node {

    public final Token operator;
    public final Node operand;

    public UnaryOperatorNode(Token op, Node opnd) {
        this.operator = op;
        this.operand = opnd;
    }

    @Override
    public String toString() {
        return operator.toString() + " " + operand.toString();
    }

    public String srepr() {
        return "UnaryOp-" + operator.toString() + "[" + operand.toString() + "]";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Unary Operator " + operator.c);
        self.add(operand.toTree());
        return self;
    }

}
