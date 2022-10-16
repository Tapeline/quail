package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class FieldSetNode extends Node {

    public Node object;
    public String field;
    public Node value;

    public FieldSetNode(Token token, Node object, String field, Node value) {
        super(token);
        this.object = object;
        this.field = field;
        this.value = value;
    }
}
