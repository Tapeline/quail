package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuncThread extends FuncType {

    public FuncThread() {
        super("thread", Collections.singletonList("code"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.require(a.size() > 0, "func thread:invalid args size");
        Assert.require(QValue.isFunc(a.get(0)), "func thread:thread should be function");
        if (a.size() == 2)
            Assert.require(QValue.isList(a.get(1)), "func thread:expected list of args");
        QThread thread = new QThread(runtime, ((FuncType) a.get(0).v),
                a.size() == 1? new ArrayList<>() : ((ListType) a.get(1).v).values);
        JavaType<QThread> val = new JavaType<>(thread);
        val.table.put("start",   new QValue(new ThreadFuncStart()));
        val.table.put("result",  new QValue(new ThreadFuncResult()));
        val.table.put("isended", new QValue(new ThreadFuncIsended()));
        val.table.put("wait",    new QValue(new ThreadFuncWait()));
        return new QValue(val);
    }

    @Override
    public QType copy() {
        return new FuncThread();
    }
}
