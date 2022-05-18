package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Collections;
import java.util.List;

public class FuncOrd extends FuncType {

    public FuncOrd() {
        super("ord", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1) throw new RuntimeStriker("func ord:specify a value!");
        return QType.V((int) a.get(0).toString().toCharArray()[0]);
    }

    @Override
    public QType copy() {
        return new FuncOrd();
    }
}
