package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncFileexists extends FuncType {

    public FuncFileexists() {
        super("fileexists", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1)
            throw new RuntimeStriker("fileexists:invalid args length");
        return QType.V(IOManager.fileExists(a.get(0).toString()));
    }

    @Override
    public QType copy() {
        return new FuncFileexists();
    }
}
