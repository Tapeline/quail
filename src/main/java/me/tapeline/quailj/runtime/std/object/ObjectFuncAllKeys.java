package me.tapeline.quailj.runtime.std.object;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class ObjectFuncAllKeys extends QBuiltinFunc {

    public ObjectFuncAllKeys(Runtime runtime) {
        super(
                "allKeys",
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
        Set<String> keys = args.get("obj").getTable().getValues().keySet();
        List<QObject> keysList = new ArrayList<>();
        for (String key : keys)
            keysList.add(QObject.Val(key));
        return QObject.Val(keysList);
    }

}
