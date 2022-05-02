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
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        if (a.get(0).v instanceof ContainerType &&
                a.get(0).v.table.containsKey("_tonum") &&
                a.get(0).v.table.get("_tonum").v instanceof FuncType) {
            List<QValue> metaArgs = new ArrayList<>(Collections.singletonList(a.get(0)));
            metaArgs.addAll(a);
            return ((FuncType) a.get(0).v.table.get("_tonum").v).
                    run(runtime, metaArgs);
        } else {
            try {
                double d = Double.parseDouble(a.get(0).v.toString());
                return new QValue(d);
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
