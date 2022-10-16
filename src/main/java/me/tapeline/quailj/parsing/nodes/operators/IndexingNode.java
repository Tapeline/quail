package me.tapeline.quailj.parsing.nodes.operators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class IndexingNode extends Node {

    public Node object;
    public Node index;

    public IndexingNode(Token token, Node object, Node index) {
        super(token);
        this.object = object;
        this.index = index;
    }

}
