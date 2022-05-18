package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncBit extends FuncType {

    public FuncBit() {
        super("bit", Collections.singletonList(""), null);
    }

    public static boolean getBit(byte b, int position) {
        return ((b >> position) & 1) == 1;
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 2 || !QType.isNum(a.get(1)) || !(a.get(0) instanceof BinType))
            throw new RuntimeStriker("func bit:specify BinType NumType!");
        return QType.V(getBit(((BinType) a.get(0)).value, ((int) ((NumType) a.get(1)).value)));
    }

    @Override
    public QType copy() {
        return new FuncBit();
    }
}
