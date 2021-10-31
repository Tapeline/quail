package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;
import me.tapeline.quarkj.tokenizetools.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

public class RootNode extends Node {

    public List<Node> nodes = new ArrayList<>();
    public final Token token = new Token(TokenType.WHITESPACE, "", 0);

    public void addNode(Node n) {
        nodes.add(n);
    }

    public int getLastIfNodeIndex() {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            if (nodes.get(i) instanceof IfBlockNode)
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String s = "Code[";
        for (Node n : nodes) {
            s += ("\n" + n.toString());
        }
        s += "]";
        return s;
    }
}
