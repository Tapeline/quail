package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;
import me.tapeline.quailj.utils.Utilities;

import java.util.Arrays;
import java.util.List;

public class ListFuncFind extends FuncType {

    public ListFuncFind() {
        super("find", Arrays.asList("l", "needle"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "list find:invalid args size");
        Assert.require(a.get(0).v instanceof ListType, "list find:invalid arg0 type");
        Assert.require(a.get(1) != null, "list find:invalid arg1 type");
        for (int i = 0; i < ((ListType) a.get(0).v).values.size(); i++) if (Utilities.compare(
                ((ListType) a.get(0).v).values.get(i), a.get(1)).value) return new QValue(i);
        return new QValue(-1);
    }

    @Override
    public QType copy() {
        return new ListFuncFind();
    }
}
