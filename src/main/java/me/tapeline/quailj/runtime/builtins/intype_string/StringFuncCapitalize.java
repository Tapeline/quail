package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncCapitalize extends FuncType {

    public StringFuncCapitalize() {
        super("capitalize", Arrays.asList("str"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "string capitalize:invalid args size");
        Assert.require(a.get(0).v instanceof StringType, "string capitalize:invalid arg0 type");
        return new QValue(((StringType) a.get(0).v).value.substring(0, 1).toUpperCase() +
                ((StringType) a.get(0).v).value.substring(1).toLowerCase());
    }

    @Override
    public QType copy() {
        return new StringFuncCapitalize();
    }
}
