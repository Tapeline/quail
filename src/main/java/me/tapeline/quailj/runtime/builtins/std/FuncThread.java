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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() > 0, "func thread:invalid args size");
        Assert.require(QType.isFunc(a.get(0)), "func thread:thread should be function");
        if (a.size() == 2)
            Assert.require(QType.isList(a.get(1)), "func thread:expected list of args");
        QThread thread = new QThread(runtime, ((FuncType) a.get(0)),
                a.size() == 1? new ArrayList<>() : ((ListType) a.get(1)).values);
        JavaType<QThread> val = new JavaType<>(thread);
        val.table.put("start",   new ThreadFuncStart());
        val.table.put("result",  new ThreadFuncResult());
        val.table.put("isended", new ThreadFuncIsended());
        val.table.put("wait",    new ThreadFuncWait());
        val.table.put("sleep",   new ThreadFuncSleep());
        val.table.put("wake",    new ThreadFuncWake());
        thread.setThreadObject(val);
        return val;
    }

    @Override
    public QType copy() {
        return new FuncThread();
    }
}
