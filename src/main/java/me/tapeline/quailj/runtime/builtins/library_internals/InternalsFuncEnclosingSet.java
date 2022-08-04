package me.tapeline.quailj.runtime.builtins.library_internals;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class InternalsFuncEnclosingSet extends FuncType {

    public InternalsFuncEnclosingSet() {
        super("enclosingSet", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 2) throw new RuntimeStriker("internal func:too few args");
        runtime.scope.set(runtime, a.get(0).toString(), a.get(1));
        return QType.V();
    }

    @Override
    public QType copy() {
        return new InternalsFuncEnclosingSet();
    }
}
