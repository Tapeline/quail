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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "string size:invalid args size");
        Assert.require(QType.isStr(a.get(0)), "string size:invalid types");
        return new NumType(((StringType) a.get(0)).value.length());
    }
}
