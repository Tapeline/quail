package me.tapeline.quarkj.language.types;

import me.tapeline.quarkj.parsingtools.nodes.BlockNode;

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

    @Override
    public String toString() {
        return "FuncType " + name;
    }

}
