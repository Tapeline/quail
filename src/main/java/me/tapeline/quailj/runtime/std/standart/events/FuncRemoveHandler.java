package me.tapeline.quailj.runtime.std.standart.events;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Arrays;
import java.util.HashMap;

public class FuncRemoveHandler extends QBuiltinFunc {

    public FuncRemoveHandler(Runtime runtime) {
        super(
                "removeHandler",
                Arrays.asList(
                        new FuncArgument(
                                "event",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "handler",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_FUNC)),
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
        runtime.removeEventHandler(
                args.get("event").toString(),
                ((QFunc) args.get("handler"))
        );
        return QObject.Val();
    }

}
