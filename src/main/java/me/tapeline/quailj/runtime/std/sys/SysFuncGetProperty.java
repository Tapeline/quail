package me.tapeline.quailj.runtime.std.sys;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;

public class SysFuncGetProperty extends QBuiltinFunc {

    public SysFuncGetProperty(Runtime runtime) {
        super(
                "getProperty",
                Collections.singletonList(
                        new FuncArgument(
                                "prop",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        return QObject.Val(System.getProperty(args.get("prop").toString()));
    }

}
