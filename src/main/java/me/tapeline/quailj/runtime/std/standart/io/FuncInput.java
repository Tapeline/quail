package me.tapeline.quailj.runtime.std.standart.io;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QList;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import me.tapeline.quailj.utils.ErrorFormatter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FuncInput extends QBuiltinFunc {

    public FuncInput(Runtime runtime) {
        super(
                "input",
                Arrays.asList(
                        new FuncArgument(
                                "prompt",
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
        if (!args.get("prompt").isNull())
            runtime.io.consolePut(args.get("prompt").toString());
        return QObject.Val(runtime.io.consoleInput());
    }

}
