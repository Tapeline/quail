package me.tapeline.quarkj.parsingtools.nodes;

public class EveryBlockNode extends Node {

    public final VariableNode variable;
    public final Node expr;
    public final BlockNode nodes;

    public EveryBlockNode(Node expr, VariableNode var, BlockNode block) {
        this.expr = expr;
        this.nodes = block;
        this.variable = var;
    }

    @Override
    public String toString() {
        return "every " +  variable.toString() + " in " + expr.toString() + " do " + nodes.toString() + " end";
    }

}
