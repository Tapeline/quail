package me.tapeline.quailj.parsingtools.nodes;

import me.tapeline.quailj.tokenizetools.tokens.Token;
import me.tapeline.quailj.tokenizetools.tokens.TokenType;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class LiteralContainerNode extends Node {

    public List<Node> initialize = new ArrayList<>();
    public boolean isMeta = false;
    public String name;
    public LiteralFunctionNode builder = new LiteralFunctionNode(new Token(TokenType.ID,
            "no-builder", 0), new MultiElementNode(), new BlockNode());
    public String alike = "container";

    public LiteralContainerNode(String name, boolean im, String alike) {
        this.name = name;
        this.isMeta = im;
        this.alike = alike;
    }

    @Override
    public String toString() {
        return (isMeta? "metacontainerNode" : "containerNode") + initialize.toString();
    }

    @Override
    public DefaultMutableTreeNode toTree() {
        DefaultMutableTreeNode self = new DefaultMutableTreeNode((isMeta? "Metacontainer " : "Container ") + name);
        DefaultMutableTreeNode bldr = new DefaultMutableTreeNode("Object Builder");
        bldr.add(builder.toTree());
        DefaultMutableTreeNode pend = new DefaultMutableTreeNode("Pending initialization");
        for (Node n : initialize)
            pend.add(n.toTree());
        self.add(bldr);
        self.add(pend);
        return self;
    }
}
