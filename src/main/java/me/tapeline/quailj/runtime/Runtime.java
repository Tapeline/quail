package me.tapeline.quailj.runtime;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.tapeline.quailj.libmanagement.EmbedIntegrator;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.block.BlockNode;
import me.tapeline.quailj.parsing.nodes.branching.CatchClause;
import me.tapeline.quailj.parsing.nodes.branching.EventNode;
import me.tapeline.quailj.parsing.nodes.branching.IfNode;
import me.tapeline.quailj.parsing.nodes.branching.TryCatchNode;
import me.tapeline.quailj.parsing.nodes.effect.EffectNode;
import me.tapeline.quailj.parsing.nodes.effect.UseNode;
import me.tapeline.quailj.parsing.nodes.expression.CallNode;
import me.tapeline.quailj.parsing.nodes.generators.ContainerGeneratorNode;
import me.tapeline.quailj.parsing.nodes.generators.ListGeneratorNode;
import me.tapeline.quailj.parsing.nodes.literals.*;
import me.tapeline.quailj.parsing.nodes.loops.ForNode;
import me.tapeline.quailj.parsing.nodes.loops.LoopNode;
import me.tapeline.quailj.parsing.nodes.loops.ThroughNode;
import me.tapeline.quailj.parsing.nodes.loops.WhileNode;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.platforms.IOManager;
import me.tapeline.quailj.typing.modifiers.FinalModifier;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.modifiers.VariableModifier;
import me.tapeline.quailj.typing.objects.*;
import me.tapeline.quailj.typing.objects.funcutils.AlternativeCall;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.utils.VariableTable;
import me.tapeline.quailj.utils.ErrorFormatter;
import me.tapeline.quailj.utils.Pair;

import static me.tapeline.quailj.typing.objects.QObject.Val;

import java.util.*;

public class Runtime {

    private String sourceCode;
    private Node root;
    private boolean doProfile;
    private IOManager io;
    public Memory memory;
    private EmbedIntegrator embedIntegrator;
    public QObject superObject = new QObject("Object", null, new HashMap<>());

    private HashMap<String, List<Pair<QFunc, Boolean>>> eventsHandlerMap = new HashMap<>();

    private List<Node> runNodeStack = new ArrayList<>();

    public Runtime(String sourceCode, Node root, IOManager io, boolean doProfile) {
        this.sourceCode = sourceCode;
        this.root = root;
        this.doProfile = doProfile;
        this.io = io;
        this.memory = new Memory();
    }

    private void registerDefaults() {

    }

    public void error(Node node, String message) throws RuntimeStriker {
        throw new RuntimeStriker(
                ErrorFormatter.formatError(
                        sourceCode,
                        node.line - 1,
                        node.character,
                        node.length,
                        message
                )
        );
    }

    public void error(String message) throws RuntimeStriker {
        error(runNodeStack.get(runNodeStack.size() - 1), message);
    }

    private void begin(Node node) {
        node.executionStart = System.currentTimeMillis();
        runNodeStack.add(node);
    }

    private void end(Node node) {
        node.executionTime = System.currentTimeMillis() - node.executionStart;
        runNodeStack.remove(node);
    }

    public QObject run(Node node, Memory scope) throws RuntimeStriker {
        begin(node);

        if (node instanceof BlockNode) {
            BlockNode thisNode = (BlockNode) node;
            int blockSize = thisNode.nodes.size();
            for (int i = 0; i < blockSize; i++)
                run(thisNode.nodes.get(i), scope);
            end(node);
            return Val();
        } else if (node instanceof EventNode) {
            EventNode thisNode = (EventNode) node;
            String eventName = run(thisNode.event, scope).toString();
            QFunc func = new QFunc(
                    thisNode.funcName,
                    Arrays.asList(new FuncArgument(
                            thisNode.id,
                            FuncArgument.defaultNull,
                            new ArrayList<>(),
                            false,
                            false
                    )),
                    thisNode.code,
                    this,
                    new ArrayList<>(),
                    false
            );
            if (eventsHandlerMap.containsKey(eventName))
                eventsHandlerMap.put(eventName,
                        Arrays.asList(new Pair<>(func, true)));
            else
                eventsHandlerMap.get(eventName).add(new Pair<>(func, true));
            end(node);
            return Val();
        } else if (node instanceof IfNode) {
            IfNode thisNode = (IfNode) node;
            int conditions = thisNode.conditions.size();
            for (int i = 0; i < conditions; i++) {
                QObject condition = run(thisNode.conditions.get(i), scope);
                if (condition.isTrue()) {
                    run(thisNode.branches.get(i), scope);
                    end(node);
                    return Val();
                }
            }
            if (thisNode.elseBranch != null)
                run(thisNode.elseBranch, scope);
            end(node);
            return Val();
        } else if (node instanceof TryCatchNode) {
            TryCatchNode thisNode = (TryCatchNode) node;
            try {
                run(thisNode.tryCode, scope);
            } catch (RuntimeStriker striker) {
                if (striker.type == RuntimeStriker.Type.EXCEPTION) {
                    for (CatchClause clause : thisNode.catchClauses) {
                        QObject expectedException = run(clause.exceptionClass, scope);
                        if (striker.exception.instanceOf(expectedException)) {
                            Memory enclosing = new Memory(scope);
                            enclosing.set(clause.variable, striker.exception, new ArrayList<>());
                            run(clause.code, enclosing);
                            enclosing = null;
                        }
                    }
                } else {
                    end(node);
                    throw striker;
                }
            }
            end(node);
            return Val();
        } else if (node instanceof EffectNode) {
            EffectNode thisNode = (EffectNode) node;
            switch (thisNode.effect) {
                case EFFECT_ASSERT: {
                    if (!run(thisNode.value, scope).isTrue()) error("Assertion failed");
                    break;
                }
                case EFFECT_RETURN: {
                    throw new RuntimeStriker(run(thisNode.value, scope));
                }
                case EFFECT_THROW: {
                    QObject exception = run(thisNode.value, scope);
                    String representation = exception.typeString(this).toString();
                    throw new RuntimeStriker(exception, representation);
                }
                case EFFECT_STRIKE: {
                    QObject count = run(thisNode.value, scope);
                    if (!count.isNum()) error("Cannot strike. Not a number");
                    throw new RuntimeStriker((long) ((QNumber) count).value);
                }
                case EFFECT_IMPORT: {
                    // TODO: do import effect action
                    break;
                }
            }
            end(node);
            return Val();
        } else if (node instanceof UseNode) {
            UseNode thisNode = (UseNode) node;
            // TODO: do use effect action
            end(node);
            return Val();
        } else if (node instanceof CallNode) {
            CallNode thisNode = (CallNode) node;
            // TODO: do call action
            end(node);
            return Val();
        } else if (node instanceof ContainerGeneratorNode) {
            ContainerGeneratorNode thisNode = (ContainerGeneratorNode) node;
            // TODO: do container generator
            end(node);
            return Val();
        } else if (node instanceof ListGeneratorNode) {
            ListGeneratorNode thisNode = (ListGeneratorNode) node;
            // TODO: do list generator
            end(node);
            return Val();
        } else if (node instanceof LiteralBool) {
            end(node);
            return Val(((LiteralBool) node).value);
        } else if (node instanceof LiteralNum) {
            end(node);
            return Val(((LiteralNum) node).value);
        } else if (node instanceof LiteralNull) {
            end(node);
            return Val();
        } else if (node instanceof LiteralString) {
            end(node);
            return Val(((LiteralString) node).value);
        } else if (node instanceof LiteralList) {
            LiteralList thisNode = (LiteralList) node;
            List<QObject> values = new ArrayList<>();
            int objectCount = thisNode.value.size();
            for (int i = 0; i < objectCount; i++)
                values.add(run(thisNode.value.get(i), scope));
            end(node);
            return Val(values);
        } else if (node instanceof LiteralContainer) {
            LiteralContainer thisNode = (LiteralContainer) node;
            QObject container = new QObject(new HashMap<>());
            int pairCount = thisNode.contents.getKeys().size();
            for (int i = 0; i < pairCount; i++) {
                String key = run(thisNode.contents.getKeys().get(i), scope).toString();
                QObject value = run(thisNode.contents.getValueMap().get(i), scope);
                container.set(key, value);
            }
            end(node);
            return container;
        } else if (node instanceof LiteralFunction) {
            LiteralFunction thisNode = (LiteralFunction) node;
            if (scope.get(thisNode.name).isFunc()) {
                QFunc superFunc = ((QFunc) scope.get(thisNode.name));
                superFunc.alternatives.add(new AlternativeCall(
                        thisNode.args,
                        thisNode.code
                ));
            } else {
                QFunc func = new QFunc(
                        thisNode.name,
                        thisNode.args,
                        thisNode.code,
                        this,
                        new ArrayList<>(),
                        thisNode.isStatic
                );
                scope.set(func.name, func, new ArrayList<>());
                return func;
            }
            end(node);
            return Val();
        } else if (node instanceof LiteralClass) {
            LiteralClass thisNode = (LiteralClass) node;
            QObject parent = thisNode.like == null? null : run(thisNode.like, scope);
            if (parent != null && parent.isNull())
                error("Attempt to inherit from null. Maybe the class isn't defined at the moment");
            else if (parent != null && !parent.isPrototype())
                error("Attempt to inherit from a non-class object");
            VariableTable contents = new VariableTable();
            if (parent != null)
                contents.putAll(parent.getTable());
            thisNode.methods.forEach((methodName, method) -> {
                if (contents.get(methodName).isFunc()) {
                    QFunc superFunc = ((QFunc) contents.get(methodName));
                    superFunc.alternatives.add(new AlternativeCall(
                            method.args,
                            method.code
                    ));
                } else {
                    contents.put(methodName, new QFunc(
                            methodName,
                            method.args,
                            method.code,
                            this,
                            new ArrayList<>(),
                            method.isStatic
                    ));
                }
            });
            for (Map.Entry<VariableNode, Node> entry : thisNode.contents.entrySet())
                contents.put(entry.getKey().id, run(entry.getValue(), scope),
                         entry.getKey().modifiers);
            int items = thisNode.initialize.size();
            for (int i = 0; i < items; i++)
                run(thisNode.initialize.get(i), scope);
            QObject qClass = new QObject(thisNode.name, superObject, contents);
            qClass.setPrototypeFlag(true);
            end(node);
            return qClass;
        } else if (node instanceof ForNode) {
            // TODO: add strike support
            ForNode thisNode = (ForNode) node;
            QObject iterable = run(thisNode.iterable, scope);
            int unpackingSize = thisNode.iterator.size();
            Memory enclosing = new Memory(scope);
            while (true) {
                try {
                    QObject next = iterable.iterateNext(this);
                    if (unpackingSize == 1)
                        enclosing.set(thisNode.iterator.get(0), next, new ArrayList<>());
                    else if (next.isList()) {
                        QList list = (QList) next;
                        if (list.values.size() != unpackingSize)
                            error("Unpacking failed. List size = " +
                                    list.values.size() + "; Iterators = " + unpackingSize);
                        for (int i = 0; i < unpackingSize; i++)
                            enclosing.set(thisNode.iterator.get(i), list.values.get(i), new ArrayList<>());
                    } else if (unpackingSize == 2) {
                        String keyVar = thisNode.iterator.get(0);
                        String valVar = thisNode.iterator.get(1);
                        next.getTable().forEach((key, value) -> {
                            enclosing.set(keyVar, Val(key), new ArrayList<>());
                            enclosing.set(valVar, value, new ArrayList<>());
                        });
                    } else error("Iterator unpacking error. Unknown error");

                    run(thisNode.code, enclosing);
                } catch (RuntimeStriker striker) {
                    if (striker.type == RuntimeStriker.Type.BREAK ||
                        striker.type == RuntimeStriker.Type.STOP_ITERATION)
                        break;
                    else if (striker.type == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            end(node);
            return Val();
        } else if (node instanceof LoopNode) {
            LoopNode thisNode = (LoopNode) node;
            // TODO: add strike support
            while (true) {
                try {
                    run(thisNode.code, scope);

                    QObject condition = run(thisNode.condition, scope);
                    if (condition.isTrue()) break;
                } catch (RuntimeStriker striker) {
                    if (striker.type == RuntimeStriker.Type.BREAK)
                        break;
                    else if (striker.type == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            end(node);
            return Val();
        } else if (node instanceof WhileNode) {
            WhileNode thisNode = (WhileNode) node;
            // TODO: add strike support
            while (true) {
                try {
                    QObject condition = run(thisNode.condition, scope);
                    if (!condition.isTrue()) break;

                    run(thisNode.code, scope);
                } catch (RuntimeStriker striker) {
                    if (striker.type == RuntimeStriker.Type.BREAK)
                        break;
                    else if (striker.type == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            end(node);
            return Val();
        } else if (node instanceof ThroughNode) {
            ThroughNode thisNode = (ThroughNode) node;

        }
        return Val();
    }

}
