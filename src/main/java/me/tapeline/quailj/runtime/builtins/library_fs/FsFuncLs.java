package me.tapeline.quailj.runtime.builtins.library_fs;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.ListType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FsFuncLs extends FuncType {

    public FsFuncLs() {
        super("ls", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1)
            throw new RuntimeStriker("fs ls:invalid args length");
        List<QType> l = new ArrayList<>();
        String[] list = new File(a.get(0).toString()).list();
        list = list == null? new String[] {} : list;
        for (String s : list) l.add(QType.V(s));
        return new ListType(l);
    }

    @Override
    public QType copy() {
        return new FsFuncLs();
    }
}
