package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class ThreadFuncWait extends FuncType {

    public ThreadFuncWait() {
        super("wait", Collections.singletonList("code"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() == 1, "thread wait:invalid args size");
        Assert.require(a.get(0) instanceof JavaType
                && ((JavaType<?>) a.get(0)).value instanceof QThread,
                "thread wait:not a thread");
        QThread thread = ((QThread) ((JavaType<?>) a.get(0)).value);
        while (thread.isAlive()) {}
        return thread.ret;
    }

    @Override
    public QType copy() {
        return new ThreadFuncWait();
    }
}
