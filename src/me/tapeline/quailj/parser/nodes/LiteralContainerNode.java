package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class LiteralContainerNode extends Node {

    public List<Node> initialize = new ArrayList<>();
    public boolean isMeta = false;
    public String name;
    public String alike = "container";

    public LiteralContainerNode(Token name, boolean im, String alike) {
        this.name = name.c;
        this.codePos = name.p;
        this.isMeta = im;
        this.alike = alike;
    }

    @Override
    public String toString() {
        return (isMeta? "metacontainer " : "container ")
                + name + "like" + alike + "\n"
                + initialize.toString() + "\n";
    }
}
