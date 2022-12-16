package me.tapeline.quailj.runtime.std.standart.common;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class FuncMap extends QBuiltinFunc {

    public FuncMap(Runtime runtime) {
        super(
                "map",
                Arrays.asList(
                        new FuncArgument(
                                "callback",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_FUNC)),
                                false
                        ),
                        new FuncArgument(
                                "collection",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_LIST)),
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
        List<QObject> values = args.get("collection").listValue();
        QFunc callback = ((QFunc) args.get("callback"));
        List<QObject> result = new ArrayList<>();
        int count = values.size();
        for (int i = 0; i < count; i++)
            result.add(callback.call(runtime, Collections.singletonList(values.get(i))));
        return QObject.Val(result);
    }

}
