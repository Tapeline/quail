package me.tapeline.quailj.language.types;

import me.tapeline.quailj.interpretertools.Runtime;
import me.tapeline.quailj.parsingtools.nodes.BlockNode;

import java.util.ArrayList;
import java.util.List;

public class FuncType extends QType {

    public String name;
    public List<String> args = new ArrayList<>();
    public BlockNode code;

    public FuncType(String name, List<String> args, BlockNode code) {
        this.code = code;
        this.args = args;
        this.name = name;
    }

    public QType run(Runtime runtime, List<QType> args) {
        return new VoidType();
    }

    @Override
    public String toString() {
        return "FuncType " + name;
    }

}
