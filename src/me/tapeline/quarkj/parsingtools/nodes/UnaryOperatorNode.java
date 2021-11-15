package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class UnaryOperatorNode extends Node {

    public final Token operator;
    public final Node operand;

    public UnaryOperatorNode(Token op, Node opnd) {
        this.operator = op;
        this.operand = opnd;
    }

    @Override
    public String toString() {
        return operator.toString() + " " + operand.toString();
    }

    public String srepr() {
        return "UnaryOp-" + operator.toString() + "[" + operand.toString() + "]";
    }

}
