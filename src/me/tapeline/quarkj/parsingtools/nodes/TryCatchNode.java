package me.tapeline.quarkj.parsingtools.nodes;

public class TryCatchNode extends Node {

    public VariableNode variable;
    public BlockNode tryNodes;
    public BlockNode catchNodes;

    public TryCatchNode(BlockNode tn, BlockNode cn, VariableNode var) {
        this.catchNodes = cn;
        this.tryNodes = tn;
        this.variable = var;
    }

    @Override
    public String toString() {
        return "try do " + tryNodes + " end catch as " + variable + " do " + catchNodes;
    }

    public String srepr() {
        return "Try=" + tryNodes + ",Catch=" + catchNodes;
    }
}
