package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

public class FieldSetNode implements Node {

    public final Node lnode;
    public final Node rnode;
    public final Token operator;
    public final Node value;
    public int codePos = 0;

    public FieldSetNode(Token op, Node lNode, Node rNode, Node value) {
        this.operator = op;
        this.lnode = lNode;
        this.rnode = rNode;
        this.codePos = op.p;
        this.value = value;
    }


    @Override
    public String toString() {
        return lnode.toString() + "->" + rnode.toString();
    }
}
