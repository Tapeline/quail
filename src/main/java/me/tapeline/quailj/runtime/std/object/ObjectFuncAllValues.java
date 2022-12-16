package me.tapeline.quailj.runtime.std.object;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class ObjectFuncAllValues extends QBuiltinFunc {

    public ObjectFuncAllValues(Runtime runtime) {
        super(
                "allValues",
                Collections.singletonList(
                        new FuncArgument(
                                "obj",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        return QObject.Val(new ArrayList<>(args.get("obj").getTable().getValues().values()));
    }

}
