package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.literals.LiteralBool;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class FsFuncSetWritable extends QBuiltinFunc {

    public FsFuncSetWritable(Runtime runtime) {
        super(
                "setWritable",
                Arrays.asList(
                        new FuncArgument(
                                "path",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "flag",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_BOOL)),
                                false
                        ),
                        new FuncArgument(
                                "ownerOnly",
                                new LiteralBool(Token.UNDEFINED, true),
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_BOOL)),
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
        return QObject.Val(new File(args.get("path").toString()).setWritable(
            args.get("flag").isTrue(),
            args.get("ownerOnly").isTrue()
        ));
    }

}
