package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncIsalpha extends FuncType {

    public StringFuncIsalpha() {
        super("isalpha", Arrays.asList("str"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "string isalpha:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "string isalpha:invalid arg0 type");
        String sv = ((StringType) a.get(0)).value;
        for (char c : sv.toCharArray())
            if (!Character.isLetter(c)) return QType.V(false);
        return QType.V(true);
    }

    @Override
    public QType copy() {
        return new StringFuncIsalpha();
    }
}
