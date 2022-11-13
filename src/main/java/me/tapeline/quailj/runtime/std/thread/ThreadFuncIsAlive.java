package me.tapeline.quailj.runtime.std.thread;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ThreadFuncIsAlive extends QBuiltinFunc {

    public ThreadFuncIsAlive(Runtime runtime) {
        super(
                "isAlive",
                Arrays.asList(
                        new FuncArgument(
                                "thread",
                                new ArrayList<>(),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        if (!(args.get("thread") instanceof QThread))
            Runtime.error("Not a thread");
        return QObject.Val(((QThread) args.get("thread")).worker.isAlive());
    }

}
