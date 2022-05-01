package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncFind extends FuncType {

    public StringFuncFind() {
        super("find", Arrays.asList("str", "needle"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "string find:invalid args size");
        Assert.require(a.get(0).v instanceof StringType, "string find:invalid arg0 type");
        Assert.require(a.get(1).v instanceof StringType, "string find:invalid arg1 type");
        return new QValue(((StringType) a.get(0).v).value.indexOf(((StringType) a.get(1).v).value));
    }

    @Override
    public QType copy() {
        return new StringFuncFind();
    }
}
