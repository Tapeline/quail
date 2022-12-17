package me.tapeline.quailj.runtime.std.thread;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

import java.util.HashMap;
import java.util.List;

public class QThreadWorker extends Thread {

    public QFunc runnable;
    public Runtime outer;
    public Runtime inner;
    public List<QObject> args;
    public QObject ret = QObject.Val();
    public QThread threadObject = null;

    public QThreadWorker(Runtime r, QFunc f, List<QObject> casted) {
        outer = r;
        runnable = f;
        inner = new Runtime(r.scriptHome, null, outer.io, new String[] {}, false);
        args = casted;
    }

    public void setThreadObject(QThread value) {
        threadObject = value;
    }

    public void sleepFor(long t) {
        try {
            sleep(t);
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void run() {
        try {
            args.add(0, QObject.nullSafe(threadObject));
            ret = runnable.call(inner, args);
        } catch (RuntimeStriker e) {
            e.printStackTrace();
            HashMap<String, QObject> data = new HashMap<>();
            data.put("error", QObject.Val(true));
            data.put("message", QObject.Val(e.error.toString()));
            ret = QObject.Val(data);
        }
    }

}
