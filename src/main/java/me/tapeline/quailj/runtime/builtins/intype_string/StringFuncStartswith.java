package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncStartswith extends FuncType {

    public StringFuncStartswith() {
        super("startswith", Arrays.asList("str", "needle"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "string startswith:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "string startswith:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "string startswith:invalid arg1 type");
        return QType.V(((StringType) a.get(0)).value.startsWith(a.get(1).toString()));
    }

    @Override
    public QType copy() {
        return new StringFuncStartswith();
    }
}
