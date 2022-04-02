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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "string get:invalid args size");
        Assert.require(QType.isStr(a.get(0)) && QType.isNum(a.get(1)), "string get:invalid types");
        return new StringType("" + ((StringType) a.get(0)).value.charAt((int) ((NumType) a.get(1)).value));
    }
}
