package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncNewevent extends FuncType {

    public FuncNewevent() {
        super("newevent", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1)
            throw new RuntimeStriker("newevent:invalid args length");
        ContainerType metadata = new ContainerType("_anonymous", "container", new HashMap<>(), false);
        if (a.size() > 1)
            if (a.get(1) instanceof ContainerType)
                metadata = (ContainerType) a.get(1);
            else throw new RuntimeStriker("newevent:event metadata can only be a container");
        String event = a.get(0).toString();
        Runtime.callEvent(runtime, event, metadata);
        return QType.V();
    }

    @Override
    public QType copy() {
        return new FuncNewevent();
    }
}
