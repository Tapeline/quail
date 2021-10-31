package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class UnaryOperatorNode extends Node {

    public final Token operator;
    public final Node operand;
    public final Token token;

    public UnaryOperatorNode(Token op, Node opnd) {
        this.operator = op;
        this.operand = opnd;
        this.token = op;
    }

    @Override
    public String toString() {
        return "UnaryOp-" + operator.toString() + "[" + operand.toString() + "]";
    }

}
