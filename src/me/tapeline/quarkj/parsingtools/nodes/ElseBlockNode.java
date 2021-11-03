package me.tapeline.quarkj.parsingtools.nodes;

public class ElseBlockNode extends Node {

    public final BlockNode nodes;

    public ElseBlockNode(BlockNode block) {
        this.nodes = block;
    }

    @Override
    public String toString() {
        return "ElseNode=" + nodes.toString() + "\n";
    }

}
