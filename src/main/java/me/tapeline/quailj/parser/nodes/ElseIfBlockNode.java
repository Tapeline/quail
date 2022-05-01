package me.tapeline.quailj.parser.nodes;

public class ElseIfBlockNode extends Node {

    public BinaryOperatorNode condition;
    public BlockNode nodes;

    public ElseIfBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
        this.codePos = condition.codePos;
    }

    @Override
    public String toString() {
        return "elseif(\n" + condition.toString() + ")"+ nodes.toString() + "\n";
    }
}
