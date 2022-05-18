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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "container size:invalid args size");
        Assert.require(a.get(0) instanceof ContainerType, "container size:invalid arg0 type");
        return QType.V(a.get(0).table.size());
    }

    @Override
    public QType copy() {
        return new ContainerFuncSize();
    }
}
