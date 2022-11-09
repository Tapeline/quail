package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.utils.VariableTable;

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



    public String toString() {
        return value? "true" : "false";
    }

}
