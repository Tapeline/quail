package me.tapeline.quailj.types;

/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FuncType implements QType {

    public String name;
    public List<String> args;
    //public BlockNode code;
    public boolean restrictMetacalls = false;

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public HashMap<String, QType> table = new HashMap<>();

    public FuncType(String name) {
        this.code = new BlockNode();
        this.args = new ArrayList<>();
        this.name = name;
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<String> args) {
        this.code = new BlockNode();
        this.args = args;
        this.name = name;
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<String> args, BlockNode code) {
        this.code = code;
        this.args = args;
        this.name = name;
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<String> args, BlockNode code, boolean r) {
        this.code = code;
        this.args = args;
        this.name = name;
        this.restrictMetacalls = r;
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
    public String toString() {
        return "func " + name;
    }


}*/
