package me.tapeline.quailj.runtime.std.string;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class StringFuncChars extends QBuiltinFunc {

    public StringFuncChars(Runtime runtime) {
        super(
                "chars",
                Collections.singletonList(
                        new FuncArgument(
                                "str",
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
        List<QObject> split = new ArrayList<>();
        String s = args.get("str").toString();
        int size = s.length();
        for (int i = 0; i < size; i++)
            split.add(QObject.Val("" + s.charAt(i)));
        return QObject.Val(split);
    }

}
