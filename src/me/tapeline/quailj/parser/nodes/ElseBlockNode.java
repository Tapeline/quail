package me.tapeline.quailj.parser.nodes;

public class ElseBlockNode implements Node {

    public final BlockNode nodes;
    public int codePos = 0;

    public ElseBlockNode(BlockNode block) {
        this.nodes = block;
        this.codePos = block.codePos;
    }

    @Override
    public String toString() {
        return "else\n" + nodes.toString() + "\n";
    }
}
