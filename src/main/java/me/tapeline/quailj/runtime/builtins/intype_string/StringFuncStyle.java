package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.types.StringType;
import me.tapeline.quailj.utils.AnsiStringBuilder;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncStyle extends FuncType {

    public StringFuncStyle() {
        super("style", Arrays.asList("str", "c"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "string color:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "string color:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "string color:invalid arg1 type");
        try {
            AnsiStringBuilder ansiStringBuilder = new AnsiStringBuilder();
            switch (a.get(1).toString()) {
                case "blink": ansiStringBuilder = ansiStringBuilder.blink(); break;
                case "bold": ansiStringBuilder = ansiStringBuilder.bold(); break;
                case "dim": ansiStringBuilder = ansiStringBuilder.dim(); break;
                case "invert": ansiStringBuilder = ansiStringBuilder.invertColor(); break;
                case "italic": ansiStringBuilder = ansiStringBuilder.italic(); break;
                case "strike": ansiStringBuilder = ansiStringBuilder.strikeThrough(); break;
                case "under": ansiStringBuilder = ansiStringBuilder.underline(); break;
            }
            return QType.V(ansiStringBuilder.append(a.get(0)).toString());
        } catch (Exception e) {
            throw new RuntimeStriker(e.getMessage());
        }
    }

    @Override
    public QType copy() {
        return new StringFuncStyle();
    }
}
