package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncChar extends FuncType {

    public FuncChar() {
        super("char", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        if (a.size() < 1 || !(a.get(0).v instanceof NumType))
            throw new RuntimeStriker("func char:specify a numeric value!");
        return new QValue(((char) ((Double) ((NumType) a.get(0).v).value).intValue()) + "");
    }

    @Override
    public QType copy() {
        return new FuncChar();
    }
}
