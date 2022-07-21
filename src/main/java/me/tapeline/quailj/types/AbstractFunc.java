package me.tapeline.quailj.types;

import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.lexer.TokenType;
import me.tapeline.quailj.libmanagement.AbstractMethodAction;
import me.tapeline.quailj.parser.nodes.BlockNode;
import me.tapeline.quailj.parser.nodes.VariableNode;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.VariableTable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFunc extends QType {

    public String name;
    public transient List<VariableNode> args = new ArrayList<>();
    public boolean restrictMetacalls = false;

    public static VariableTable tableToClone = new VariableTable("Default FuncType");

    public abstract QType run(Runtime runtime, List<QType> a) throws RuntimeStriker;

    public abstract QType copy();

    @Override
    public String toString() {
        return "func " + name;
    }

    public static AbstractFunc create(String name, List<VariableNode> args, boolean restrictMetacalls,
                                      AbstractMethodAction action) {
        AbstractFunc f = new AbstractFunc() {
            public AbstractMethodAction methodAction = action;

            @Override
            public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
                return methodAction.run(this, runtime, a);
            }

            @Override
            public QType copy() {
                return AbstractFunc.create(name, args, restrictMetacalls, action);
            }
        };
        f.name = name;
        f.args = args;
        f.restrictMetacalls = restrictMetacalls;
        return f;
    }


}
