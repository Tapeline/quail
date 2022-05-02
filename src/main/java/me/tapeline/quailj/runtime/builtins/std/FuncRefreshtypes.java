package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.Collections;
import java.util.List;

public class FuncRefreshtypes extends FuncType {

    public FuncRefreshtypes() {
        super("refreshtypes", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        NumType.tableToClone.putAll(runtime.scope.get("Number").v.table);
        BoolType.tableToClone.putAll(runtime.scope.get("Bool").v.table);
        VoidType.tableToClone.putAll(runtime.scope.get("Null").v.table);
        StringType.tableToClone.putAll(runtime.scope.get("String").v.table);
        ListType.tableToClone.putAll(runtime.scope.get("List").v.table);
        return new QValue();
    }

    @Override
    public QType copy() {
        return new FuncRefreshtypes();
    }
}
