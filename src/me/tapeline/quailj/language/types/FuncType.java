package me.tapeline.quailj.language.types;

import me.tapeline.quailj.interpretertools.Memory;
import me.tapeline.quailj.interpretertools.Runtime;
import me.tapeline.quailj.parsingtools.nodes.BlockNode;

import java.util.ArrayList;
import java.util.List;

public class FuncType extends QType {

    public String name;
    public List<String> args;
    public BlockNode code;
    public boolean restrictMetacalls = false;

    public FuncType(String name, List<String> args, BlockNode code) {
        this.code = code;
        this.args = args;
        this.name = name;
    }
    public FuncType(String name, List<String> args, BlockNode code, boolean r) {
        this.code = code;
        this.args = args;
        this.name = name;
        this.restrictMetacalls = r;
    }

    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Memory mem = new Memory(runtime.scope);
        for (int i = 0; i < Math.min(args.size(), a.size()); i++) {
            mem.set(args.get(i), a.get(i));
        }
        try {
            runtime.run(this.code, mem);
        } catch (RuntimeStriker striker) {
            if (striker.type.equals(RuntimeStrikerTypes.RETURN)) {
                return striker.retVal;
            } else throw striker;
        }
        return new VoidType();
    }

    @Override
    public String toString() {
        return "FuncType " + name;
    }

    public QType metaRun(Runtime runtime, List<QType> a) throws RuntimeStriker {
        List<QType> result = new ArrayList<>();
        Memory mem = new Memory(runtime.scope);
        for (int i = 0; i < Math.min(args.size(), a.size()); i++) {
            mem.set(args.get(i), a.get(i));
        }
        try {
            runtime.run(this.code, mem);
        } catch (RuntimeStriker striker) {
            if (striker.type.equals(RuntimeStrikerTypes.RETURN)) {
                result.add(striker.retVal);
            } else throw striker;
        }
        if (result.size() < 1)
            result.add(new VoidType());
        return result.get(0);
    }
}
