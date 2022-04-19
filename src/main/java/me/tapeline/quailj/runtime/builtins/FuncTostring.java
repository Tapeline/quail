package me.tapeline.quailj.runtime.builtins;

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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.get(0) instanceof ContainerType &&
                a.get(0).table.containsKey("_tostring") &&
                a.get(0).table.get("_tostring") instanceof FuncType) {
            List<QType> metaArgs = new ArrayList<>(Collections.singletonList(a.get(0)));
            metaArgs.addAll(a);
            return ((FuncType) a.get(0).table.get("_tostring")).
                    run(runtime, metaArgs);
        } else return new StringType(a.get(0).toString());
    }

    @Override
    public QType copy() {
        return new FuncTostring();
    }
}
