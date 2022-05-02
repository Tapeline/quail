package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QValue;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Collections;
import java.util.List;

public class FuncOrd extends FuncType {

    public FuncOrd() {
        super("ord", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        if (a.size() < 1) throw new RuntimeStriker("func ord:specify a value!");
        return new QValue((int) a.get(0).toString().toCharArray()[0]);
    }

    @Override
    public QType copy() {
        return new FuncOrd();
    }
}
