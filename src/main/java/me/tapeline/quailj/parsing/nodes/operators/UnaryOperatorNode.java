package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

public class UnaryOperatorNode extends Node {

    public TokenType operation;
    public Node operand;

    public UnaryOperatorNode(Token token, TokenType operation, Node operand) {
        super(token);
        this.operation = operation;
        this.operand = operand;
    }

}
