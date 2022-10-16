package me.tapeline.quailj.parsing.nodes.generators;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class ListGeneratorNode extends Node {

    public List<String> iterators;
    public Node iterable;
    public Node condition;
    public Node fallback;
    public Node expression;

    public ListGeneratorNode(Token token, Node expression, List<String> iterators, Node iterable, Node condition, Node fallback) {
        super(token);
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = condition;
        this.fallback = fallback;
        this.expression = expression;
    }

    public ListGeneratorNode(Token token, Node expression, List<String> iterators, Node iterable) {
        super(token);
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = null;
        this.fallback = null;
        this.expression = expression;
    }

    public ListGeneratorNode(Token token, Node expression, List<String> iterators, Node iterable, Node condition) {
        super(token);
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = condition;
        this.fallback = null;
        this.expression = expression;
    }

}
