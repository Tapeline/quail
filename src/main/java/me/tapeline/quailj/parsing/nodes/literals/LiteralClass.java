package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;

import java.util.HashMap;
import java.util.List;

public class LiteralClass extends Node {

    public String name;
    public Node like;
    public HashMap<VariableNode, Node> contents;
    public HashMap<String, LiteralFunction> methods;
    public List<Node> initialize;

    public LiteralClass(Token token, String name, Node like,
                        HashMap<VariableNode, Node> contents,
                        HashMap<String, LiteralFunction> methods,
                        List<Node> init) {
        super(token);
        this.name = name;
        this.like = like;
        this.contents = contents;
        this.methods = methods;
        this.initialize = init;
    }
}
