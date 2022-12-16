package me.tapeline.quailj.runtime.std.standart.io;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QList;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FuncPrint extends QBuiltinFunc {

    public FuncPrint(Runtime runtime) {
        super(
                "print",
                Collections.singletonList(new FuncArgument("values",
                        new ArrayList<>(), true)),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        QList values = (QList) args.get("values");
        for (QObject o : values.values)
            System.out.print(o.toString() + " ");
        System.out.println();
        return QObject.Val();
    }

}
