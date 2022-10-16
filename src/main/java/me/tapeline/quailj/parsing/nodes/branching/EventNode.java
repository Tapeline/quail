package me.tapeline.quailj.parsing.nodes.branching;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class EventNode extends Node {

    public Node event;
    public String id;

    public EventNode(Token token, Node event, String id) {
        super(token);
        this.event = event;
        this.id = id;
    }

}
