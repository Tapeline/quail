package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class ThroughBlockNode extends Node {

    public final Node variable;
    public final BinaryOperatorNode range;
    public final BlockNode nodes;

    public ThroughBlockNode(BinaryOperatorNode range, Node var, BlockNode block) {
        this.range = range;
        this.nodes = block;
        this.variable = var;
    }


    @Override
    public String toString() {
        return "through " + range.toString() + " as " + variable.toString() + " do " + nodes.toString() + " end";
    }

    public String srepr() {
        return "ThroughNode-" + range.toString() + ":" + variable.toString() + "=" + nodes.toString() + "\n";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Through");
        self.add(range.toTree());
        self.add(variable.toTree());
        self.add(nodes.toTree());
        return self;
    }

}
