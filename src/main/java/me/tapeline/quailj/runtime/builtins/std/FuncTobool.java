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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.get(0) instanceof ContainerType &&
                a.get(0).table.containsKey("_tobool") &&
                a.get(0).table.get("_tobool") instanceof FuncType) {
            List<QType> metaArgs = new ArrayList<>(Collections.singletonList(a.get(0)));
            metaArgs.addAll(a);
            return ((FuncType) a.get(0).table.get("_tobool")).
                    run(runtime, metaArgs);
        } else {
            try {
                boolean b = Boolean.parseBoolean(a.get(0).toString());
                return QType.V(b);
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
