package me.tapeline.quarkj.parsingtools.nodes;

public class ElseIfBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;

    public ElseIfBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
    }

    @Override
    public String toString() {
        return "ElseIfNode?" + condition.toString() + "=" + nodes.toString() + "\n";
    }

}
