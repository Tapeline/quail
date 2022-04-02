package me.tapeline.quailj.parser.nodes;

import java.util.ArrayList;
import java.util.List;

public class IfBlockNode extends Node {

    public final BinaryOperatorNode condition;
    public final BlockNode nodes;
    public List<Node> linkedNodes = new ArrayList<>();

    public IfBlockNode(BinaryOperatorNode condition, BlockNode block) {
        this.condition = condition;
        this.nodes = block;
        this.codePos = condition.codePos;
    }

    public void linkNode(Node node) {
        linkedNodes.add(node);
    }

    @Override
    public String toString() {
        return "if(" + condition.toString() + ")" + nodes.toString() + "\n" +
                "Linked: " + linkedNodes.toString() + "\n";
    }
}
