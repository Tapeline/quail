package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncUpper extends FuncType {

    public StringFuncUpper() {
        super("upper", Arrays.asList("str"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "string upper:invalid args size");
        Assert.require(a.get(0).v instanceof StringType, "string upper:invalid arg0 type");
        return new QValue(((StringType) a.get(0).v).value.toUpperCase());
    }

    @Override
    public QType copy() {
        return new StringFuncUpper();
    }
}
