package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuncTobool extends FuncType {

    public FuncTobool() {
        super("tobool", Collections.singletonList("a"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        if (a.get(0).v instanceof ContainerType &&
                a.get(0).v.table.containsKey("_tobool") &&
                a.get(0).v.table.get("_tobool").v instanceof FuncType) {
            List<QValue> metaArgs = new ArrayList<>(Collections.singletonList(a.get(0)));
            metaArgs.addAll(a);
            return ((FuncType) a.get(0).v.table.get("_tobool").v).
                    run(runtime, metaArgs);
        } else {
            try {
                boolean b = Boolean.parseBoolean(a.get(0).v.toString());
                return new QValue(b);
            } catch (Exception nfe) {
                throw new RuntimeStriker("run:tobool:cannot parse bool");
            }
        }
    }

    @Override
    public QType copy() {
        return new FuncTobool();
    }
}
