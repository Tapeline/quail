package me.tapeline.quailj.runtime.std.list;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class ListFuncSorted extends QBuiltinFunc {

    public ListFuncSorted(Runtime runtime) {
        super(
                "sorted",
                Arrays.asList(
                        new FuncArgument(
                                "list",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        ),
                        new FuncArgument(
                                "comparator",
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
        List<QObject> list = args.get("list").listValue();
        List<QObject> sorted = list.subList(0, list.size());
        QFunc comparator = ((QFunc) args.get("comparator"));
        List<QObject> comparatorArgs = new ArrayList<>();
        comparatorArgs.add(QObject.Val());
        comparatorArgs.add(QObject.Val());
        sorted.sort((o1, o2) -> {
            comparatorArgs.set(0, o1);
            comparatorArgs.set(1, o2);
            try {
                return (int) comparator.call(runtime, comparatorArgs).numValue();
            } catch (RuntimeStriker ignored) {}
            return 0;
        });
        return QObject.Val(sorted);
    }

}
