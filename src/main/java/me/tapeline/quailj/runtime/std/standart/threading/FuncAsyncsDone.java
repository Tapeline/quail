package me.tapeline.quailj.runtime.std.standart.threading;

import me.tapeline.quailj.runtime.AsyncRuntimeWorker;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.HashMap;

public class FuncAsyncsDone extends QBuiltinFunc {

    public FuncAsyncsDone(Runtime runtime) {
        super(
                "asyncsDone",
                new ArrayList<>(),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        int ctr = 0;
        for (AsyncRuntimeWorker worker : runtime.asyncRuntimeWorkers)
            if (worker.isAlive())
                ctr++;
        return QObject.Val(ctr == 0);
    }

}
