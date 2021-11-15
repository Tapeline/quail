package me.tapeline.quarkj.parsingtools.nodes;

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
}
