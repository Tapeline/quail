package me.tapeline.quailj.runtime.builtins.library_fs;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FsFuncExists extends FuncType {

    public FsFuncExists() {
        super("exists", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1)
            throw new RuntimeStriker("fs exists:invalid args length");
        return QType.V(new File(a.get(0).toString()).exists());
    }

    @Override
    public QType copy() {
        return new FsFuncExists();
    }
}
