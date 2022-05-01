package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class StringFuncGet extends FuncType {

    public StringFuncGet() {
        super("get", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "string get:invalid args size");
        Assert.require(QType.isStr(a.get(0).v) && QType.isNum(a.get(1).v), "string get:invalid types");
        return new QValue("" + ((StringType) a.get(0).v).value.charAt((int) ((NumType) a.get(1).v).value));
    }

    @Override
    public QType copy() {
        return new StringFuncGet();
    }
}
