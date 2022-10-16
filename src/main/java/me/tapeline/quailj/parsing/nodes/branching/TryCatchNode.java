package me.tapeline.quailj.parsing.nodes.branching;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class TryCatchNode extends Node {

    public Node tryCode;
    public List<CatchClause> catchClauses;

    public TryCatchNode(Token token, Node tryCode, List<CatchClause> catchClauses) {
        super(token);
        this.tryCode = tryCode;
        this.catchClauses = catchClauses;
    }

}
