package me.tapeline.quailj.debugtools;

import me.tapeline.quailj.parsingtools.nodes.Node;
import me.tapeline.quailj.tokenizetools.tokens.Token;

import java.util.List;

public class DebugGUIManager {

    private Node tree;
    private List<Token> tokens;

    public void setNodeTree(Node node) {
        this.tree = node;
    }

    public void setTokenList(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void displayTree() {
        NodeTreeFrame nodeTreeFrame = new NodeTreeFrame(this.tree);
    }

    public void displayTokens() {
        TokenListFrame tokenListFrame = new TokenListFrame(this.tokens);
    }

}
