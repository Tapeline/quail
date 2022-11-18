package me.tapeline.quailj.typing.objects.funcutils;

import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class QBuiltinFunc extends QFunc {


    public QBuiltinFunc(String name,
                        List<FuncArgument> args,
                        Runtime boundRuntime,
                        Memory closure,
                        boolean isStatic) {
        super(name, args, null, boundRuntime, closure, new ArrayList<>(), isStatic);
    }

    public abstract QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker;

    @Override
    public QObject call(Runtime runtime, List<QObject> args)
            throws RuntimeStriker {
        if (boundRuntime == null)
            runtime = boundRuntime;
        if (runtime == null)
            throw new RuntimeException("No bound runtime! Stopping execution.");
        Memory enclosing = new Memory(runtime.memory);
        int argsSize = this.args.size();
        for (int i = 0; i < argsSize; i++) {
            FuncArgument arg = this.args.get(i);
            if (i >= args.size()) {
                QObject preparedNull = runtime.run(arg.defaultValue, enclosing);
                if (!arg.matchesRequirements(runtime, preparedNull))
                    Runtime.error("Argument mapping failed for (not provided => null) arg #" +
                            (i + 1) + ".\n" + "" +
                            "Inapplicable for " + arg.modifiers.toString());
                enclosing.set(arg.name, preparedNull, arg.modifiers);
            } else {
                if (arg.isArgsConsumer)
                    enclosing.set(arg.name, Val(args.subList(i, args.size())), arg.modifiers);
                else {
                    if (!arg.matchesRequirements(runtime, args.get(i)))
                        Runtime.error("Argument mapping failed for arg #" + (i + 1) + ".\n" + "" +
                                "Inapplicable for " + arg.modifiers.toString());
                    enclosing.set(arg.name, args.get(i), arg.modifiers);
                }
            }
        }
        try {
            return action(runtime, enclosing.mem.getValues());
        } catch (RuntimeStriker striker) {
            if (striker.type == RuntimeStriker.Type.RETURN) {
                return striker.returnValue;
            } else if (striker.type == RuntimeStriker.Type.EXCEPTION || 
                        striker.type == RuntimeStriker.Type.EXIT) {
                throw striker;
            }
        }
        return QObject.Val();
    }

    public QObject copy(Runtime runtime) throws RuntimeStriker {
        try {
            QBuiltinFunc copy = this.getClass().getConstructor(Class.forName(
                    "me.tapeline.quailj.runtime.Runtime")).newInstance(runtime);
            copy.getTable().putAll(table);
            return copy;
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public QObject clone(Runtime runtime) throws RuntimeStriker {
        try {
            QBuiltinFunc cloned = this.getClass().getConstructor(Class.forName(
                    "me.tapeline.quailj.runtime.Runtime")).newInstance(runtime);
            table.forEach((k, v) -> {
                try {
                    cloned.getTable().put(
                            k,
                            v.clone(runtime),
                            table.getModifiersFor(k)
                    );
                } catch (RuntimeStriker striker) {
                    throw new RuntimeException(striker);
                }
            });
            return cloned;
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
