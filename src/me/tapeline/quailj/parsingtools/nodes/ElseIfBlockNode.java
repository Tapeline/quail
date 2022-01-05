package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class ElseIfBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;

    public ElseIfBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
    }

    @Override
    public String toString() {
        return "ElseIfNode?" + condition.toString() + "=" + nodes.toString() + "\n";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("ElseIf");
        self.add(condition.toTree());
        self.add(nodes.toTree());
        return self;
    }

}
