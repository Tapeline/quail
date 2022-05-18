package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class ThreadFuncWake extends FuncType {

    public ThreadFuncWake() {
        super("wake", Collections.singletonList("code"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() == 1, "thread wake:invalid args size");
        Assert.require(a.get(0) instanceof JavaType
                && ((JavaType<?>) a.get(0)).value instanceof QThread,
                "thread wake:not a thread");
        QThread thread = ((QThread) ((JavaType<?>) a.get(0)).value);
        thread.interrupt();
        return QType.V();
    }

    @Override
    public QType copy() {
        return new ThreadFuncWake();
    }
}
