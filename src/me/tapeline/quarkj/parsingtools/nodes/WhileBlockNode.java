package me.tapeline.quarkj.parsingtools.nodes;

public class WhileBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;

    public WhileBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
    }


    @Override
    public String toString() {
        return "WhileNode-" + condition.toString() + "=" + nodes.toString() + "\n";
    }

}
