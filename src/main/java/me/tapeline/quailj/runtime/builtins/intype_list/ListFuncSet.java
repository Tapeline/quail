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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 3, "list set:invalid args size");
        Assert.require(QType.isList(a.get(0)) && QType.isNum(a.get(1)), "list set:invalid types");
        Assert.require(((ListType) a.get(0)).values.size() > ((NumType) a.get(1)).value,
                "list set:out of bounds");
        return ((ListType) a.get(0)).values.set((int) ((NumType) a.get(1)).value, a.get(2));
    }

    @Override
    public QType copy() {
        return new ListFuncSet();
    }
}
