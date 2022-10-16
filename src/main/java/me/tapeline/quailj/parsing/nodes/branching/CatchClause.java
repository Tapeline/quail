package me.tapeline.quailj.parsing.nodes.branching;

import me.tapeline.quailj.parsing.nodes.Node;

public class CatchClause {

    public Node exceptionClass;
    public String variable;
    public Node code;

    public CatchClause(Node exceptionClass, String variable, Node code) {
        this.exceptionClass = exceptionClass;
        this.variable = variable;
        this.code = code;
    }

}
