package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;
import me.tapeline.quarkj.tokenizetools.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

public class IfBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;
    public List<Node> linkedNodes = new ArrayList<>();
    public final Token token;

    public IfBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
        this.token = condition.token;
    }

    public void linkNode(Node node) {
        linkedNodes.add(node);
    }

    @Override
    public String toString() {
        return "IfNode?" + condition.toString() + "=" + nodes.toString() + "\n" +
                "LinkedNodes{" + linkedNodes.toString() + "}\n";
    }

}
