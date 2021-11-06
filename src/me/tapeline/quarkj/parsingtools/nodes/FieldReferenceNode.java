package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class FieldReferenceNode extends Node {

    public final Node lnode;
    public final Node rnode;
    public final Token operator;

    public FieldReferenceNode(Token op, Node lNode, Node rNode) {
        this.operator = op;
        this.lnode = lNode;
        this.rnode = rNode;
    }

    @Override
    public String toString() {
        return "Field[left=" + lnode.toString() + ", right=" + rnode.toString() + "]";
    }

}
