package me.tapeline.quailj.runtime.std.standart.math;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class FuncMin extends QBuiltinFunc {

    public FuncMin(Runtime runtime) {
        super(
                "min",
                Collections.singletonList(
                        new FuncArgument(
                                "values",
                                new ArrayList<>(),
                                true
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        List<QObject> values = args.get("values").listValue();
        int count = values.size();
        double minValue = values.get(0).numValue();
        for (int i = 0; i < count; i++) {
            QObject val = values.get(i);
            if (!val.isNum()) Runtime.error("Cannot find min among non-num values: " + val);
            if (val.numValue() < minValue) minValue = val.numValue();
        }
        return QObject.Val(minValue);
    }

}
