package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuncTonum extends FuncType {

    public FuncTonum() {
        super("tonum", Collections.singletonList("a"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.get(0) instanceof ContainerType &&
                a.get(0).table.containsKey("_tonum") &&
                a.get(0).table.get("_tonum") instanceof FuncType) {
            List<QType> metaArgs = new ArrayList<>(Collections.singletonList(a.get(0)));
            metaArgs.addAll(a);
            return ((FuncType) a.get(0).table.get("_tonum")).
                    run(runtime, metaArgs);
        } else {
            try {
                if (a.get(0) instanceof BoolType) return QType.V(((BoolType) a.get(0)).value? 1 : 0);
                double d = Double.parseDouble(a.get(0).toString());
                return QType.V(d);
            } catch (NumberFormatException nfe) {
                throw new RuntimeStriker("run:tonum:cannot parse number");
            }
        }
    }

    @Override
    public QType copy() {
        return new FuncTonum();
    }
}
