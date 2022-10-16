package me.tapeline.quailj.parsing.nodes.sequence;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class TupleNode extends Node {

    public List<Node> nodes;

    public TupleNode(Token token, List<Node> nodes) {
        super(token);
        this.nodes = nodes;
    }

}
