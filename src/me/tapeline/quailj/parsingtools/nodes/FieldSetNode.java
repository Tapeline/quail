package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class FieldSetNode extends Node {

    public final Node lnode;
    public final Node rnode;
    public final Node value;

    public FieldSetNode(Node lNode, Node rNode, Node value) {
        this.value = value;
        this.lnode = lNode;
        this.rnode = rNode;
    }

    @Override
    public String toString() {
        return lnode.toString() + "." + rnode.toString() + "=" + value.toString();
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Field Set");
        self.add(lnode.toTree());
        self.add(rnode.toTree());
        self.add(new DefaultMutableTreeNode("=>"));
        self.add(value.toTree());
        return self;
    }

}
