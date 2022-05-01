package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class StringFuncSize extends FuncType {

    public StringFuncSize() {
        super("size", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "string size:invalid args size");
        Assert.require(QType.isStr(a.get(0).v), "string size:invalid types");
        return new QValue(((StringType) a.get(0).v).value.length());
    }

    @Override
    public QType copy() {
        return new StringFuncSize();
    }
}
