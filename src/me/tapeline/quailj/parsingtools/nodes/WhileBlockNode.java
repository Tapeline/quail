package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class WhileBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;

    public WhileBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
    }


    @Override
    public String toString() {
        return "while (" + condition.toString() + ") do" + nodes.toString() + "end";
    }

    public String sRepresentation() {
        return "WhileNode-" + condition.toString() + "=" + nodes.toString() + "\n";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("While");
        self.add(condition.toTree());
        self.add(nodes.toTree());
        return self;
    }

}
