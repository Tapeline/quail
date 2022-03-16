package me.tapeline.quailj.parser.nodes;

public class EveryBlockNode implements Node {

    public int codePos = 0;
    public final VariableNode variable;
    public final Node expr;
    public final BlockNode nodes;

    public EveryBlockNode(Node expr, VariableNode var, BlockNode block, int pos) {
        this.expr = expr;
        this.nodes = block;
        this.variable = var;
        this.codePos = pos;
    }

    @Override
    public String toString() {
        return "every(\n" + variable.toString() + " : " + expr.toString() + ")"+ nodes.toString() + "\n";
    }
}
