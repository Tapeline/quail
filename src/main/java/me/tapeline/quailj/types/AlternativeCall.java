package me.tapeline.quailj.types;

import me.tapeline.quailj.parser.nodes.BlockNode;
import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.parser.nodes.VariableNode;

import java.util.ArrayList;
import java.util.List;

public class AlternativeCall {

    public List<VariableNode> arguments = new ArrayList<>();
    public Node code;

    public AlternativeCall(List<VariableNode> args, Node code) {
        this.arguments = args;
        this.code = code;
    }

}
