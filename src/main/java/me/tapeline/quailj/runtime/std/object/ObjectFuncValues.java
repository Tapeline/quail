package me.tapeline.quailj.runtime.std.object;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ObjectFuncValues extends QBuiltinFunc {

    public ObjectFuncValues(Runtime runtime) {
        super(
                "values",
                Arrays.asList(
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        return QObject.Val(new ArrayList<>(args.get("obj").getNonDefaultFields().values()));
    }

}