package me.tapeline.quailj.parsing.nodes.modifiers;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class AsyncFlagNode extends Node {

    public Node node;

    public AsyncFlagNode(Token token, Node node) {
        super(token);
        this.node = node;
    }

}
