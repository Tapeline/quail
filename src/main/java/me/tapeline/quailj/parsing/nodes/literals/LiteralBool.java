package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class LiteralBool extends Node {

    public boolean value;

    public LiteralBool(Token token, boolean value) {
        super(token);
        this.value = value;
    }

}
