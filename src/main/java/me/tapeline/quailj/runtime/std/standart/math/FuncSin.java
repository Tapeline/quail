package me.tapeline.quailj.runtime.std.standart.math;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;

public class FuncSin extends QBuiltinFunc {

    public FuncSin(Runtime runtime) {
        super(
                "sin",
                Collections.singletonList(
                        new FuncArgument(
                                "n",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_NUM)),
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
        return QObject.Val(Math.sin(args.get("n").numValue()));
    }

}
