package me.tapeline.quailj.runtime.std.string;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class StringFuncReplaceAll extends QBuiltinFunc {

    public StringFuncReplaceAll(Runtime runtime) {
        super(
                "replaceAll",
                Arrays.asList(
                        new FuncArgument(
                                "str",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "needle",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "replacement",
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
        return QObject.Val(args.get("str").toString().replaceAll(Pattern.quote(args.get("needle").toString()),
                args.get("replacement").toString()));
    }

}
