package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class ThreadFuncSleep extends FuncType {

    public ThreadFuncSleep() {
        super("sleep", Collections.singletonList("code"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() == 2, "thread sleep:invalid args size");
        Assert.require(a.get(0) instanceof JavaType
                && ((JavaType<?>) a.get(0)).value instanceof QThread,
                "thread sleep:not a thread");
        Assert.require(QType.isNum(a.get(1)), "thread sleep:not a number");
        QThread thread = ((QThread) ((JavaType<?>) a.get(0)).value);
        thread.goodNight((long) ((NumType) a.get(1)).value);
        return QType.V();
    }

    @Override
    public QType copy() {
        return new ThreadFuncSleep();
    }
}
