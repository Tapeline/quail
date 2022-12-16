package me.tapeline.quailj.runtime.std.string;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class StringFuncFormat extends QBuiltinFunc {

    public StringFuncFormat(Runtime runtime) {
        super(
                "format",
                Arrays.asList(
                        new FuncArgument(
                                "str",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "values",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        if (args.get("values").isList()) {
            String result = args.get("str").toString();
            List<QObject> values = args.get("values").listValue();
            int size = values.size();
            for (int i = 0; i < size; i++)
                result = result.replaceAll("%" + i, values.get(i).toString());
            return QObject.Val(result);
        } else {
            String result = args.get("str").toString();
            for (Map.Entry<String, QObject> entry : args.get("values").getTable().getValues().entrySet())
                result = result.replaceAll("%" + entry.getKey(), entry.getValue().toString());
            return QObject.Val(result);
        }
    }

}
