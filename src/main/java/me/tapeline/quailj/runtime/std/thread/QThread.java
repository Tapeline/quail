package me.tapeline.quailj.runtime.std.thread;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;
import java.util.List;

public class QThread extends QObject {

    public static QObject prototype = null;

    public static QObject definition() {
        if (QThread.prototype == null) {
            System.out.println("Thread class defined");
            QThread.prototype = new QObject("Thread", null, new HashMap<>());
            //prototype.set();
        }
        return QThread.prototype;
    }

    public QThreadWorker worker;

    public QThread(Runtime runtime, QFunc func, List<QObject> args) {
        table.putAll(Runtime.superObject.table);
        table.putAll(definition().table);
        definition().derivedObjects.add(this);
        setObjectMetadata(definition());

        worker = new QThreadWorker(runtime, func, args);
        worker.setThreadObject(this);
    }

}
