package me.tapeline.quailj.runtime.std.thread;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

import java.util.HashMap;
import java.util.List;

public class QThread extends QObject {

    public static QObject prototype = null;

    public static QObject definition(Runtime runtime) {
        if (QThread.prototype == null) {
            QThread.prototype = new QObject("Thread", null, new HashMap<>());
            QThread.prototype.setPrototypeFlag(true);
            QThread.prototype.setSafeSuperClass(Runtime.superObject);
            QThread.prototype.isInheritable = false;
            QThread.prototype.set("_constructor", new ThreadConstructor(runtime));
            QThread.prototype.set("start",      new ThreadFuncStart(runtime));
            QThread.prototype.set("isAlive",    new ThreadFuncIsAlive(runtime));
            QThread.prototype.set("result",     new ThreadFuncResult(runtime));
            QThread.prototype.set("sleep",      new ThreadFuncSleep(runtime));
            QThread.prototype.set("wait",       new ThreadFuncWait(runtime));
            QThread.prototype.set("wake",       new ThreadFuncWake(runtime));
        }
        return QThread.prototype;
    }

    public QThreadWorker worker;

    public QThread(Runtime runtime, QFunc func, List<QObject> args) throws RuntimeStriker  {
        table.putAll(Runtime.superObject.table);
        table.putAll(definition(runtime).table);
        definition(runtime).derivedObjects.add(this);
        setObjectMetadata(definition(runtime));

        worker = new QThreadWorker(runtime, func, args);
        worker.setThreadObject(this);
    }

}
