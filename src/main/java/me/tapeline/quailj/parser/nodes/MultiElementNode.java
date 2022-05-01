package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class MultiElementNode extends Node {

    public List<Node> nodes = new ArrayList<>();

    public MultiElementNode(Token op) {
        this.codePos = op.p;
    }

    public MultiElementNode(List<Node> n, int pos) {
        this.codePos = pos;
        this.nodes.addAll(n);
    }

    public void addNode(Node n) {
        if(n != null) nodes.add(n);
    }

    @Override
    public String toString() {
        String n = nodes.toString();
        return "(" + n.substring(1, n.length() - 1) + ")";
    }
}
