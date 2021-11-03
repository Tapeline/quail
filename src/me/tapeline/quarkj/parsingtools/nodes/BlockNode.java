package me.tapeline.quarkj.parsingtools.nodes;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends Node {

    public List<Node> nodes = new ArrayList<>();

    public void addNode(Node n) {
        if(n != null) nodes.add(n);
    }

    public int getLastIfNodeIndex() {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            if (nodes.get(i) instanceof IfBlockNode)
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String s = "Code[";
        for (Node n : nodes) {
            s += ("\n" + n.toString());
        }
        s += "]";
        return s;
    }
}
