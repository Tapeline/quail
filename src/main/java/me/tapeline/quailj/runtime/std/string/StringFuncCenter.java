package me.tapeline.quailj.runtime.std.string;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class StringFuncCenter extends QBuiltinFunc {

    public StringFuncCenter(Runtime runtime) {
        super(
                "center",
                Arrays.asList(
                        new FuncArgument(
                                "str",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "size",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
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
        return QObject.Val(StringUtils.center(args.get("str").toString(), (int) args.get("size").numValue()));
    }

}
