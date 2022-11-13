package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.AlternativeCall;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;

import java.util.ArrayList;
import java.util.List;

public class QFunc extends QObject {
    public String name;
    public List<FuncArgument> args = new ArrayList<>();
    public Node code;
    public Runtime boundRuntime = null;
    public boolean isStatic;
    public List<AlternativeCall> alternatives = new ArrayList<>();
    private Memory closure;

    public QFunc(String name,
                 List<FuncArgument> args,
                 Node code,
                 Runtime boundRuntime,
                 Memory closure,
                 List<AlternativeCall> alternatives,
                 boolean isStatic) {
        this.name = name;
        this.args = args;
        this.code = code;
        this.boundRuntime = boundRuntime;
        this.alternatives = alternatives;
        this.isStatic = isStatic;
        this.closure = closure;
        table.putAll(Runtime.superObject.table);
        table.putAll(Runtime.funcPrototype.table);
        Runtime.funcPrototype.derivedObjects.add(this);
        setObjectMetadata(Runtime.funcPrototype);
    }

    public QObject call(Runtime runtime, List<QObject> args) throws RuntimeStriker {
        if (boundRuntime == null)
            runtime = boundRuntime;
        if (runtime == null)
            throw new RuntimeException("No bound runtime! Stopping execution.");
        Memory enclosing = new Memory(closure);
        int argsSize = this.args.size();

        if (alternatives.size() > 0)
            for (AlternativeCall alternativeCall : alternatives) {
                try {
                    for (int i = 0; i < argsSize; i++) {
                        FuncArgument arg = this.args.get(i);
                        if (i >= args.size()) {
                            QObject preparedNull = QObject.Val();
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
                } catch (RuntimeStriker striker) {
                    continue;
                }
                try {
                    runtime.run(alternativeCall.code, enclosing);
                    if (name.equals("_constructor") && args.size() > 0)
                        return args.get(0);
                } catch (RuntimeStriker striker) {
                    if (striker.type == RuntimeStriker.Type.RETURN) {
                        return striker.returnValue;
                    } else if (striker.type == RuntimeStriker.Type.EXCEPTION) {
                        throw striker;
                    }
                }
                return QObject.Val();
            }

        for (int i = 0; i < argsSize; i++) {
            FuncArgument arg = this.args.get(i);
            if (i >= args.size()) {
                QObject preparedNull = QObject.Val();
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
            runtime.run(code, enclosing);
            if (name.equals("_constructor") && args.size() > 0)
                return args.get(0);
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
