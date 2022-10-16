package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class LiteralList extends Node {

    public List<Node> value;

    public LiteralList(Token token, List<Node> value) {
        super(token);
        this.value = value;
    }

}
