package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncSin extends FuncType {

    public FuncSin() {
        super("sin", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1 || !(a.get(0) instanceof NumType))
            throw new RuntimeStriker("func sin:specify a numeric value!");
        return QType.V(Math.sin(((NumType) a.get(0)).value));
    }

    @Override
    public QType copy() {
        return new FuncSin();
    }
}
