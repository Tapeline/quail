package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.HashMap;
import java.util.List;

public class CallNode extends Node {

    public List<Node> arguments;
    public Node function;
    public HashMap<String, Node> keywordArguments;

    public CallNode(Token token, List<Node> arguments, Node function, HashMap<String, Node> keywordArguments) {
        super(token);
        this.arguments = arguments;
        this.function = function;
        this.keywordArguments = keywordArguments;
    }

}
