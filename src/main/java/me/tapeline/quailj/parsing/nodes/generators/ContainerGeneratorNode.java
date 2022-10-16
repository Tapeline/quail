package me.tapeline.quailj.parsing.nodes.generators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class ContainerGeneratorNode extends Node {

    public List<String> iterators;
    public Node iterable;
    public Node condition;
    public Node fallback;
    public Node key;
    public Node value;

    public ContainerGeneratorNode(Token token, Node key, Node value, List<String> iterators, Node iterable, Node condition, Node fallback) {
        super(token);
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = condition;
        this.fallback = fallback;
        this.key = key;
        this.value = value;
    }

    public ContainerGeneratorNode(Token token, Node key, Node value, List<String> iterators, Node iterable) {
        super(token);
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = null;
        this.fallback = null;
        this.key = key;
        this.value = value;
    }

    public ContainerGeneratorNode(Token token, Node key, Node value, List<String> iterators, Node iterable, Node condition) {
        super(token);
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = condition;
        this.fallback = null;
        this.key = key;
        this.value = value;
    }

}
