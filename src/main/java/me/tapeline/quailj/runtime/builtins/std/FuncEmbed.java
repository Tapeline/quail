package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.libmanagement.Embed;
import me.tapeline.quailj.libmanagement.EmbedLoader;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FuncEmbed extends FuncType {

    public FuncEmbed() {
        super("embed", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        if (a.size() < 3)
            throw new RuntimeStriker("embed:invalid args length");
        Assert.require(a.get(0).v instanceof StringType, "embed:invalid arg0 type");
        Assert.require(a.get(1).v instanceof StringType, "embed:invalid arg1 type");
        Assert.require(a.get(2).v instanceof StringType, "embed:invalid arg2 type");
        File file = new File(((StringType) a.get(0).v).value);
        Embed embed = EmbedLoader.loadEmbed(file, ((StringType) a.get(1).v).value, ((StringType) a.get(2).v).value);
        if (embed == null) return new QValue(1);
        return new QValue(runtime.embedIntegrator.integrateEmbed(embed));
    }

    @Override
    public QType copy() {
        return new FuncEmbed();
    }
}
