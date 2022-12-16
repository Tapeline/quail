package me.tapeline.quailj.runtime.std.standart.numbers;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;

public class FuncHex extends QBuiltinFunc {

    public FuncHex(Runtime runtime) {
        super(
                "hex",
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
        return QObject.Val(Integer.toHexString((int) args.get("n").numValue()));
    }

}
