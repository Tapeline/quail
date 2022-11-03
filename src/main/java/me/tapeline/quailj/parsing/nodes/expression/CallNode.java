package me.tapeline.quailj.parsing.nodes.expression;

import com.sun.org.apache.xerces.internal.impl.xs.identity.Field;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.operators.FieldReferenceNode;

import java.util.HashMap;
import java.util.List;

public class CallNode extends Node {

    public List<Node> arguments;
    public Node function;
    public HashMap<String, Node> keywordArguments;
    public boolean isFieldCall;
    public Node parentField;

    public CallNode(Token token, List<Node> arguments, Node function, HashMap<String, Node> keywordArguments) {
        super(token);
        this.arguments = arguments;
        this.function = function;
        this.keywordArguments = keywordArguments;
        if (function instanceof FieldReferenceNode) {
            parentField = ((FieldReferenceNode) function).object;
            isFieldCall = true;
        }
    }

}
