package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class ObjectFieldReferenceNode extends Node {

    public final Node lnode;
    public final Node rnode;
    public final Token operator;

    public ObjectFieldReferenceNode(Token op, Node lNode, Node rNode) {
        this.operator = op;
        this.lnode = lNode;
        this.rnode = rNode;
    }

    @Override
    public String toString() {
        return "Field-" + rnode.toString() + ", reference from=" + lnode.toString();
    }

}
