package me.tapeline.quailj.runtime.std.standart.math;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Arrays;
import java.util.HashMap;

public class FuncAtan2 extends QBuiltinFunc {

    public FuncAtan2(Runtime runtime) {
        super(
                "atan2",
                Arrays.asList(
                        new FuncArgument(
                                "x",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "y",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
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
        return QObject.Val(Math.atan2(args.get("x").numValue(), args.get("y").numValue()));
    }

}
