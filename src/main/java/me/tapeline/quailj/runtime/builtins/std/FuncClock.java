package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncClock extends FuncType {

    public FuncClock() {
        super("clock", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        return new QValue(((float)System.currentTimeMillis()) / 1000F);
    }

    @Override
    public QType copy() {
        return new FuncClock();
    }
}
