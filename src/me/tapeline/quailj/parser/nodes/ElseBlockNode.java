package me.tapeline.quailj.parser.nodes;

public class ElseBlockNode extends Node {

    public final BlockNode nodes;

    public ElseBlockNode(BlockNode block) {
        this.nodes = block;
        this.codePos = block.codePos;
    }

    @Override
    public String toString() {
        return "else\n" + nodes.toString() + "\n";
    }
}
