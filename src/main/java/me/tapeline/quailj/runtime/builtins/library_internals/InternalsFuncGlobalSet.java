package me.tapeline.quailj.runtime.builtins.library_internals;

import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Collections;
import java.util.List;

public class InternalsFuncGlobalSet extends FuncType {

    public InternalsFuncGlobalSet() {
        super("globalSet", Collections.singletonList(""), null);
    }

    public Memory resolveTopmost(Memory mem) {
        if (mem.enclosing != null)
            return resolveTopmost(mem.enclosing);
        return mem;
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        if (a.size() < 2) throw new RuntimeStriker("internal func:too few args");
        Memory topMost = resolveTopmost(runtime.scope);
        topMost.set(runtime, a.get(0).toString(), a.get(1));
        return QType.V();
    }

    @Override
    public QType copy() {
        return new InternalsFuncGlobalSet();
    }
}
