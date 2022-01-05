package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class LiteralListNode extends Node {

    public List<Node> nodes = new ArrayList<>();

    public void addNode(Node n) {
        if(n != null) nodes.add(n);
    }

    @Override
    public String toString() {
        String s = "List[";
        for (Node n : nodes) {
            s += (n.toString() + ',');
        }
        s += "]";
        return s;
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("List");
        for (Node n : nodes)
            self.add(n.toTree());
        return self;
    }
}
