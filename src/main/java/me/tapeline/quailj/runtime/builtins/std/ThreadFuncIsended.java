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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() == 1, "thread isended:invalid args size");
        Assert.require(a.get(0) instanceof JavaType
                && ((JavaType<?>) a.get(0)).value instanceof QThread,
                "thread isended:not a thread");
        return QType.V(((QThread) ((JavaType<?>) a.get(0)).value).isAlive());
    }

    @Override
    public QType copy() {
        return new ThreadFuncIsended();
    }
}
