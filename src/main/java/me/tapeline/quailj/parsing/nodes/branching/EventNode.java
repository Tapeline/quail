package me.tapeline.quailj.parsing.nodes.branching;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class EventNode extends Node {

    public Node event;
    public String id;
    public Node code;
    public String funcName;

    public EventNode(Token token, Node event, String id, String funcName, Node code) {
        super(token);
        this.event = event;
        this.id = id;
        this.code = code;
        this.funcName = funcName;
    }

}
