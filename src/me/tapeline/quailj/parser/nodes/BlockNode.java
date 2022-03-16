package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class BlockNode implements Node {

    public final List<Node> nodes = new ArrayList<>();
    public int codePos = 0;

    public BlockNode(int pos) {
        this.codePos = pos;
    }

    public BlockNode(List<Node> l, int pos) {
        this.codePos = pos;
        this.nodes.addAll(l);
    }

    public void addNode(Node n) {
        if(n != null) nodes.add(n);
    }

    @Override
    public String toString() {
        String s = "{\n";
        for (Node n : nodes) {
            s += (n.toString() + "\n");
        }
        s += "}";
        return s;
    }
}
