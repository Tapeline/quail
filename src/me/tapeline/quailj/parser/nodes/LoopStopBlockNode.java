package me.tapeline.quailj.parser.nodes;

public class LoopStopBlockNode implements Node {

    public int codePos = 0;
    public final BinaryOperatorNode condition;
    public final BlockNode nodes;

    public LoopStopBlockNode(BinaryOperatorNode condition, BlockNode block, int pos) {
        this.condition = condition;
        this.nodes = block;
        this.codePos = pos;
    }

    @Override
    public String toString() {
        return "loop" + nodes.toString() + "stop when" + condition.toString() + "\n";
    }
}
