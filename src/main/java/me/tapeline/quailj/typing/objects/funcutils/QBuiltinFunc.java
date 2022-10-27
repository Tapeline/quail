package me.tapeline.quailj.typing.objects.funcutils;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.RuntimeStriker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class QBuiltinFunc extends QFunc {


    public QBuiltinFunc(String name,
                        List<FuncArgument> args,
                        Runtime boundRuntime,
                        boolean isStatic) {
        super(name, args, null, boundRuntime, new ArrayList<>(), isStatic);
    }

    public abstract QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker;

    @Override
    public QObject call(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs)
            throws RuntimeStriker {
        if (boundRuntime == null)
            runtime = boundRuntime;
        if (runtime == null)
            throw new RuntimeException("No bound runtime! Stopping execution.");
        Memory enclosing = new Memory(runtime.memory);
        mapArguments(runtime, args, kwargs, enclosing);
        try {
            return action(runtime, enclosing.mem.getValues());
        } catch (RuntimeStriker striker) {
            if (striker.type == RuntimeStriker.Type.RETURN) {
                return striker.returnValue;
            } else if (striker.type == RuntimeStriker.Type.EXCEPTION) {
                throw striker;
            }
        }
        return QObject.Val();
    }
}
