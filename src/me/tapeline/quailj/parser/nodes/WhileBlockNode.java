package me.tapeline.quailj.parser.nodes;

public class WhileBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;

    public WhileBlockNode(BinaryOperatorNode condition, BlockNode block, int pos) {
        this.condition = condition;
        this.nodes = block;
        this.codePos = pos;
    }

    @Override
    public String toString() {
        return "while (" + condition.toString() + ")" + nodes.toString() + "\n";
    }
}
