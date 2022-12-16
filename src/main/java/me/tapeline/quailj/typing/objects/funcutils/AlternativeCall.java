package me.tapeline.quailj.typing.objects.funcutils;

import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class AlternativeCall {

    public List<FuncArgument> args;
    public Node code;

    public AlternativeCall(List<FuncArgument> args, Node code) {
        this.args = args;
        this.code = code;
    }

}
