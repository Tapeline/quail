package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.tree.DefaultMutableTreeNode;

public class BinaryOperatorNode extends Node {

    public final Node lnode;
    public final Node rnode;
    public final Token operator;

    public BinaryOperatorNode(Token op, Node lNode, Node rNode) {
        this.operator = op;
        this.lnode = lNode;
        this.rnode = rNode;
    }

    @Override
    public String toString() {
        return lnode.toString() + " " + operator.toString() + " " + rnode.toString();
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Binary Operator " + operator.c);
        self.add(lnode.toTree());
        self.add(rnode.toTree());
        return self;
    }
}
