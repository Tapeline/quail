package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncSplit extends FuncType {

    public StringFuncSplit() {
        super("split", Arrays.asList("str", "sep"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "string split:invalid args size");
        Assert.require(a.get(0).v instanceof StringType, "string split:invalid arg0 type");
        Assert.require(a.get(1).v instanceof StringType, "string split:invalid arg1 type");
        ListType l = new ListType();
        for (String s : ((StringType) a.get(0).v).value.split(((StringType) a.get(1).v).value)) l.values.add(
                new QValue(s));
        return new QValue(l);
    }

    @Override
    public QType copy() {
        return new StringFuncSplit();
    }
}
