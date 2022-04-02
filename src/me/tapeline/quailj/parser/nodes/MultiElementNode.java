package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class MultiElementNode extends Node {

    public final List<Node> nodes = new ArrayList<>();

    public MultiElementNode(Token op) {
        this.codePos = op.p;
    }

    public void addNode(Node n) {
        if(n != null) nodes.add(n);
    }

    @Override
    public String toString() {
        return "Multi" + nodes.toString();
    }
}
