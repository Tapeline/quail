package me.tapeline.quailj.runtime.std.standart.io;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QList;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FuncPrint extends QBuiltinFunc {

    public FuncPrint(Runtime runtime) {
        super(
                "print",
                Arrays.asList(new FuncArgument("values",
                        new ArrayList<>(), true)),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        QList values = (QList) args.get("values");
        for (QObject o : values.values)
            System.out.print(o.toString() + " ");
        System.out.println();
        return QObject.Val();
    }

}
