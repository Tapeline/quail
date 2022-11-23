package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.javaadapter.AdapterFuncJavaCall;
import me.tapeline.quailj.runtime.std.thread.*;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

import java.util.HashMap;
import java.util.List;

public class QJavaAdapter<T> extends QObject {

    public static QObject prototype = null;

    public static QObject definition(Runtime runtime) {
        if (QJavaAdapter.prototype == null) {
            QJavaAdapter.prototype = new QObject("JavaAdapter", null, new HashMap<>());
            QJavaAdapter.prototype.setPrototypeFlag(true);
            QJavaAdapter.prototype.setSafeSuperClass(Runtime.superObject);
            QJavaAdapter.prototype.set("javaCall", new AdapterFuncJavaCall(runtime));
        }
        return QJavaAdapter.prototype;
    }

    public T object;

    public QJavaAdapter(Runtime runtime, T object) {
        table.putAll(Runtime.superObject.table);
        table.putAll(definition(runtime).table);
        definition(runtime).derivedObjects.add(this);
        setObjectMetadata(definition(runtime));

        this.object = object;
    }

}
