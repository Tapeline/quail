package me.tapeline.quailj.runtime.std.string;

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
import java.util.List;
import java.util.regex.Pattern;

public class StringFuncSplit extends QBuiltinFunc {

    public StringFuncSplit(Runtime runtime) {
        super(
                "split",
                Arrays.asList(
                        new FuncArgument(
                                "str",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "sep",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
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
        List<QObject> split = new ArrayList<>();
        String[] parts = args.get("str").toString().split(
                Pattern.quote(args.get("sep").toString())
        );
        for (int i = 0; i < parts.length; i++)
            split.add(QObject.Val(parts[i]));
        return QObject.Val(split);
    }

}
