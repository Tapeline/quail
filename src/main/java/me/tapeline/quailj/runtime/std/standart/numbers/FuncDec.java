package me.tapeline.quailj.runtime.std.standart.numbers;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FuncDec extends QBuiltinFunc {

    public FuncDec(Runtime runtime) {
        super(
                "dec",
                Arrays.asList(
                        new FuncArgument(
                                "n",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "base",
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
        return QObject.Val(Integer.parseInt(args.get("n").toString(), ((int) args.get("base").numValue())));
    }

}
