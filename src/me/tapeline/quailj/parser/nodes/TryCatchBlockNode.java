package me.tapeline.quailj.parser.nodes;

public class TryCatchBlockNode extends Node {

    public VariableNode variable;
    public BlockNode tryNodes;
    public BlockNode catchNodes;

    public TryCatchBlockNode(BlockNode tn, BlockNode cn, VariableNode var, int pos) {
        this.catchNodes = cn;
        this.tryNodes = tn;
        this.variable = var;
        this.codePos = pos;
    }

    @Override
    public String toString() {
        return "try" + tryNodes + "catch  " + variable  + catchNodes + "\n";
    }
}
