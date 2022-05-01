package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

public class EffectNode extends Node {

    public final Token operator;
    public final Node operand;

    public EffectNode(Token op, Node opnd) {
        this.operator = op;
        this.operand = opnd;
        this.codePos = op.p;
    }

    @Override
    public String toString() {
        return operator.toString() + " " + operand.toString();
    }

}
