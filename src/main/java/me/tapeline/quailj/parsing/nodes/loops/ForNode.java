package me.tapeline.quailj.parsing.nodes.loops;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class ForNode extends Node {

    public Node iterable;
    public List<String> iterator;
    public Node code;

    public ForNode(Token token, Node iterable, List<String> iterator, Node code) {
        super(token);
        this.iterable = iterable;
        this.iterator = iterator;
        this.code = code;
    }

}
