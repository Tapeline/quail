package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncBinfilewrite extends FuncType {

    public FuncBinfilewrite() {
        super("binfileread", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 2 || !(a.get(1) instanceof ListType))
            throw new RuntimeStriker("binfilewrite:invalid args");
        IOManager.fileBinSet(a.get(0).toString(), ((ListType) a.get(1)).values);
        return new VoidType();
    }

    @Override
    public QType copy() {
        return new FuncBinfilewrite();
    }
}
