package me.tapeline.quailj.runtime.std.object;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class ObjectStaticAssemblePairs extends QBuiltinFunc {

    public ObjectStaticAssemblePairs(Runtime runtime) {
        super(
                "assemblePairs",
                Collections.singletonList(
                        new FuncArgument(
                                "pairs",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        HashMap<String, QObject> obj = new HashMap<>();
        List<QObject> pairs = args.get("pairs").listValue();
        int size = pairs.size();
        List<QObject> pair = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            QObject o = pairs.get(i);
            if (!o.isList() || o.listValue().size() != 2)
                Runtime.error("Pair #" + (i + 1) + " is not a pair");
            pair = o.listValue();
            obj.put(pair.get(0).toString(), pair.get(1));
        }
        QObject container = QObject.Val(obj);
        container.setObjectMetadata(Runtime.superObject);
        container.setPrototypeFlag(false);
        container.isDict = true;
        return container;
    }

}
