package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.utils.VariableTable;

import java.util.ArrayList;
import java.util.List;

public class QBool extends QObject {

    public boolean value;

    public QBool(boolean value) {
        this.value = value;
        table.putAll(Runtime.superObject.table);
        table.putAll(Runtime.boolPrototype.table);
        Runtime.boolPrototype.derivedObjects.add(this);
        setObjectMetadata(Runtime.boolPrototype);
    }

    public QObject not(Runtime runtime) {
        return Val(!value);
    }

    public QObject minus(Runtime runtime) {
        return Val(!value);
    }

    public QObject and(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isBool())
            return Val(value && ((QBool) other).value);
        return super.and(runtime, other);
    }

    public QObject or(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isBool())
            return Val(value || ((QBool) other).value);
        return super.and(runtime, other);
    }

    @Override
    public QObject copy(Runtime runtime) {
        QObject copy = QObject.Val(value);
        copy.getTable().putAll(table);
        return copy;
    }

    @Override
    public QObject clone(Runtime runtime) {
        QObject cloned = QObject.Val(value);
        table.forEach((k, v) -> cloned.getTable().put(
                k,
                v.clone(runtime),
                table.getModifiersFor(k)
        ));
        return cloned;
    }

    public String toString() {
        return value? "true" : "false";
    }

}
