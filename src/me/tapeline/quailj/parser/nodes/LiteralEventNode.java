package me.tapeline.quailj.parser.nodes;

public class LiteralEventNode implements Node {

    public final Node name;
    public final BlockNode code;
    public final Node params;
    public int codePos = 0;

    public LiteralEventNode(Node name, Node params, BlockNode code, int pos) {
        this.name = name;
        this.params = params;
        this.code = code;
    }

    @Override
    public String toString() {
        return "event " + name.toString() + " listen" + params.toString() + code.toString();
    }
}
