package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class FieldReferenceNode extends Node {

    public Node object;
    public String field;

    public FieldReferenceNode(Token token, Node object, String field) {
        super(token);
        this.object = object;
        this.field = field;
    }

}
