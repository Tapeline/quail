package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class EveryBlockNode extends Node {

    public final Node variable;
    public final Node expr;
    public final BlockNode nodes;

    public EveryBlockNode(Node expr, Node var, BlockNode block) {
        this.expr = expr;
        this.nodes = block;
        this.variable = var;
    }

    @Override
    public String toString() {
        return "every " +  variable.toString() + " in " + expr.toString() + " do " + nodes.toString() + " end";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Every");
        self.add(variable.toTree());
        self.add(new DefaultMutableTreeNode("in"));
        self.add(expr.toTree());
        self.add(nodes.toTree());
        return self;
    }

}
