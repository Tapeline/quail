package me.tapeline.quailj.runtime.std.sys;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class SysFuncGetProperty extends QBuiltinFunc {

    public SysFuncGetProperty(Runtime runtime) {
        super(
                "getProperty",
                Arrays.asList(
                        new FuncArgument(
                                "prop",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        return QObject.Val(System.getProperty(args.get("prop").toString()));
    }

}
