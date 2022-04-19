package me.tapeline.quailj.runtime.builtins;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncRefreshtypes extends FuncType {

    public FuncRefreshtypes() {
        super("refreshtypes", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        NumType.tableToClone.putAll(runtime.scope.get("Number").table);
        BoolType.tableToClone.putAll(runtime.scope.get("Bool").table);
        VoidType.tableToClone.putAll(runtime.scope.get("Null").table);
        StringType.tableToClone.putAll(runtime.scope.get("String").table);
        ListType.tableToClone.putAll(runtime.scope.get("List").table);
        return new VoidType();
    }

    @Override
    public QType copy() {
        return new FuncRefreshtypes();
    }
}
