package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class ListFuncGet extends FuncType {

    public ListFuncGet() {
        super("get", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "list get:invalid args size");
        Assert.require(QType.isList(a.get(0).v) && QType.isNum(a.get(1).v), "list get:invalid types");
        Assert.require(((ListType) a.get(0).v).values.size() > ((NumType) a.get(1).v).value,
                "list get:out of bounds");
        return ((ListType) a.get(0).v).values.get((int) ((NumType) a.get(1).v).value);
    }

    @Override
    public QType copy() {
        return new ListFuncGet();
    }
}
