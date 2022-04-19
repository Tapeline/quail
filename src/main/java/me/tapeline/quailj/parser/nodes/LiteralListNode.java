package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class LiteralListNode extends Node {

    public List<Node> nodes = new ArrayList<>();

    public LiteralListNode(Token token) {
        this.codePos = token.p;
    }

    public void addNode(Node n) {
        if(n != null) nodes.add(n);
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
