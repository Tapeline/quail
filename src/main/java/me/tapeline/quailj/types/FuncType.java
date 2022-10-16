package me.tapeline.quailj.types;

import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.lexer.TokenType;
import me.tapeline.quailj.parser.nodes.BlockNode;
import me.tapeline.quailj.parser.nodes.VariableNode;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.VariableTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FuncType extends AbstractFunc {

    public String name;
    public transient List<VariableNode> args = new ArrayList<>();
    public transient BlockNode code;
    public Runtime boundRuntime = null;
    public List<AlternativeCall> alternatives = new ArrayList<>();

    public FuncType(String name, List<VariableNode> args, BlockNode code, Object marker) {
        this.code = code;
        this.args = args;
        this.name = name;
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<VariableNode> args, BlockNode code, boolean r, Object marker) {
        this.code = code;
        this.args = args;
        this.name = name;
        this.restrictMetacalls = r;
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<String> args, BlockNode code) {
        this.code = code;
        args.forEach((v) -> this.args.add(new VariableNode(new Token(
                TokenType.ID, v, code != null? code.codePos : 0))));
        this.name = name;
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<String> args, BlockNode code, boolean r) {
        this.code = code;
        args.forEach((v) -> this.args.add(new VariableNode(new Token(TokenType.ID, v, code.codePos))));
        this.name = name;
        this.restrictMetacalls = r;
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    /*public QType run(Runtime runtime, QType parent, List<QType> a) throws RuntimeStriker {
        return run(runtime, a);
    }*/

    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        //return run(runtime, null, a);
        if (boundRuntime != null)
            runtime = boundRuntime;
        Memory mem = new Memory(runtime.scope);
        /*if (parent != null)
            mem.set("this", parent, new ArrayList<>());*/
        List<VariableNode> args = this.args;
        for (AlternativeCall alt : alternatives) {
            boolean match = true;
            for (int i = 0; i < Math.min(alt.arguments.size(), a.size()); i++) {
                if (!alt.arguments.get(0).matchesRequirements(runtime, a.get(i))) {
                    match = false;
                    break;
                }
            }
            if (match) {
                for (int i = 0; i < Math.min(alt.arguments.size(), a.size()); i++) {
                    if (alt.arguments.get(i).isConsumer) {
                        ListType l = new ListType(a.subList(i, a.size()));
                        mem.set(alt.arguments.get(i).token.c, l, alt.arguments.get(i).modifiers);
                    } else mem.set(alt.arguments.get(i).token.c, a.get(i), alt.arguments.get(i).modifiers);
                }
                try {
                    runtime.run(alt.code, mem);
                } catch (RuntimeStriker striker) {
                    if (striker.isNotException()) {
                        return striker.val;
                    } else {
                        throw striker;
                    }
                }
                return QType.V();
            }
        }
        boolean match = true;
        for (int i = 0; i < Math.min(args.size(), a.size()); i++) {
            if (args.size() > 0 && !args.get(i).matchesRequirements(runtime, a.get(i))) {
                match = false;
                break;
            }
        }
        if (!match) {
            String s = "";
            for (AlternativeCall ac : alternatives)
                s += "  " + ac.toString() + "\n";
            s += "  " + args.toString();
            String as = "";
            for (QType qt : a)
                as += qt.getClass().getSimpleName() + " ";
            throw new RuntimeStriker("func " + name + ":failed to find valid call variant for (" +
                as + ")\n" +
                "Valid are:\n" + s);
        }
        for (int i = 0; i < Math.min(args.size(), a.size()); i++) {
            if (args.get(i).isConsumer) {
                ListType l = new ListType(a.subList(i, a.size()));
                mem.set(args.get(i).token.c, l, args.get(i).modifiers);
            } else mem.set(args.get(i).token.c, a.get(i), args.get(i).modifiers);
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
        return QType.V();
    }

    @Override
    public QType copy() {
        FuncType v = new FuncType(
                this.name,
                this.args,
                this.code,
                this.restrictMetacalls,
                null
        );
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return "func " + name;
    }


}
