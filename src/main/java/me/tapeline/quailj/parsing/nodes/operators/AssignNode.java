package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;

public class AssignNode extends Node {

    public String variable;
    public Node value;
    public VariableNode variableNode;

    public AssignNode(Token token, String variable, Node value) {
        super(token);
        this.variable = variable;
        this.value = value;
    }

    public AssignNode(Token token, VariableNode node, String variable, Node value) {
        super(token);
        this.variable = variable;
        this.value = value;
        this.variableNode = node;
    }

}
