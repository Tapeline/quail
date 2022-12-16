package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class RangeNode extends Node {

    public Node start;
    public Node end;
    public Node step;
    public boolean include;

    public RangeNode(Token token, Node start, Node end, Node step, boolean include) {
        super(token);
        this.start = start;
        this.end = end;
        this.step = step;
        this.include = include;
    }

}
