package me.tapeline.quailj.parsing.nodes.effect;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class UseNode extends Node {

    public Node library;
    public Node alias;

    public UseNode(Token token, Node library, Node alias) {
        super(token);
        this.library = library;
        this.alias = alias;
    }

}
