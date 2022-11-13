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
import java.util.Locale;

public class StringFuncCapitalize extends QBuiltinFunc {

    public StringFuncCapitalize(Runtime runtime) {
        super(
                "capitalize",
                Arrays.asList(
                        new FuncArgument(
                                "str",
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
        return QObject.Val(
                args.get("str").toString().substring(0, 1) +
                        args.get("str").toString().substring(1).toLowerCase(Locale.ROOT)
        );
    }

}
