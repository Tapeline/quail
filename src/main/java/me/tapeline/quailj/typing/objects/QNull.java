package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;

public class QNull extends QObject {

    public QNull() {
        table.putAll(Runtime.superObject.table);
        table.putAll(Runtime.nullPrototype.table);
        Runtime.nullPrototype.derivedObjects.add(this);
        setObjectMetadata(Runtime.nullPrototype);
    }

    public String toString() {
        return "null";
    }

}
