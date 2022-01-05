package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class GroupNode extends Node {

    public final Node node;

    public GroupNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return node.toString();
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        return node.toTree();
    }

}
