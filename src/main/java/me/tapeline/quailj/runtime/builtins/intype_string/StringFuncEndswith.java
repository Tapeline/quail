package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncEndswith extends FuncType {

    public StringFuncEndswith() {
        super("endswith", Arrays.asList("str", "needle"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "string endswith:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "string endswith:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "string endswith:invalid arg1 type");
        return QType.V(((StringType) a.get(0)).value.endsWith(a.get(1).toString()));
    }

    @Override
    public QType copy() {
        return new StringFuncEndswith();
    }
}
