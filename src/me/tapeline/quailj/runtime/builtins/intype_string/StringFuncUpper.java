package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringFuncUpper extends FuncType {

    public StringFuncUpper() {
        super("upper", Arrays.asList("str"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "string upper:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "string upper:invalid arg0 type");
        return new StringType(((StringType) a.get(0)).value.toUpperCase());
    }
}
