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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        return QType.V(System.currentTimeMillis());
    }

    @Override
    public QType copy() {
        return new FuncMillis();
    }
}
