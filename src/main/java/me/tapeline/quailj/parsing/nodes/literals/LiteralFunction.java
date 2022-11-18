package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.block.BlockNode;
import me.tapeline.quailj.parsing.nodes.sequence.TupleNode;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;

import java.util.List;

public class LiteralFunction extends Node {

    public String name;
    public List<FuncArgument> args;
    public Node code;
    public boolean isStatic;

    public LiteralFunction(Token token, String name, List<FuncArgument> args, Node code, boolean isStatic) {
        super(token);
        this.name = name;
        this.args = args;
        this.code = code;
        this.isStatic = isStatic;
    }

}