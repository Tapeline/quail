package me.tapeline.quailj.parser.nodes;

public class EventNode extends Node {

    public final FunctionCallNode event;
    public final Node code;

    public EventNode(FunctionCallNode event, Node code, int pos) {
        this.event = event;
        this.code = code;
        this.codePos = pos;
    }

    @Override
    public String toString() {
        return "on " + event.toString() + code.toString();
    }
}
