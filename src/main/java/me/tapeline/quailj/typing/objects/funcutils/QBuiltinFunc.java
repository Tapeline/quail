package me.tapeline.quailj.typing.objects.funcutils;

import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

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
        for (int i = 0; i < Math.min(this.args.size(), args.size()); i++) {
            FuncArgument arg = this.args.get(i);
            if (arg.isArgsConsumer)
                enclosing.set(arg.name, Val(args.subList(i, args.size())), arg.modifiers);
            else {
                if (!arg.matchesRequirements(runtime, args.get(i)))
                    runtime.error("Argument mapping failed for arg #" + (i + 1) + ".\n" + "" +
                            "Inapplicable for " + arg.modifiers.toString());
                enclosing.set(arg.name, args.get(i), arg.modifiers);
            }
        }
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
