package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class ListFuncSet extends FuncType {

    public ListFuncSet() {
        super("set", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 3, "list set:invalid args size");
        Assert.require(QValue.isList(a.get(0)) && QType.isNum(a.get(1).v), "list set:invalid types");
        Assert.require(((ListType) a.get(0).v).values.size() > ((NumType) a.get(1).v).value,
                "list set:out of bounds");
        return ((ListType) a.get(0).v).values.set((int) ((NumType) a.get(1).v).value, a.get(2));
    }

    @Override
    public QType copy() {
        return new ListFuncSet();
    }
}
