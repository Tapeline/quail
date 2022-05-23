package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncBitset extends FuncType {

    public FuncBitset() {
        super("bitset", Collections.singletonList(""), null);
    }

    public static boolean getBit(byte b, int position) {
        return ((b >> position) & 1) == 1;
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 2 || !QType.isNum(a.get(1)) || !QType.isBool(a.get(2))  || !(a.get(0) instanceof BinType))
            throw new RuntimeStriker("func bit:specify BinType NumType BoolType!");
        if (((BoolType) a.get(2)).value) {
            ((BinType) a.get(0)).value = (byte) (((BinType) a.get(0)).value | (1 << ((int) ((NumType) a.get(1)).value)));
            ((BinType) a.get(0)).value |= 1 << ((int) ((NumType) a.get(1)).value);
        } else {
            ((BinType) a.get(0)).value = (byte) (((BinType) a.get(0)).value & ~(1 << ((int) ((NumType) a.get(1)).value)));
            ((BinType) a.get(0)).value &= ~(1 << ((int) ((NumType) a.get(1)).value));
        }
        return new VoidType();
    }

    @Override
    public QType copy() {
        return new FuncBitset();
    }
}
