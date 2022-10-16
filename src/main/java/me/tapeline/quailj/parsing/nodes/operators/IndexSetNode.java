package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class IndexSetNode extends Node {

    public Node object;
    public Node index;
    public Node value;

    public IndexSetNode(Token token, Node object, Node index, Node value) {
        super(token);
        this.object = object;
        this.index = index;
        this.value = value;
    }

}
