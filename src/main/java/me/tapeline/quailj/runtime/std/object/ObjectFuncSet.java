package me.tapeline.quailj.runtime.std.object;

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

public class ObjectFuncSet extends QBuiltinFunc {

    public ObjectFuncSet(Runtime runtime) {
        super(
                "set",
                Arrays.asList(
                        new FuncArgument(
                                "obj",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "key",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "value",
                                new ArrayList<>(),
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
        args.get("obj").setOverridable(runtime, args.get("key").toString(), args.get("value"));
        return QObject.Val();
    }

}
