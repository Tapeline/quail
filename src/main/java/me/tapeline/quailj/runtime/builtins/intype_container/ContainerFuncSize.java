package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class ContainerFuncSize extends FuncType {

    public ContainerFuncSize() {
        super("size", Collections.singletonList("c"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "container size:invalid args size");
        Assert.require(a.get(0).v instanceof ContainerType, "container size:invalid arg0 type");
        return new QValue(a.get(0).v.table.size());
    }

    @Override
    public QType copy() {
        return new ContainerFuncSize();
    }
}
