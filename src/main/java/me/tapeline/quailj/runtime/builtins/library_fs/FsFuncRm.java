package me.tapeline.quailj.runtime.builtins.library_fs;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class FsFuncRm extends FuncType {

    public FsFuncRm() {
        super("rm", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1)
            throw new RuntimeStriker("fs rm:invalid args length");
        return QType.V(new File(a.get(0).toString()).delete());
    }

    @Override
    public QType copy() {
        return new FsFuncRm();
    }
}
