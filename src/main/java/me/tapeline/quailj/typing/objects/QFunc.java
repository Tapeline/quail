package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.funcutils.AlternativeCall;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.utils.VariableTable;
import me.tapeline.quailj.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QFunc extends QObject {

    public static VariableTable defaults = new VariableTable();

    public String name;
    public List<FuncArgument> args = new ArrayList<>();
    public Node code;
    public Runtime boundRuntime = null;
    public boolean isStatic;
    public List<AlternativeCall> alternatives = new ArrayList<>();

    public QFunc(String name,
                 List<FuncArgument> args,
                 Node code,
                 Runtime boundRuntime,
                 List<AlternativeCall> alternatives,
                 boolean isStatic) {
        this.name = name;
        this.args = args;
        this.code = code;
        this.boundRuntime = boundRuntime;
        this.alternatives = alternatives;
        this.isStatic = isStatic;
    }

    public QObject call(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs)
            throws RuntimeStriker {
        if (boundRuntime == null)
            runtime = boundRuntime;
        if (runtime == null)
            throw new RuntimeException("No bound runtime! Stopping execution.");

        for (AlternativeCall alternativeCall : alternatives) {
            boolean match = true;
            int argsCount = Math.min(alternativeCall.args.size(), args.size());
            for (int i = 0; i < argsCount; i++) {
                if (!alternativeCall.args.get(0).matchesRequirements(runtime, args.get(i))) {
                    match = false;
                    break;
                }
            }
            if (match) {
                Memory enclosing = new Memory(runtime.memory);
                mapArguments(runtime, args, kwargs, enclosing);
                try {
                    runtime.run(alternativeCall.code, enclosing);
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

        Memory enclosing = new Memory(runtime.memory);
        mapArguments(runtime, args, kwargs, enclosing);
        try {
            runtime.run(code, enclosing);
        } catch (RuntimeStriker striker) {
            if (striker.type == RuntimeStriker.Type.RETURN) {
                return striker.returnValue;
            } else if (striker.type == RuntimeStriker.Type.EXCEPTION) {
                throw striker;
            }
        }
        return QObject.Val();
    }

    public void mapArguments(Runtime runtime, List<QObject> args,
                             HashMap<String, QObject> kwargs, Memory memory) throws RuntimeStriker {
        int argumentCount = this.args.size();
        int passedCount = args.size();
        for (int i = 0; i < argumentCount; i++) {
            FuncArgument thisArg = this.args.get(i);
            if (i >= passedCount) {
                if (thisArg.defaultValue == null)
                    memory.set(thisArg.name, QObject.Val(), thisArg.modifiers);
                else
                    memory.set(thisArg.name, runtime.run(
                            thisArg.defaultValue,
                            memory
                    ), thisArg.modifiers);
            }

            QObject thisPassed = args.get(i);
            if (thisArg.isArgsConsumer) {
                List<QObject> consumed = new ArrayList<>();
                for (int j = i; j < passedCount; j++)
                    consumed.add(thisPassed);
                memory.set(thisArg.name, QObject.Val(consumed), thisArg.modifiers);
                break;
            }

            if (VariableNode.match(thisArg.modifiers, runtime, thisPassed)) {
                memory.set(thisArg.name, thisPassed, thisArg.modifiers);
            } else {
                runtime.error("Argument mapping failed. Arg #" + (i + 1) +
                        " is not applicable for " + thisArg);
            }
        }

        memory.mem.putAll(kwargs);
    }

}
