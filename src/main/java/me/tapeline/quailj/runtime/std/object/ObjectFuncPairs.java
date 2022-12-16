package me.tapeline.quailj.runtime.std.object;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import me.tapeline.quailj.utils.Utilities;

import java.util.*;

public class ObjectFuncPairs extends QBuiltinFunc {

    public ObjectFuncPairs(Runtime runtime) {
        super(
                "pairs",
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
        Set<Map.Entry<String, QObject>> pairs = args.get("obj").getNonDefaultFields().entrySet();
        List<QObject> pairsList = new ArrayList<>();
        for (Map.Entry<String, QObject> entry : pairs)
            pairsList.add(QObject.Val(Utilities.asList(QObject.Val(entry.getKey()), entry.getValue())));
        return QObject.Val(pairsList);
    }

}
