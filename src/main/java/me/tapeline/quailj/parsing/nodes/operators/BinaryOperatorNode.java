package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

public class BinaryOperatorNode extends Node {

    public TokenType operation;
    public Node left;
    public Node right;

    public BinaryOperatorNode(Token token, TokenType operation, Node left, Node right) {
        super(token);
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

}
