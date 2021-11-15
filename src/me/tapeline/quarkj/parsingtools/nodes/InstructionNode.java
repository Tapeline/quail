package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class InstructionNode extends Node {

    public final Token operator;

    public InstructionNode(Token op) {
        this.operator = op;
    }

    @Override
    public String toString() {
        return operator.toString();
    }

    public String srepr() {
        return "Instruction-" + operator.toString();
    }

}
