package me.tapeline.quailj.runtime.builtins;

import me.tapeline.quailj.libmanagement.Embed;
import me.tapeline.quailj.libmanagement.EmbedLoader;
import me.tapeline.quailj.platformspecific.IOManager;
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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 3)
            throw new RuntimeStriker("embed:invalid args length");
        Assert.require(a.get(0) instanceof StringType, "embed:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "embed:invalid arg1 type");
        Assert.require(a.get(2) instanceof StringType, "embed:invalid arg2 type");
        File file = new File(((StringType) a.get(0)).value);
        Embed embed = EmbedLoader.loadEmbed(file, ((StringType) a.get(1)).value, ((StringType) a.get(2)).value);
        if (embed == null) return new NumType(1);
        return new NumType(runtime.embedIntegrator.integrateEmbed(embed));
    }

    @Override
    public QType copy() {
        return new FuncEmbed();
    }
}
