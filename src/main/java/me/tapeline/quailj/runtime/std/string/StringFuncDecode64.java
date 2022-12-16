package me.tapeline.quailj.runtime.std.string;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import org.apache.commons.codec.binary.Base64;

import java.util.Collections;
import java.util.HashMap;

public class StringFuncDecode64 extends QBuiltinFunc {

    public StringFuncDecode64(Runtime runtime) {
        super(
                "decode64",
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
        return QObject.Val(new String(new Base64().decode(args.get("str").toString().getBytes())));
    }

}
