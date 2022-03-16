package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class ThroughBlockNode implements Node {

    public int codePos = 0;
    public final VariableNode variable;
    public final BinaryOperatorNode range;
    public final BlockNode nodes;

    public ThroughBlockNode(BinaryOperatorNode range, VariableNode var, BlockNode block, int pos) {
        this.range = range;
        this.nodes = block;
        this.variable = var;
        this.codePos = pos;
    }


    @Override
    public String toString() {
        return "through " + range.toString() + " as " + variable.toString() + nodes.toString() + "\n";
    }
}
