package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncMillis extends FuncType {

    public FuncMillis() {
        super("millis", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        return new QValue(System.currentTimeMillis());
    }

    @Override
    public QType copy() {
        return new FuncMillis();
    }
}
