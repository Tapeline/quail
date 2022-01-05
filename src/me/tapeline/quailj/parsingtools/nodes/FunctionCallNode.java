package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class FunctionCallNode extends Node {

    public final Node id;
    public final Node args;

    public FunctionCallNode(Node id, Node args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public String toString() {
        return id.toString() + (args instanceof MultiElementNode? args.toString() : "(" +
                args.toString() + ")");
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Function Call");
        self.add(id.toTree());
        self.add(args.toTree());
        return self;
    }

}
