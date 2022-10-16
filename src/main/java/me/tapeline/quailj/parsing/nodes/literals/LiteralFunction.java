package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.block.BlockNode;
import me.tapeline.quailj.parsing.nodes.sequence.TupleNode;

public class LiteralFunction extends Node {

    public String name;
    public TupleNode args;
    public Node code;
    public boolean isStatic;

    public LiteralFunction(Token token, String name, TupleNode args, Node code, boolean isStatic) {
        super(token);
        this.name = name;
        this.args = args;
        this.code = code;
        this.isStatic = isStatic;
    }

}
