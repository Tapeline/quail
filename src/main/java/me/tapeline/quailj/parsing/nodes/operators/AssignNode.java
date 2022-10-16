package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class AssignNode extends Node {

    public String variable;
    public Node value;

    public AssignNode(Token token, String variable, Node value) {
        super(token);
        this.variable = variable;
        this.value = value;
    }

}
