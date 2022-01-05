package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class IfBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;
    public List<Node> linkedNodes = new ArrayList<>();

    public IfBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
    }

    public void linkNode(Node node) {
        linkedNodes.add(node);
    }

    @Override
    public String toString() {
        return "if (" + condition.toString() + ") do" + nodes.toString() + "\n" +
                "Linked{" + linkedNodes.toString() + "}";
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("If");
        self.add(condition.toTree());
        self.add(nodes.toTree());
        DefaultMutableTreeNode linked = new DefaultMutableTreeNode("Branches");
        for (Node branch : linkedNodes)
            linked.add(branch.toTree());
        self.add(linked);
        return self;
    }

}
