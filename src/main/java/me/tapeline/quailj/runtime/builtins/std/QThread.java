package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.HashMap;
import java.util.List;

public class QThread extends Thread {

    public FuncType runnable;
    public Runtime outer;
    public Runtime inner;
    public List<QType> args;
    public QType ret = QType.V();

    public QThread(Runtime r, FuncType f, List<QType> casted) {
        outer = r;
        runnable = f;
        inner = new Runtime(null, outer.io, outer.path);
        args = casted;
    }

    public void setThreadObject(QType value) {
        args.add(0, value);
    }

    public void goodNight(long t) {
        try {
            sleep(t);
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void run() {
        try {
            ret = runnable.run(inner, args);
        } catch (RuntimeStriker e) {
            e.printStackTrace();
            HashMap<String, QType> data = new HashMap<>();
            data.put("error", QType.V(true));
            data.put("message", e.val);
            ret = new ContainerType(data);
        }
    }
}
