package me.tapeline.quailj.parsing.nodes.effect;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class UseNode extends Node {

    public String path;
    public String alias;

    public UseNode(Token token, String path, String alias) {
        super(token);
        this.path = path;
        this.alias = alias;
    }

}
