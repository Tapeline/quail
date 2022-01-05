package me.tapeline.quailj.parsingtools.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

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

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode("Try-Catch");
        DefaultMutableTreeNode t = new DefaultMutableTreeNode("Try:");
        t.add(tryNodes.toTree());
        DefaultMutableTreeNode c = new DefaultMutableTreeNode("Catch:");
        c.add(variable.toTree());
        c.add(tryNodes.toTree());
        self.add(t);
        self.add(c);
        return self;
    }
}
