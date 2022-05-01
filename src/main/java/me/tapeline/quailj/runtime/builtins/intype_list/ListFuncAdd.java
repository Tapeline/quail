package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ListFuncAdd extends FuncType {

    public ListFuncAdd() {
        super("add", Arrays.asList("l", "val"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "list add:invalid args size");
        Assert.require(a.get(0).v instanceof ListType, "list add:invalid arg0 type");
        Assert.require(a.get(1) != null, "list add:invalid arg1 type");
        ((ListType) a.get(0).v).values.add(a.get(1));
        return new QValue();
    }

    @Override
    public QType copy() {
        return new ListFuncAdd();
    }
}
