package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class FunctionCallNode extends Node {

    public final Token operator;
    public final Node operand;

    public FunctionCallNode(Token op, Node opnd) {
        this.operator = op;
        this.operand = opnd;
    }

    @Override
    public String toString() {
        return operator.toString() + (operand instanceof MultiElementNode? operand.toString() : "(" +
                operand.toString() + ")");
    }

}
