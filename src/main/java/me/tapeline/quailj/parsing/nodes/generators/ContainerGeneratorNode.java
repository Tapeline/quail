package me.tapeline.quailj.parsing.nodes.generators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class ContainerGeneratorNode extends Node {

    public Node key;
    public Node value;
    public List<String> iterators;
    public Node iterable;
    public Node condition;
    public Node fallbackKey;
    public Node fallbackValue;

    public ContainerGeneratorNode(Token token, Node key, Node value,
                                  List<String> iterators, Node iterable,
                                  Node condition, Node fallbackKey, Node fallbackValue) {
        super(token);
        this.key = key;
        this.value = value;
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = condition;
        this.fallbackKey = fallbackKey;
        this.fallbackValue = fallbackValue;
    }
}
