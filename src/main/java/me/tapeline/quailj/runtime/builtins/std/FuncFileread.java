package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.StringType;

import java.util.Collections;
import java.util.List;

public class FuncFileread extends FuncType {

    public FuncFileread() {
        super("fileread", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        if (a.size() < 1)
            throw new RuntimeStriker("fileread:invalid args length");
        return new QValue(IOManager.fileInput(a.get(0).v.toString()));
    }

    @Override
    public QType copy() {
        return new FuncFileread();
    }
}
