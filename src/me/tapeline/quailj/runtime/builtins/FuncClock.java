package me.tapeline.quailj.runtime.builtins;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.NumType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Collections;
import java.util.List;

public class FuncClock extends FuncType {

    public FuncClock() {
        super("clock", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        return new NumType(((float)System.currentTimeMillis()) / 1000F);
    }
}
