package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncOut extends FuncType {

    public FuncOut() {
        super("out", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        if (a.size() > 0) runtime.io.consolePut(QValue.nullSafe(a.get(0)).toString() + "\n");
        else runtime.io.consolePut("\n");
        return new QValue();
    }

    @Override
    public QType copy() {
        return new FuncOut();
    }
}
