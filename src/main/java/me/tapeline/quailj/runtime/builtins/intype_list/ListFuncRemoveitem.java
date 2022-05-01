package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;
import me.tapeline.quailj.utils.Utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListFuncRemoveitem extends FuncType {

    public ListFuncRemoveitem() {
        super("removeitem", Arrays.asList("l", "subj"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "list removeItem:invalid args size");
        Assert.require(a.get(0) instanceof ListType, "list removeItem:invalid arg0 type");
        Assert.require(a.get(1) != null, "list removeItem:invalid arg1 type");
        int index = -1;
        for (int i = 0; i < ((ListType) a.get(0)).values.size(); i++) if (Utilities.compare(((ListType) a.get(0)).values.get(i),
                ((QType) a.get(1))).value)
            index = i;
        ((ListType) a.get(0)).values.remove(index);
        return a.get(0);
    }

    @Override
    public QType copy() {
        return new ListFuncRemoveitem();
    }
}
