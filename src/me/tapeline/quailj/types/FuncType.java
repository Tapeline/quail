package me.tapeline.quailj.types;

import me.tapeline.quailj.parser.nodes.BlockNode;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;

import java.util.HashMap;
import java.util.List;

public class FuncType extends QType {

    public String name;
    public List<String> args;
    public BlockNode code;
    public boolean restrictMetacalls = false;

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public FuncType(String name, List<String> args, BlockNode code) {
        this.code = code;
        this.args = args;
        this.name = name;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<String> args, BlockNode code, boolean r) {
        this.code = code;
        this.args = args;
        this.name = name;
        this.restrictMetacalls = r;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Memory mem = new Memory(runtime.scope);
        for (int i = 0; i < Math.min(args.size(), a.size()); i++) {
            mem.set(args.get(i), a.get(i));
        }
        try {
            runtime.run(this.code, mem);
        } catch (RuntimeStriker striker) {
            if (striker.isNotException()) {
                return striker.val;
            } else {
                throw striker;
            }
        }
        return new VoidType();
    }

    @Override
    public QType copy() {
        FuncType v = new FuncType(
                this.name,
                this.args,
                this.code,
                this.restrictMetacalls
        );
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return "func " + name;
    }


}
