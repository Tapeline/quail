package me.tapeline.quailj.parsing.nodes.loops;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class ThroughNode extends Node {

    public Node start;
    public Node end;
    public Node step;
    public String iterator;
    public Node code;

    public ThroughNode(Token token, Node start, Node end, Node step, String iterator, Node code) {
        super(token);
        this.start = start;
        this.end = end;
        this.step = step;
        this.iterator = iterator;
        this.code = code;
    }

}
