package me.tapeline.quailj.runtime.std.string;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.HashMap;

public class StringFuncReversed extends QBuiltinFunc {

    public StringFuncReversed(Runtime runtime) {
        super(
                "reversed",
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
        return QObject.Val(StringUtils.reverse(args.get("str").toString()));
    }

}
