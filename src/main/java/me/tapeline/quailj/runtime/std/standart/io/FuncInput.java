package me.tapeline.quailj.runtime.std.standart.io;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;

public class FuncInput extends QBuiltinFunc {

    public FuncInput(Runtime runtime) {
        super(
                "input",
                Collections.singletonList(
                        new FuncArgument(
                                "prompt",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
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
        if (!args.get("prompt").isNull())
            runtime.io.consolePut(args.get("prompt").toString());
        return QObject.Val(runtime.io.consoleInput());
    }

}
