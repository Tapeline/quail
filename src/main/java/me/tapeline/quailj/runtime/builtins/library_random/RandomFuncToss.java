package me.tapeline.quailj.runtime.builtins.library_random;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomFuncToss extends FuncType {

    public RandomFuncToss() {
        super("toss", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        return new BoolType(new Random().nextBoolean());
    }

    @Override
    public QType copy() {
        return new RandomFuncToss();
    }
}
