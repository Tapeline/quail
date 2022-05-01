package me.tapeline.quailj.runtime.builtins;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.types.VoidType;

import java.util.Collections;
import java.util.List;

public class FuncPut extends FuncType {

    public FuncPut() {
        super("put", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() > 0) runtime.io.consolePut(QType.nullSafe(a.get(0)).toString());
        return new VoidType();
    }

    @Override
    public QType copy() {
        return new FuncPut();
    }
}
