package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncCount extends FuncType {

    public StringFuncCount() {
        super("count", Arrays.asList("str", "needle"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "string count:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "string count:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "string count:invalid arg1 type");
        String str = ((StringType) a.get(0)).value;
        String findStr = ((StringType) a.get(1)).value;
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return QType.V(count);
    }

    @Override
    public QType copy() {
        return new StringFuncCount();
    }
}
