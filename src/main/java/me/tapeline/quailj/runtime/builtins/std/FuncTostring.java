package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuncTostring extends FuncType {

    public FuncTostring() {
        super("tostring", Collections.singletonList("a"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        if (a.get(0).v instanceof ContainerType &&
                a.get(0).v.table.containsKey("_tostring") &&
                a.get(0).v.table.get("_tostring").v instanceof FuncType) {
            List<QValue> metaArgs = new ArrayList<>(Collections.singletonList(a.get(0)));
            metaArgs.addAll(a);
            return ((FuncType) a.get(0).v.table.get("_tostring").v).
                    run(runtime, metaArgs);
        } else return new QValue(a.get(0).v.toString());
    }

    @Override
    public QType copy() {
        return new FuncTostring();
    }
}
