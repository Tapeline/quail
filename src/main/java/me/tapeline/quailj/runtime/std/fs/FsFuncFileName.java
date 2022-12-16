package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;

public class FsFuncFileName extends QBuiltinFunc {

    public FsFuncFileName(Runtime runtime) {
        super(
                "fileName",
                Collections.singletonList(
                        new FuncArgument(
                                "path",
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
        return QObject.Val(new File(args.get("path").toString()).getName());
    }

}
