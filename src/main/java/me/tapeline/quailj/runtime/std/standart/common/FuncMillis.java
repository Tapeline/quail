package me.tapeline.quailj.runtime.std.standart.common;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;

public class FuncMillis extends QBuiltinFunc {

    public FuncMillis(Runtime runtime) {
        super(
                "millis",
                Collections.emptyList(),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        return QObject.Val(System.currentTimeMillis());
    }

}
