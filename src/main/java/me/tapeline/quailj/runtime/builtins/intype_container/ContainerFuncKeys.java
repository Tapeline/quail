package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ContainerFuncKeys extends FuncType {

    public ContainerFuncKeys() {
        super("keys", Arrays.asList("c"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "container keys:invalid args size");
        Assert.require(a.get(0) instanceof ContainerType, "container keys:invalid arg0 type");
        ListType l = new ListType();
        QType.forEachNotBuiltIn(a.get(0), (k, v) -> l.values.add(QType.V(k)));
        return l;
    }

    @Override
    public QType copy() {
        return new ContainerFuncKeys();
    }
}
