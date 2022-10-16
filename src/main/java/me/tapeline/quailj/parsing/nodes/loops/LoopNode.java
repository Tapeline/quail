package me.tapeline.quailj.parsing.nodes.loops;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class LoopNode extends Node {

    public Node condition;
    public Node code;

    public LoopNode(Token token, Node condition, Node code) {
        super(token);
        this.condition = condition;
        this.code = code;
    }

}
