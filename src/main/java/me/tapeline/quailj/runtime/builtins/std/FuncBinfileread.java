package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Collections;
import java.util.List;

public class FuncBinfileread extends FuncType {

    public FuncBinfileread() {
        super("binfileread", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1)
            throw new RuntimeStriker("fileread:invalid args length");
        return IOManager.fileBinInput(a.get(0).toString());
    }

    @Override
    public QType copy() {
        return new FuncBinfileread();
    }
}
