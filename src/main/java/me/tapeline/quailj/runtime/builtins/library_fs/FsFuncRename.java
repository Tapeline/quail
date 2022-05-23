package me.tapeline.quailj.runtime.builtins.library_fs;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FsFuncRename extends FuncType {

    public FsFuncRename() {
        super("rename", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 2)
            throw new RuntimeStriker("fs rename:invalid args length");
        return QType.V(new File(a.get(0).toString()).renameTo(new File(a.get(1).toString())));
    }

    @Override
    public QType copy() {
        return new FsFuncRename();
    }
}
