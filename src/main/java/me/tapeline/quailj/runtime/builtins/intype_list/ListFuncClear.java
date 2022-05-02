package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ListFuncClear extends FuncType {

    public ListFuncClear() {
        super("clear", Arrays.asList("l"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "list clear:invalid args size");
        Assert.require(a.get(0).v instanceof ListType, "list clear:invalid arg0 type");
        ((ListType) a.get(0).v).values.clear();
        return new QValue();
    }

    @Override
    public QType copy() {
        return new ListFuncClear();
    }
}
