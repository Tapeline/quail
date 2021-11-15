package me.tapeline.quarkj.parsingtools.nodes;

public class ThroughBlockNode extends Node {

    public final VariableNode variable;
    public final BinaryOperatorNode range;
    public final BlockNode nodes;

    public ThroughBlockNode(BinaryOperatorNode range, VariableNode var, BlockNode block) {
        this.range = range;
        this.nodes = block;
        this.variable = var;
    }


    @Override
    public String toString() {
        return "through " + range.toString() + " as " + variable.toString() + " do " + nodes.toString() + " end";
    }

    public String srepr() {
        return "ThroughNode-" + range.toString() + ":" + variable.toString() + "=" + nodes.toString() + "\n";
    }

}
