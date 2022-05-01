package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

public class EffectNode extends Node {

    public final Token operator;
    public final Node operand;
    public final String other;

    public EffectNode(Token op, Node opnd) {
        this.operator = op;
        this.operand = opnd;
        this.codePos = op.p;
        this.other = "_defaultname";
    }

    public EffectNode(Token op, Node opnd, String other) {
        this.operator = op;
        this.operand = opnd;
        this.codePos = op.p;
        this.other = other;
    }

    @Override
    public String toString() {
        return operator.toString() + " " + operand.toString();
    }

}
