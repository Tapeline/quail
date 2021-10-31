package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;

public class BinaryOperatorNode extends Node {

    public final Node lnode;
    public final Node rnode;
    public final Token operator;
    public final Token token;

    public BinaryOperatorNode(Token op, Node lNode, Node rNode) {
        this.operator = op;
        this.token = op;
        this.lnode = lNode;
        this.rnode = rNode;
    }

    @Override
    public String toString() {
        return "BinaryOp-" + operator.toString() + "[left=" + lnode.toString() + ", right=" + rnode.toString() + "]";
    }

}
