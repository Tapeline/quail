package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.libmanagement.Embed;
import me.tapeline.quailj.libmanagement.EmbedLoader;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.types.StringType;
import me.tapeline.quailj.utils.Assert;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FuncUsejar extends FuncType {

    public FuncUsejar() {
        super("usejar", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1)
            throw new RuntimeStriker("usejar:invalid args length");
        File file = new File(a.get(0).toString());
        Embed embed;
        try {
            embed = EmbedLoader.loadFromJar(file, a.size() > 1 ? a.get(1).toString() : "%");
        } catch (Exception e) { return QType.V(1); }
        if (embed == null) return QType.V(1);
        return QType.V(runtime.embedIntegrator.integrateEmbed(embed));
    }

    @Override
    public QType copy() {
        return new FuncUsejar();
    }
}
