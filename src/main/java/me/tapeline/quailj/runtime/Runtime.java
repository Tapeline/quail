package me.tapeline.quailj.runtime;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.libmanagement.EmbedIntegrator;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.block.BlockNode;
import me.tapeline.quailj.parsing.nodes.block.JavaEmbedNode;
import me.tapeline.quailj.parsing.nodes.branching.CatchClause;
import me.tapeline.quailj.parsing.nodes.branching.EventNode;
import me.tapeline.quailj.parsing.nodes.branching.IfNode;
import me.tapeline.quailj.parsing.nodes.branching.TryCatchNode;
import me.tapeline.quailj.parsing.nodes.effect.EffectNode;
import me.tapeline.quailj.parsing.nodes.effect.InstructionNode;
import me.tapeline.quailj.parsing.nodes.effect.UseNode;
import me.tapeline.quailj.parsing.nodes.expression.CallNode;
import me.tapeline.quailj.parsing.nodes.generators.ContainerGeneratorNode;
import me.tapeline.quailj.parsing.nodes.generators.ListGeneratorNode;
import me.tapeline.quailj.parsing.nodes.literals.*;
import me.tapeline.quailj.parsing.nodes.loops.ForNode;
import me.tapeline.quailj.parsing.nodes.loops.LoopNode;
import me.tapeline.quailj.parsing.nodes.loops.ThroughNode;
import me.tapeline.quailj.parsing.nodes.loops.WhileNode;
import me.tapeline.quailj.parsing.nodes.modifiers.AsyncFlagNode;
import me.tapeline.quailj.parsing.nodes.modifiers.TypeCastNode;
import me.tapeline.quailj.parsing.nodes.operators.*;
import me.tapeline.quailj.parsing.nodes.sequence.TupleNode;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.platforms.IOManager;
import me.tapeline.quailj.runtime.libraries.LibraryLoader;
import me.tapeline.quailj.runtime.libraries.LibraryRegistry;
import me.tapeline.quailj.runtime.std.*;
import me.tapeline.quailj.runtime.std.number.*;
import me.tapeline.quailj.runtime.std.string.*;
import me.tapeline.quailj.runtime.utils.JavaAction;
import me.tapeline.quailj.typing.modifiers.FinalModifier;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.*;
import me.tapeline.quailj.typing.objects.errors.Error;
import me.tapeline.quailj.typing.objects.errors.ErrorMessage;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.AlternativeCall;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.VariableTable;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.ErrorFormatter;
import me.tapeline.quailj.utils.Pair;

import static me.tapeline.quailj.typing.objects.QObject.Val;

import java.util.*;

public class Runtime {

    private String sourceCode;
    private Node root;
    private boolean doProfile;
    public IOManager io;
    public Memory memory;
    private EmbedIntegrator embedIntegrator;
    public static LibraryRegistry libraryRegistry = new LibraryRegistry();
    public List<AsyncRuntimeWorker> asyncRuntimeWorkers = new ArrayList<>();

    private HashMap<String, List<Pair<QFunc, Boolean>>> eventsHandlerMap = new HashMap<>();



    public static QObject superObject = QObject.constructSuperObject();
    public static QObject numberPrototype = new QObject("Number", null, new HashMap<>());
    public static QObject nullPrototype = new QObject("Null", null, new HashMap<>());
    public static QObject stringPrototype = new QObject("String", null, new HashMap<>());
    public static QObject listPrototype = new QObject("List", null, new HashMap<>());
    public static QObject boolPrototype = new QObject("Bool", null, new HashMap<>());
    public static QObject funcPrototype = new QObject("Func", null, new HashMap<>());



    public Runtime(String sourceCode, Node root, IOManager io, boolean doProfile) {
        this.sourceCode = sourceCode;
        this.root = root;
        this.doProfile = doProfile;
        this.io = io;
        this.memory = new Memory();
        registerDefaults();
        libraryRegistry.libraryRoots.add("/home/tapeline/JavaProjects/Files/Quailv2/qlibs/?");
    }

    private void registerDefaults() {
        superObject.setPrototypeFlag(true);
        memory.set("Object", superObject, new ArrayList<>());

        memory.set("Number", numberPrototype, new ArrayList<>());
        numberPrototype.setPrototypeFlag(true);
        numberPrototype.setSuperClass(superObject);
        numberPrototype.set("ceil", new NumberFuncCeil(this));
        numberPrototype.set("round", new NumberFuncRound(this));
        numberPrototype.set("floor", new NumberFuncFloor(this));

        memory.set("Null", nullPrototype, new ArrayList<>());
        numberPrototype.setSuperClass(superObject);
        nullPrototype.setPrototypeFlag(true);

        memory.set("List", listPrototype, new ArrayList<>());
        numberPrototype.setSuperClass(superObject);
        listPrototype.setPrototypeFlag(true);

        memory.set("Func", funcPrototype, new ArrayList<>());
        numberPrototype.setSuperClass(superObject);
        funcPrototype.setPrototypeFlag(true);

        memory.set("Bool", boolPrototype, new ArrayList<>());
        numberPrototype.setSuperClass(superObject);
        boolPrototype.setPrototypeFlag(true);

        memory.set("String", stringPrototype, new ArrayList<>());
        numberPrototype.setSuperClass(superObject);
        stringPrototype.setPrototypeFlag(true);
        stringPrototype.set("upper", new StringFuncUpper(this));

        memory.set("abs", new FuncAbs(this), new ArrayList<>());
        memory.set("all", new FuncAll(this), new ArrayList<>());
        memory.set("any", new FuncAny(this), new ArrayList<>());
        memory.set("enumerate", new FuncEnumerate(this), new ArrayList<>());
        memory.set("eval", new FuncEval(this), new ArrayList<>());
        memory.set("exec", new FuncExec(this), new ArrayList<>());
        memory.set("input", new FuncInput(this), new ArrayList<>());
        memory.set("print", new FuncPrint(this), new ArrayList<>());
        memory.set("millis", new FuncMillis(this), new ArrayList<>());
        memory.set("className", new FuncClassName(this), new ArrayList<>());
        memory.set("superClassName", new FuncSuperClassName(this), new ArrayList<>());
        memory.set("remainingAsyncs", new FuncRemainingAsyncs(this), new ArrayList<>());
        memory.set("asyncsDone", new FuncAsyncsDone(this), new ArrayList<>());
    }

    public void error(String message) throws RuntimeStriker {
        throw new RuntimeStriker(
                new ErrorMessage(
                        Error.ERROR,
                        message
                )
        );
    }

    private void begin(Node node) {
        node.executionStart = System.currentTimeMillis();
    }

    private void end(Node node) {
        node.executionTime = System.currentTimeMillis() - node.executionStart;
    }

    public void loopFor(List<String> iterator, QObject iterable, JavaAction action, Memory scope)
            throws RuntimeStriker {
        int unpackingSize = iterator.size();
        Memory enclosing = new Memory(scope);
        long rethrow = 0;
        iterable.iterateStart(this);
        while (true) {
            try {
                QObject next = iterable.iterateNext(this);
                if (unpackingSize == 1)
                    enclosing.set(iterator.get(0), next, new ArrayList<>());
                else if (next.isList()) {
                    QList list = (QList) next;
                    if (list.values.size() != unpackingSize)
                        error("Unpacking failed. List size = " +
                                list.values.size() + "; Iterators = " + unpackingSize);
                    for (int i = 0; i < unpackingSize; i++)
                        enclosing.set(iterator.get(i), list.values.get(i), new ArrayList<>());
                } else if (unpackingSize == 2) {
                    String keyVar = iterator.get(0);
                    String valVar = iterator.get(1);
                    next.getTable().forEach((key, value) -> {
                        enclosing.set(keyVar, Val(key), new ArrayList<>());
                        enclosing.set(valVar, value, new ArrayList<>());
                    });
                } else error("Iterator unpacking error. Unknown error");

                action.action(this, enclosing);
            } catch (RuntimeStriker striker) {
                if (striker.type == RuntimeStriker.Type.BREAK) {
                    if (--striker.strikeHP > 0) rethrow = striker.strikeHP;
                    break;
                } else if (striker.type == RuntimeStriker.Type.STOP_ITERATION)
                    break;
                else if (striker.type == RuntimeStriker.Type.CONTINUE)
                    continue;
                else throw striker;
            }
        }
        if (rethrow > 0) throw new RuntimeStriker(rethrow);
    }

    public QObject performBinaryOperation(QObject operandA, TokenType op, QObject operandB)
            throws RuntimeStriker {
        switch (op) {
            case PLUS: return operandA.sum(this, operandB);
            case MINUS: return operandA.subtract(this, operandB);
            case MULTIPLY: return operandA.multiply(this, operandB);
            case DIVIDE: return operandA.divide(this, operandB);
            case INTDIV: return operandA.intDivide(this, operandB);
            case MODULO: return operandA.modulo(this, operandB);
            case POWER: return operandA.power(this, operandB);
            case SHIFT_LEFT: return operandA.shiftLeft(this, operandB);
            case SHIFT_RIGHT: return operandA.shiftRight(this, operandB);
            case AND: return operandA.and(this, operandB);
            case OR: return operandA.or(this, operandB);
            case GREATER: return operandA.greater(this, operandB);
            case GREATER_EQUAL: return operandA.greaterEqual(this, operandB);
            case LESS: return operandA.less(this, operandB);
            case LESS_EQUAL: return operandA.lessEqual(this, operandB);
            case EQUALS: return operandA.equalsObject(this, operandB);
            case NOT_EQUALS: return operandA.notEqualsObject(this, operandB);
            case INSTANCEOF: return Val(operandA.instanceOf(operandB));
        }
        return null;
    }

    public final QObject run(Node node, Memory scope) throws RuntimeStriker {
        // TODO: split into methods
        if (doProfile) begin(node);
        try {
        if (node instanceof AsyncFlagNode) {
            AsyncFlagNode thisNode = (AsyncFlagNode) node;
            AsyncRuntimeWorker worker = new AsyncRuntimeWorker(thisNode.node, this, scope);
            asyncRuntimeWorkers.add(worker);
            worker.start();
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof BlockNode) {
            BlockNode thisNode = (BlockNode) node;
            int blockSize = thisNode.nodes.size();
            for (int i = 0; i < blockSize; i++)
                run(thisNode.nodes.get(i), scope);
            if (doProfile) end(node);
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
                            false
                    )),
                    thisNode.code,
                    this,
                    scope,
                    new ArrayList<>(),
                    false
            );
            if (eventsHandlerMap.containsKey(eventName))
                eventsHandlerMap.put(eventName,
                        Arrays.asList(new Pair<>(func, true)));
            else
                eventsHandlerMap.get(eventName).add(new Pair<>(func, true));
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof IfNode) {
            IfNode thisNode = (IfNode) node;
            int conditions = thisNode.conditions.size();
            for (int i = 0; i < conditions; i++) {
                QObject condition = run(thisNode.conditions.get(i), scope);
                if (condition.isTrue()) {
                    run(thisNode.branches.get(i), scope);
                    if (doProfile) end(node);
                    return Val();
                }
            }
            if (thisNode.elseBranch != null)
                run(thisNode.elseBranch, scope);
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof TryCatchNode) {
            TryCatchNode thisNode = (TryCatchNode) node;
            try {
                run(thisNode.tryCode, scope);
            } catch (RuntimeStriker striker) {
                if (striker.type == RuntimeStriker.Type.EXCEPTION) {
                    for (CatchClause clause : thisNode.catchClauses) {
                        QObject expectedException = run(clause.exceptionClass, scope);
                        if (striker.error.errorType.equals(expectedException.toString())) {
                            Memory enclosing = new Memory(scope);
                            enclosing.set(
                                    clause.variable,
                                    QObject.Val(Dict.make(
                                            new Pair<>(
                                                    "type",
                                                    QObject.Val(striker.error.errorType)
                                            ),
                                            new Pair<>(
                                                    "message",
                                                    QObject.Val(striker.error.message)
                                            )
                                    )),
                                    new ArrayList<>()
                            );
                            run(clause.code, enclosing);
                        }
                    }
                } else {
                    if (doProfile) end(node);
                    throw striker;
                }
            }
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof EffectNode) {
            EffectNode thisNode = (EffectNode) node;
            switch (thisNode.effect) {
                case EFFECT_ASSERT: {
                    if (doProfile) end(node);
                    if (!run(thisNode.value, scope).isTrue()) error(
                            "Assertion failed");
                    break;
                }
                case EFFECT_RETURN: {
                    if (doProfile) end(node);
                    throw new RuntimeStriker(run(thisNode.value, scope));
                }
                case EFFECT_THROW: {
                    if (doProfile) end(node);
                    QObject exception = run(thisNode.value, scope);
                    throw new RuntimeStriker(new Error(), exception);
                }
                case EFFECT_STRIKE: {
                    if (doProfile) end(node);
                    QObject count = run(thisNode.value, scope);
                    if (!count.isNum()) error("Cannot strike. Not a number");
                    throw new RuntimeStriker((long) ((QNumber) count).value);
                }
                case EFFECT_IMPORT: {
                    String code = IOManager.fileInput(run(thisNode.value, scope).toString());
                    try {
                        Lexer lexer = new Lexer(code);
                        List<Token> tokens = lexer.scan();
                        Parser parser = new Parser(code, tokens);
                        Node root = parser.parse();
                        run(root, scope);
                    } catch (RuntimeStriker striker) {
                        if (striker.type == RuntimeStriker.Type.EXCEPTION) {
                            System.err.println(ErrorFormatter.formatError(code, striker.error));
                            System.exit(1);
                        }
                    } catch (ParserException exception) {
                        System.err.println(exception.getMessage());
                        System.exit(1);
                    }
                    if (doProfile) end(node);
                    break;
                }
            }
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof InstructionNode) {
            InstructionNode thisNode = (InstructionNode) node;
            switch (thisNode.instruction) {
                case INSTRUCTION_BREAK:
                    throw new RuntimeStriker(RuntimeStriker.Type.BREAK);
                case INSTRUCTION_CONTINUE:
                    throw new RuntimeStriker(RuntimeStriker.Type.CONTINUE);
                case INSTRUCTION_BREAKPOINT: {
                    System.err.println(
                            "|| Debug: Breakpoint at " + node.line + " line");
                    String cmd;
                    Scanner sc = new Scanner(IOManager.input);
                    do {
                        cmd = sc.nextLine();
                        if (cmd.startsWith("eval")) {
                            String evalCode = cmd.substring(4 + 1);
                            QObject result = Val();
                            try {
                                Lexer lexer = new Lexer(evalCode);
                                List<Token> tokens = lexer.scan();
                                Parser parser = new Parser(evalCode, tokens);
                                Node evalNode = parser.parse();
                                result = run(evalNode, scope);
                            } catch (ParserException e) {
                                System.err.println("PE: " + e.toString());
                            } catch (RuntimeStriker r) {
                                if (r.type == RuntimeStriker.Type.RETURN)
                                    result = r.returnValue;
                                else
                                    result = Val("|| ERROR: \n" + r.error.toString());
                            } catch (Exception e) {
                                System.err.println("E: " + e.toString());
                            }
                            System.err.println(result.toString());
                        } else if (cmd.startsWith("help")) {
                            System.err.println("|| Commands:");
                            System.err.println("||   eval <code>    Run quail code");
                            System.err.println("||    Example: eval obj.get(\"a\").val");
                            System.err.println("||   w              Proceed");
                        } else if (cmd.startsWith("w")) {
                            break;
                        }
                    } while (!cmd.equals("exit"));
                    break;
                }
            }
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof UseNode) {
            UseNode thisNode = (UseNode) node;
            QObject library = LibraryLoader.loadLibrary(this, libraryRegistry, thisNode.path);
            if (thisNode.alias != null)
                scope.set(thisNode.alias, library, new ArrayList<>());
            if (doProfile) end(node);
            return library;
        } else if (node instanceof CallNode) {
            CallNode thisNode = (CallNode) node;
            QObject callee = null, parent = null;
            if (thisNode.isFieldCall) {
                parent = run(thisNode.parentField, scope);
                callee = parent.get(thisNode.field);
            } else
                callee = run(thisNode.function, scope);
            List<QObject> args = new ArrayList<>();
            int argsCount = thisNode.arguments.size();
            for (int i = 0; i < argsCount; i++)
                args.add(run(thisNode.arguments.get(i), scope));
            if (parent != null && thisNode.isFieldCall) {
                if (!parent.isPrototype())
                    return parent.callFromThis(this, callee, args);
            }
            if (doProfile) end(node);
            return callee.call(this, args);
        } else if (node instanceof ContainerGeneratorNode) {
            ContainerGeneratorNode thisNode = (ContainerGeneratorNode) node;
            QObject generated = Val(new HashMap<>());
            QObject iterable = run(thisNode.iterable, scope);
            loopFor(
                    thisNode.iterators,
                    iterable,
                    new JavaAction() {
                        @Override
                        public QObject action(Runtime runtime, Memory memory) throws RuntimeStriker {
                            if (thisNode.condition != null) {
                                QObject condition = run(thisNode.condition, memory);
                                if (condition.isTrue())
                                    generated.set(
                                            run(thisNode.key, memory).toString(),
                                            run(thisNode.value, memory)
                                    );
                                else if (thisNode.fallbackKey != null && thisNode.fallbackValue != null)
                                    generated.set(
                                            run(thisNode.fallbackKey, memory).toString(),
                                            run(thisNode.fallbackValue, memory)
                                    );
                            } else generated.set(
                                    run(thisNode.key, memory).toString(),
                                    run(thisNode.value, memory)
                            );
                            return null;
                        }
                    },
                    scope
            );
            if (doProfile) end(node);
            return generated;
        } else if (node instanceof ListGeneratorNode) {
            ListGeneratorNode thisNode = (ListGeneratorNode) node;
            QList generated = (QList) Val(new ArrayList<>());
            QObject iterable = run(thisNode.iterable, scope);
            loopFor(
                    thisNode.iterators,
                    iterable,
                    new JavaAction() {
                        @Override
                        public QObject action(Runtime runtime, Memory memory) throws RuntimeStriker {
                            if (thisNode.condition != null) {
                                QObject condition = run(thisNode.condition, memory);
                                if (condition.isTrue())
                                    generated.values.add(run(thisNode.expression, memory));
                                else if (thisNode.fallback != null)
                                    generated.values.add(run(thisNode.fallback, memory));
                            } else generated.values.add(run(thisNode.expression, memory));
                            return null;
                        }
                    },
                    scope
            );
            if (doProfile) end(node);
            return generated;
        } else if (node instanceof LiteralBool) {
            if (doProfile) end(node);
            return Val(((LiteralBool) node).value);
        } else if (node instanceof LiteralNum) {
            if (doProfile) end(node);
            return Val(((LiteralNum) node).value);
        } else if (node instanceof LiteralNull) {
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof LiteralString) {
            if (doProfile) end(node);
            return Val(((LiteralString) node).value);
        } else if (node instanceof LiteralList) {
            LiteralList thisNode = (LiteralList) node;
            List<QObject> values = new ArrayList<>();
            int objectCount = thisNode.value.size();
            for (int i = 0; i < objectCount; i++)
                values.add(run(thisNode.value.get(i), scope));
            if (doProfile) end(node);
            return Val(values);
        } else if (node instanceof LiteralContainer) {
            LiteralContainer thisNode = (LiteralContainer) node;
            QObject container = new QObject(new HashMap<>());
            container.setPrototypeFlag(true);
            container.isDict = true;
            container.setObjectMetadata(superObject);
            int pairCount = thisNode.contents.getKeys().size();
            for (int i = 0; i < pairCount; i++) {
                String key = run(thisNode.contents.getKeys().get(i), scope).toString();
                QObject value = run(thisNode.contents.getValueMap().get(i), scope);
                container.set(key, value);
            }
            if (doProfile) end(node);
            return container;
        } else if (node instanceof LiteralFunction) {
            LiteralFunction thisNode = (LiteralFunction) node;
            if (thisNode.name.equals("->"))
                return new QFunc(
                        thisNode.name,
                        thisNode.args,
                        thisNode.code,
                        this,
                        scope,
                        new ArrayList<>(),
                        thisNode.isStatic
                );
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
                        scope,
                        new ArrayList<>(),
                        thisNode.isStatic
                );
                scope.set(func.name, func, new ArrayList<>());
                return func;
            }
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof LiteralClass) {
            LiteralClass thisNode = (LiteralClass) node;
            QObject parent = thisNode.like == null ? null : run(thisNode.like, scope);
            if (parent != null && parent.isNull())
                error(
                        "Attempt to inherit from null. Maybe the class isn't defined at the moment");
            else if (parent != null && !parent.isPrototype())
                error(
                        "Attempt to inherit from a non-class object");
            VariableTable contents = new VariableTable();
            if (parent != null)
                contents.putAll(parent.getTable());
            Set<String> hereDefinedMethods = new HashSet<>();
            thisNode.methods.forEach((methodName, method) -> {
                if (contents.containsKey(methodName) && contents.get(methodName).isFunc()
                    && hereDefinedMethods.contains(methodName)) {
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
                            scope,
                            new ArrayList<>(),
                            method.isStatic
                    ));
                    hereDefinedMethods.add(methodName);
                }
            });
            for (Map.Entry<VariableNode, Node> entry : thisNode.contents.entrySet())
                contents.put(entry.getKey().id, run(entry.getValue(), scope),
                        entry.getKey().modifiers);
            int items = thisNode.initialize.size();
            for (int i = 0; i < items; i++)
                run(thisNode.initialize.get(i), scope);
            QObject qClass = new QObject(thisNode.name, superObject, contents);
            if (parent != null)
                qClass.setSuperClass(parent);
            qClass.setPrototypeFlag(true);
            scope.set(thisNode.name, qClass, new ArrayList<>());
            if (doProfile) end(node);
            return qClass;
        } else if (node instanceof ForNode) {
            ForNode thisNode = (ForNode) node;
            QObject iterable = run(thisNode.iterable, scope);
            int unpackingSize = thisNode.iterator.size();
            Memory enclosing = new Memory(scope);
            long rethrow = 0;
            iterable.iterateStart(this);
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
                    if (striker.type == RuntimeStriker.Type.BREAK) {
                        if (--striker.strikeHP > 0) rethrow = striker.strikeHP;
                        break;
                    } else if (striker.type == RuntimeStriker.Type.STOP_ITERATION)
                        break;
                    else if (striker.type == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            if (doProfile) end(node);
            if (rethrow > 0) throw new RuntimeStriker(rethrow);
            return Val();
        } else if (node instanceof LoopNode) {
            LoopNode thisNode = (LoopNode) node;
            long rethrow = 0;
            while (true) {
                try {
                    run(thisNode.code, scope);

                    QObject condition = run(thisNode.condition, scope);
                    if (condition.isTrue()) break;
                } catch (RuntimeStriker striker) {
                    if (striker.type == RuntimeStriker.Type.BREAK) {
                        if (--striker.strikeHP > 0) rethrow = striker.strikeHP;
                        break;
                    } else if (striker.type == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            if (doProfile) end(node);
            if (rethrow > 0) throw new RuntimeStriker(rethrow);
            return Val();
        } else if (node instanceof WhileNode) {
            WhileNode thisNode = (WhileNode) node;
            long rethrow = 0;
            while (true) {
                try {
                    QObject condition = run(thisNode.condition, scope);
                    if (!condition.isTrue()) break;

                    run(thisNode.code, scope);
                } catch (RuntimeStriker striker) {
                    if (striker.type == RuntimeStriker.Type.BREAK) {
                        if (--striker.strikeHP > 0) rethrow = striker.strikeHP;
                        break;
                    } else if (striker.type == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            if (doProfile) end(node);
            if (rethrow > 0) throw new RuntimeStriker(rethrow);
            return Val();
        } else if (node instanceof ThroughNode) {
            ThroughNode thisNode = (ThroughNode) node;
            String iterator = thisNode.iterator;
            if (thisNode.start == null || thisNode.end == null)
                error("Attempt to make indefinite loop. Start or end of range is null");
            QObject startObject = run(thisNode.start, scope);
            QObject endObject = run(thisNode.end, scope);
            QObject stepObject = null;
            if (thisNode.step != null)
                stepObject = run(thisNode.step, scope);
            if (!startObject.isNum())
                error("Attempt to make through-loop with non-number start");
            if (!endObject.isNum())
                error("Attempt to make through-loop with non-number end");
            if (stepObject != null && !stepObject.isNum())
                error("Attempt to make through-loop with non-number step");
            double start = ((QNumber) startObject).value;
            double end = ((QNumber) endObject).value;
            double step;
            if (stepObject != null)
                step = ((QNumber) stepObject).value;
            else
                step = start <= end ? 1 : -1;
            boolean isIncreasing = start <= end;
            boolean isIncluding = thisNode.doesInclude;
            long rethrow = 0;
            QNumber numIterator = (QNumber) QObject.Val(start);
            scope.set(iterator, numIterator, Arrays.asList(new FinalModifier()));
            while (true) {
                try {
                    if (isIncluding && (
                            (isIncreasing && numIterator.value > end)
                                    ||
                                    (!isIncreasing && numIterator.value < end)
                    ) || !isIncluding && (
                            (isIncreasing && numIterator.value >= end)
                                    ||
                                    (!isIncreasing && numIterator.value <= end)
                    )) break;
                    run(thisNode.code, scope);
                } catch (RuntimeStriker striker) {
                    if (striker.type == RuntimeStriker.Type.BREAK) {
                        if (--striker.strikeHP > 0) rethrow = striker.strikeHP;
                        break;
                    } else if (striker.type == RuntimeStriker.Type.CONTINUE) {
                        numIterator.value += step;
                        continue;
                    } else throw striker;
                }
                numIterator.value += step;
            }
            if (doProfile) end(node);
            if (rethrow > 0) throw new RuntimeStriker(rethrow);
            return Val();
        } else if (node instanceof TypeCastNode) {
            TypeCastNode thisNode = (TypeCastNode) node;
            QObject casting = run(thisNode.value, scope);
            if (thisNode.castedType == TokenType.TYPE_BOOL)
                return casting.typeBool(this);
            else if (thisNode.castedType == TokenType.TYPE_STRING)
                return casting.typeString(this);
            else if (thisNode.castedType == TokenType.TYPE_NUM)
                return casting.typeNumber(this);
            else error("Unsupported typecast");
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof AssignNode) {
            AssignNode thisNode = (AssignNode) node;
            QObject value = run(thisNode.value, scope);
            if (scope.contains(thisNode.variable))
                scope.set(this, thisNode.variable, value);
            else
                scope.set(thisNode.variable, value, thisNode.variableNode.modifiers);
            if (doProfile) end(node);
            return value;
        } else if (node instanceof BinaryOperatorNode) {
            BinaryOperatorNode thisNode = (BinaryOperatorNode) node;
            QObject operandA = run(thisNode.left, scope);
            QObject operandB = run(thisNode.right, scope);
            if (thisNode.token.getMod() == TokenType.ARRAY_MOD) {
                if (!operandA.isList() || !operandB.isList())
                    error("Cannot perform unwrapping array operation on non-list");
                QList listA = (QList) operandA;
                QList listB = (QList) operandB;
                if (listA.values.size() != listB.values.size())
                    error(
                            "Cannot perform unwrapping array operation on lists with different sizes");
                List<QObject> result = new ArrayList<>();
                int count = listA.values.size();
                for (int i = 0; i < count; i++) {
                    QObject ret = performBinaryOperation(
                            listA.values.get(i),
                            thisNode.operation,
                            listB.values.get(i)
                    );
                    if (ret == null)
                        error("Unknown binary operation");
                    result.add(ret);
                }
                return QObject.Val(result);
            } else if (thisNode.token.getMod() == TokenType.MATRIX_MOD) {
                if (!operandA.isList() || !operandB.isList())
                    error("Cannot perform unwrapping matrix operation on non-list");
                QList listA = (QList) operandA;
                QList listB = (QList) operandB;
                if (listA.values.size() != listB.values.size())
                    error(
                            "Cannot perform unwrapping matrix operation on lists with different sizes");
                List<QObject> result = new ArrayList<>();
                int countX = listA.values.size();
                for (int x = 0; x < countX; x++) {
                    QObject subA = listA.values.get(x);
                    QObject subB = listB.values.get(x);
                    if (!subA.isList() || !subB.isList() ||
                            ((QList) subA).values.size() != ((QList) subB).values.size())
                        error("Sublists do not match requirements (not list // not same size)");
                    QList sublistA = (QList) subA;
                    QList sublistB = (QList) subB;
                    List<QObject> subResult = new ArrayList<>();
                    int countY = sublistA.values.size();
                    for (int y = 0; y < countY; y++) {
                        QObject ret = performBinaryOperation(
                                sublistA.values.get(y),
                                thisNode.operation,
                                sublistB.values.get(y)
                        );
                        if (ret == null)
                            error("Unknown binary operation");
                        subResult.add(ret);
                    }
                    result.add(QObject.Val(subResult));
                }
                return QObject.Val(result);
            }
            QObject ret = performBinaryOperation(operandA, thisNode.operation, operandB);
            if (ret == null)
                error("Unknown binary operation");
            if (doProfile) end(node);
            return ret;
        } else if (node instanceof FieldReferenceNode) {
            QObject object = run(((FieldReferenceNode) node).object, scope);
            if (doProfile) end(node);
            return object.getOverridable(this, ((FieldReferenceNode) node).field);
        } else if (node instanceof FieldSetNode) {
            FieldSetNode thisNode = (FieldSetNode) node;
            QObject object = run(thisNode.object, scope);
            QObject value = run(thisNode.value, scope);
            String field = thisNode.field;
            object.setOverridable(this, field, value);
            if (object.isPrototype())
                object.updateInheritanceAndDerivations();
            if (doProfile) end(node);
            return value;
        } else if (node instanceof IndexingNode) {
            IndexingNode thisNode = (IndexingNode) node;
            QObject object = run(thisNode.object, scope);
            QObject index = run(thisNode.index, scope);
            if (doProfile) end(node);
            return object.index(this, index);
        } else if (node instanceof IndexSetNode) {
            IndexSetNode thisNode = (IndexSetNode) node;
            QObject object = run(thisNode.object, scope);
            QObject index = run(thisNode.index, scope);
            QObject value = run(thisNode.value, scope);
            if (doProfile) end(node);
            return object.indexSet(this, index, value);
        } else if (node instanceof RangeNode) {
            RangeNode thisNode = (RangeNode) node;
            QList generated = (QList) Val(new ArrayList<>());
            if (thisNode.start == null || thisNode.end == null)
                error("Attempt to make indefinite loop. Start or end of range is null");
            QObject startObject = run(thisNode.start, scope);
            QObject endObject = run(thisNode.end, scope);
            QObject stepObject = null;
            if (thisNode.step != null)
                stepObject = run(thisNode.step, scope);
            if (!startObject.isNum())
                error("Attempt to make through-loop with non-number start");
            if (!endObject.isNum())
                error("Attempt to make through-loop with non-number end");
            if (stepObject != null && !stepObject.isNum())
                error("Attempt to make through-loop with non-number step");
            double start = ((QNumber) startObject).value;
            double end = ((QNumber) endObject).value;
            double step;
            if (stepObject != null)
                step = ((QNumber) stepObject).value;
            else
                step = start <= end ? 1 : -1;
            boolean isIncreasing = start <= end;
            boolean isIncluding = thisNode.include;
            double numIterator = start;
            while ((!isIncluding || ((!isIncreasing || !(numIterator > end))
                    &&
                    (isIncreasing || !(numIterator < end))))
                    &&
                    (isIncluding || ((!isIncreasing || !(numIterator >= end))
                    &&
                    (isIncreasing || !(numIterator <= end))))) {
                generated.values.add(Val(numIterator));
                numIterator += step;
            }
            if (doProfile) end(node);
            return generated;
        } else if (node instanceof SubscriptNode) {
            SubscriptNode thisNode = (SubscriptNode) node;
            QObject object = run(thisNode.object, scope);
            QObject start = QObject.Val(), end = QObject.Val(), step = QObject.Val();
            if (thisNode.start != null)
                start = run(thisNode.start, scope);
            if (thisNode.end != null)
                end = run(thisNode.end, scope);
            if (thisNode.step != null)
                step = run(thisNode.step, scope);
            QObject ret;
            if (step.isNull())
                ret = object.subscriptStartEnd(this, start, end);
            else
                ret = object.subscriptStartEndStep(this, start, end, step);
            if (doProfile) end(node);
            return ret;
        } else if (node instanceof UnaryOperatorNode) {
            UnaryOperatorNode thisNode = (UnaryOperatorNode) node;
            QObject operand = run(thisNode.operand, scope);
            QObject ret = null;
            switch (thisNode.operation) {
                case NOT:
                    ret = operand.not(this);
                    break;
                case MINUS:
                    ret = operand.minus(this);
                    break;
                case HASH:
                    ret = QObject.Val(operand.hashCode());
                    break;
            }
            if (ret == null)
                error("Unknown unary operation");
            if (doProfile) end(node);
            return ret;
        } else if (node instanceof TupleNode) {
            TupleNode thisNode = (TupleNode) node;
            List<QObject> values = new ArrayList<>();
            int objectCount = thisNode.nodes.size();
            for (int i = 0; i < objectCount; i++)
                values.add(run(thisNode.nodes.get(i), scope));
            if (doProfile) end(node);
            return Val(values);
        } else if (node instanceof VariableNode) {
            return scope.get(((VariableNode) node).id, ((VariableNode) node));
        } else if (node instanceof JavaEmbedNode) {
            JavaEmbedNode thisNode = (JavaEmbedNode) node;
            System.out.println(thisNode.code);
            // TODO: embed execution
        }
        } catch (RuntimeStriker s) {
            if (doProfile) end(node);
            if (s.error != null && !s.error.positionResolved)
                s.error.setPosition(node.line, node.character, node.length);
            throw s;
        }
        return Val();
    }

    public void translated() throws RuntimeStriker {
        //n = 1
        memory.set(this,"n", Val(1));

        //start = millis()
        memory.set(this, "start", memory.get("millis").call(
                this, new ArrayList<>()));

        //through 1:+1000000 as x
        String iterator = "x";
        QObject startObject = Val(1);
        QObject endObject = Val(1000000);
        QObject stepObject = null;
        if (!startObject.isNum())
            error("Attempt to make through-loop with non-number start");
        if (!endObject.isNum())
            error("Attempt to make through-loop with non-number end");
        if (stepObject != null && !stepObject.isNum())
            error("Attempt to make through-loop with non-number step");
        double start = ((QNumber) startObject).value;
        double end = ((QNumber) endObject).value;
        double step;
        if (stepObject != null)
            step = ((QNumber) stepObject).value;
        else
            step = start <= end ? 1 : -1;
        boolean isIncreasing = start <= end;
        boolean isIncluding = true;
        long rethrow = 0;
        QNumber numIterator = (QNumber) QObject.Val(start);
        memory.set(iterator, numIterator, Arrays.asList(new FinalModifier()));
        while (true) {
            try {
                if (isIncluding && (
                        (isIncreasing && numIterator.value > end)
                                ||
                                (!isIncreasing && numIterator.value < end)
                ) || !isIncluding && (
                        (isIncreasing && numIterator.value >= end)
                                ||
                                (!isIncreasing && numIterator.value <= end)
                )) break;
                //n = n * x
                memory.set(
                        this,
                        "n",
                        memory.get("n").multiply(this, memory.get("x"))
                );
            } catch (RuntimeStriker striker) {
                if (striker.type == RuntimeStriker.Type.BREAK) {
                    if (--striker.strikeHP > 0) rethrow = striker.strikeHP;
                    break;
                } else if (striker.type == RuntimeStriker.Type.CONTINUE) {
                    numIterator.value += step;
                    continue;
                } else throw striker;
            }
            numIterator.value += step;
        }
        if (rethrow > 0) throw new RuntimeStriker(rethrow);

        //print(millis() - start)
        memory.get("print").call(
                this,
                Arrays.asList(
                        memory.get("millis")
                                .call(this, new ArrayList<>())
                                .subtract(this, memory.get("start"))
                )
        );
    }

    public static void main(String[] args) throws RuntimeStriker {
        // INTERPRETED: 404-469 ~= 430
        // TRANSLATED : 106-125 ~= 110
        // C++ Debug  : 8-9     ~= 9
        // C++ Release:            <1
        // Java       : 4-8     ~= 6
        // x4 performance

        /*Runtime r = new Runtime("", new BlockNode(Token.UNDEFINED, new ArrayList<>()),
                new IOManager(), false);
        r.translated();*/

    }

}
