package me.tapeline.quailj.runtime.builtins.library_random;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Collections;
import java.util.List;

public class RandomFuncRandom extends FuncType {

    public RandomFuncRandom() {
        super("random", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        return QType.V(Math.random());
    }

    @Override
    public QType copy() {
        return new RandomFuncRandom();
    }
}
