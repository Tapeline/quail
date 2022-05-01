package me.tapeline.quailj.types;

import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.lexer.TokenType;
import me.tapeline.quailj.parser.nodes.BlockNode;
import me.tapeline.quailj.parser.nodes.VariableNode;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FuncType extends QType {

    public String name;
    public List<VariableNode> args = new ArrayList<>();
    public BlockNode code;
    public boolean restrictMetacalls = false;

    public static HashMap<String, QValue> tableToClone = new HashMap<>();

    public FuncType(String name, List<VariableNode> args, BlockNode code, Object marker) {
        this.code = code;
        this.args = args;
        this.name = name;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<VariableNode> args, BlockNode code, boolean r, Object marker) {
        this.code = code;
        this.args = args;
        this.name = name;
        this.restrictMetacalls = r;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<String> args, BlockNode code) {
        this.code = code;
        args.forEach((v) -> this.args.add(new VariableNode(new Token(
                TokenType.ID, v, code != null? code.codePos : 0))));
        this.name = name;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public FuncType(String name, List<String> args, BlockNode code, boolean r) {
        this.code = code;
        args.forEach((v) -> this.args.add(new VariableNode(new Token(TokenType.ID, v, code.codePos))));
        this.name = name;
        this.restrictMetacalls = r;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Memory mem = new Memory(runtime.scope);
        for (int i = 0; i < Math.min(args.size(), a.size()); i++) {
            if (args.get(i).isConsumer) {
                ListType l = new ListType(a.subList(i, a.size()));
                mem.set(args.get(i).token.c, new QValue(l));
            } else mem.set(args.get(i).token.c, a.get(i));
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
        return new QValue();
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
