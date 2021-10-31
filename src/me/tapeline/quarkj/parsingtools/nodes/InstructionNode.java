package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;
import me.tapeline.quarkj.tokenizetools.tokens.TokenType;

public class InstructionNode extends Node {

    public final Token operator;
    public final Token token;

    public InstructionNode(Token op) {
        this.operator = op;
        this.token = op;
    }

    @Override
    public String toString() {
        return "Instruction-" + operator.toString();
    }

}
