package me.tapeline.quailj.runtime.builtins;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.NumType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Collections;
import java.util.List;

public class FuncMillis extends FuncType {

    public FuncMillis() {
        super("millis", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        return new NumType(System.currentTimeMillis());
    }
}
