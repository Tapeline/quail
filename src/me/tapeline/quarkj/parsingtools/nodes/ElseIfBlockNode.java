package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;
import me.tapeline.quarkj.tokenizetools.tokens.TokenType;

public class ElseIfBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;
    public final Token token;

    public ElseIfBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
        this.token = condition.token;
    }

    @Override
    public String toString() {
        return "ElseIfNode?" + condition.toString() + "=" + nodes.toString() + "\n";
    }

}
