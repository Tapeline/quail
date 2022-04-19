package me.tapeline.quailj.runtime.builtins;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FuncInput extends FuncType {

    public FuncInput() {
        super("input", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() > 0) runtime.io.consolePut(QType.nullSafe(a.get(0)).toString());
        return new StringType(runtime.io.consoleInput(a.size() > 0? QType.nullSafe(a.get(0)).toString() : ""));
    }

    @Override
    public QType copy() {
        return new FuncInput();
    }
}
