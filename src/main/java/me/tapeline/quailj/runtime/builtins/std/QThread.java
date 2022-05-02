package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;

import java.util.HashMap;
import java.util.List;

public class QThread extends Thread {

    public FuncType runnable;
    public Runtime outer;
    public Runtime inner;
    public List<QValue> args;
    public QValue ret = new QValue();

    public QThread(Runtime r, FuncType f, List<QValue> casted) {
        outer = r;
        runnable = f;
        inner = new Runtime(null, outer.io, outer.path);
        args = casted;
    }

    @Override
    public void run() {
        try {
            ret = runnable.run(inner, args);
        } catch (RuntimeStriker e) {
            HashMap<String, QValue> data = new HashMap<>();
            data.put("error", new QValue(true));
            data.put("message", e.val);
            ret = new QValue(new ContainerType(data));
        }
    }
}
