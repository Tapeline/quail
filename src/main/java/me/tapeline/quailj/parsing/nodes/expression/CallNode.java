package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.operators.FieldReferenceNode;

import java.util.HashMap;
import java.util.List;

public class CallNode extends Node {

    public List<Node> arguments;
    public Node function;
    public boolean isFieldCall;
    public Node parentField;
    public String field;

    public CallNode(Token token, List<Node> arguments, Node function) {
        super(token);
        this.arguments = arguments;
        this.function = function;
        if (function instanceof FieldReferenceNode) {
            field = ((FieldReferenceNode) function).field;
            parentField = ((FieldReferenceNode) function).object;
            isFieldCall = true;
        }
    }

}
