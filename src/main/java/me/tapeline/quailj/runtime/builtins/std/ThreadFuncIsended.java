package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class ThreadFuncIsended extends FuncType {

    public ThreadFuncIsended() {
        super("isended", Collections.singletonList("code"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.require(a.size() == 1, "thread isended:invalid args size");
        Assert.require(a.get(0).v instanceof JavaType
                && ((JavaType<?>) a.get(0).v).value instanceof QThread,
                "thread isended:not a thread");
        return new QValue(((QThread) ((JavaType<?>) a.get(0).v).value).isAlive());
    }

    @Override
    public QType copy() {
        return new ThreadFuncIsended();
    }
}
