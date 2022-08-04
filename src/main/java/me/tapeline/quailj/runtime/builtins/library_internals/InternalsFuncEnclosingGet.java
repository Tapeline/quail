package me.tapeline.quailj.runtime.builtins.library_internals;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Collections;
import java.util.List;

public class InternalsFuncEnclosingGet extends FuncType {

    public InternalsFuncEnclosingGet() {
        super("enclosingGet", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 1) throw new RuntimeStriker("internal func:too few args");
        return QType.nullSafe(runtime.scope.get(a.get(0).toString()));
    }

    @Override
    public QType copy() {
        return new InternalsFuncEnclosingGet();
    }
}
