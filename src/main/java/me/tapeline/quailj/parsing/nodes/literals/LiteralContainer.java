package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.typing.utils.ContainerPreRuntimeContents;

public class LiteralContainer extends Node {

    public ContainerPreRuntimeContents contents;
    public boolean isMeta = false;
    public String name;
    public String like = "container";

    public LiteralContainer(Token token, ContainerPreRuntimeContents contents,
                            boolean isMeta, String name, String like) {
        super(token);
        this.contents = contents;
        this.isMeta = isMeta;
        this.name = name;
        this.like = like;
    }

    public LiteralContainer(Token token, ContainerPreRuntimeContents contents) {
        super(token);
        this.contents = contents;
        this.isMeta = false;
        this.name = "_container";
        this.like = "Object";
    }
}
