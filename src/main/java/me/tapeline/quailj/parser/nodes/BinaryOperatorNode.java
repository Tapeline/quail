package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

public class BinaryOperatorNode extends Node {

    public final Node lnode;
    public final Node rnode;
    public final Token operator;

    public BinaryOperatorNode(Token op, Node lNode, Node rNode) {
        this.operator = op;
        this.lnode = lNode;
        this.rnode = rNode;
        this.codePos = op.p;
    }

    @Override
    public String toString() {
        return lnode.toString() + " " + operator.toString() + " " + rnode.toString();
    }

}
