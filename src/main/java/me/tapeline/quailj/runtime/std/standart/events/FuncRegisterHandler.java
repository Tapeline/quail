package me.tapeline.quailj.runtime.std.standart.events;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.libmanagement.Embed;
import me.tapeline.quailj.libmanagement.EmbedLoader;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FuncRegisterHandler extends QBuiltinFunc {

    public FuncRegisterHandler(Runtime runtime) {
        super(
                "registerHandler",
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
                        ),
                        new FuncArgument(
                                "ignoreCancelled",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_BOOL)),
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
        runtime.putEventHandler(
                args.get("event").toString(),
                ((QFunc) args.get("handler")),
                args.get("ignoreCancelled").boolValue()
        );
        return QObject.Val();
    }

}
