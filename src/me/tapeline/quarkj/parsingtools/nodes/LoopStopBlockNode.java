package me.tapeline.quarkj.parsingtools.nodes;

public class LoopStopBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;

    public LoopStopBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
    }


    @Override
    public String toString() {
        return "loop " + nodes.toString() + " stop when " + condition.toString();
    }

    public String srepr() {
        return "LoopNode-" + condition.toString() + "=" + nodes.toString() + "\n";
    }

}
