package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ContainerFuncAllkeys extends FuncType {

    public ContainerFuncAllkeys() {
        super("allkeys", Arrays.asList("c"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "container allkeys:invalid args size");
        Assert.require(a.get(0).v instanceof ContainerType, "container allkeys:invalid arg0 type");
        ListType l = new ListType();
        for (String s : a.get(0).v.table.keySet())
            l.values.add(new QValue(s));
        return new QValue(l);
    }

    @Override
    public QType copy() {
        return new ContainerFuncAllkeys();
    }
}
