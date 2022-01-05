package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class ElseBlockNode extends Node {

    public final BlockNode nodes;

    public ElseBlockNode(BlockNode block) {
        this.nodes = block;
    }

    @Override
    public String toString() {
        return "ElseNode=" + nodes.toString() + "\n";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Else");
        self.add(nodes.toTree());
        return self;
    }

}
