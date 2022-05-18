package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuncRegisterhandler extends FuncType {

    public FuncRegisterhandler() {
        super("registerhandler", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.get(1) instanceof FuncType && a.get(0) instanceof StringType,
                "func registerhandler:invalid args");
        String event = ((StringType) a.get(0)).value;
        FuncType f = (FuncType) a.get(1).copy();
        if (runtime.eventHandlers.containsKey(event)) {
            List<String> e = runtime.eventHandlers.get(event);
            e.add(f.name);
            runtime.eventHandlers.put(event, e);
        }
        else runtime.eventHandlers.put(event, new ArrayList<>(Collections.singletonList(
                f.name)));
        return QType.V(f.name);
    }

    @Override
    public QType copy() {
        return new FuncRegisterhandler();
    }
}
