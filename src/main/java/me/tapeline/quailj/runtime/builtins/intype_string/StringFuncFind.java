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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "string find:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "string find:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "string find:invalid arg1 type");
        return new NumType(((StringType) a.get(0)).value.indexOf(((StringType) a.get(1)).value));
    }

    @Override
    public QType copy() {
        return new StringFuncFind();
    }
}
