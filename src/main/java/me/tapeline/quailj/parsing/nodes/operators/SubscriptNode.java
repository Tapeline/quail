package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class SubscriptNode extends Node {

    public Node object;
    public Node start;
    public Node end;
    public Node step;

    public SubscriptNode(Token token, Node object, Node start, Node end, Node step) {
        super(token);
        this.object = object;
        this.start = start;
        this.end = end;
        this.step = step;
    }

}
