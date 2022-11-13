package me.tapeline.quailj.runtime.std.list;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import me.tapeline.quailj.utils.QListUtils;
import org.apache.commons.collections.ListUtils;

import java.util.*;

public class ListFuncReversed extends QBuiltinFunc {

    public ListFuncReversed(Runtime runtime) {
        super(
                "reversed",
                Arrays.asList(
                        new FuncArgument(
                                "list",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_LIST)),
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
        List<QObject> reversed = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--)
            reversed.add(list.get(i));
        return QObject.Val(reversed);
    }

}
