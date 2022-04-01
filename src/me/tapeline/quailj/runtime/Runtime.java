package me.tapeline.quailj.runtime;

import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.*;
import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.builtins.*;
import me.tapeline.quailj.runtime.builtins.intype_nums.NumFuncCeil;
import me.tapeline.quailj.runtime.builtins.intype_nums.NumFuncFloor;
import me.tapeline.quailj.runtime.builtins.intype_nums.NumFuncRound;
import me.tapeline.quailj.runtime.builtins.intype_string.*;
import me.tapeline.quailj.runtime.builtins.library_random.LibFuncToss;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.*;

import java.util.*;

public class Runtime {

    public static VoidType Void = new VoidType();
    public static List<String> nativeLibs = new ArrayList<>(Arrays.asList(
            "random"
    ));

    public final Node rootNode;
    public Memory scope;
    public final HashMap<String, FuncType> eventHandlers = new HashMap<>();
    public Node current;
    public IOManager io;

    public Runtime(Node rootNode, IOManager io) {
        this.rootNode = rootNode;
        this.scope = new Memory();
        this.io = io;
        defineBuiltIns();
    }

    public void defineBuiltIns() {
        scope.set("out", new FuncOut());
        scope.set("put", new FuncPut());
        scope.set("input", new FuncInput());

        scope.set("clock", new FuncClock());
        scope.set("millis", new FuncMillis());

        scope.set("tostring", new FuncTostring());
        scope.set("tonum", new FuncTonum());
        scope.set("tobool", new FuncTobool());

        scope.set("filewrite", new FuncFilewrite());
        scope.set("fileread", new FuncFileread());
        scope.set("fileexists", new FuncFileexists());

        scope.set("nothing", new VoidType());
        scope.set("million", new NumType(1000000D));
        scope.set("billion", new NumType(1000000000D));
        scope.set("trillion", new NumType(1000000000000D));

        scope.set("Number", new ContainerType("Number", "container", new HashMap<>(), true));
        NumType.tableToClone.put("floor", new NumFuncFloor());
        NumType.tableToClone.put("ceil", new NumFuncCeil());
        NumType.tableToClone.put("round", new NumFuncRound());

        scope.set("Null", new ContainerType("Null", "container", new HashMap<>(), true));

        scope.set("String", new ContainerType("String", "container", new HashMap<>(), true));
        StringType.tableToClone.put("get", new StringFuncGet());
        StringType.tableToClone.put("replace", new StringFuncReplace());
        StringType.tableToClone.put("size", new StringFuncSize());
        StringType.tableToClone.put("sub", new StringFuncSub());
        StringType.tableToClone.put("upper", new StringFuncUpper());
        StringType.tableToClone.put("lower", new StringFuncLower());
        StringType.tableToClone.put("capitalize", new StringFuncCapitalize());
        StringType.tableToClone.put("split", new StringFuncSplit());
        StringType.tableToClone.put("find", new StringFuncFind());
        StringType.tableToClone.put("reverse", new StringFuncReverse());

        scope.set("Bool", new ContainerType("Bool", "container", new HashMap<>(), true));
        scope.set("List", new ContainerType("List", "container", new HashMap<>(), true));

    }

    public ContainerType getNative(String id) throws RuntimeStriker {
        if (id.equals("random")) {
            HashMap<String, QType> lib = new HashMap<>();
            lib.put("toss", new LibFuncToss());
            return new ContainerType("lib_random", "container", lib, false);
        }
        throw new RuntimeStriker("get native lib:lib not found");
    }

    public ContainerType overrideContainerContents(ContainerType dest, ContainerType src) {
        dest.table.putAll(src.table);
        return dest;
    }

    public ContainerType inheritContainer(ContainerType parent, ContainerType child) {
        if (parent.like().equals("container")) {
            child = overrideContainerContents(child, parent);
            return child;
        } else {
            QType p = scope.get(parent.like());
            if (p instanceof ContainerType) {
                parent = inheritContainer((ContainerType) p, parent);
                child = overrideContainerContents(child, parent);
                return child;
            }
        }
        return child;
    }

    public QType run(Node node, Memory scope) throws RuntimeStriker {
        current = node;
        if (node instanceof BinaryOperatorNode) {
            QType a = run(((BinaryOperatorNode) node).lnode, scope);
            QType b = run(((BinaryOperatorNode) node).rnode, scope);
            String containerImpl = Utilities.transformOp(((BinaryOperatorNode) node).operator.c);
            if (a != null && a.table != null && a.table.size() != 0) {
                if (a.table.containsKey(containerImpl)) {
                    if (a.table.get(containerImpl) instanceof FuncType) {
                        List<QType> metaArgs = new ArrayList<>(Arrays.asList(a, b));
                        return ((FuncType) a.table.get(containerImpl)).run(this, metaArgs);
                    }
                }
            }
            switch (((BinaryOperatorNode) node).operator.c) {
                case "+":
                case "-":
                case "*":
                case "/":
                case "//":
                case ">":
                case "<":
                case "<=":
                case ">=": {
                    if (QType.isNum(a, b)) {
                        assert a instanceof NumType;
                        switch (((BinaryOperatorNode) node).operator.c) {
                            case  "+": return new NumType (((NumType) a).value +  ((NumType) b).value);
                            case  "-": return new NumType (((NumType) a).value -  ((NumType) b).value);
                            case  "*": return new NumType (((NumType) a).value *  ((NumType) b).value);
                            case  "/": return new NumType (((NumType) a).value /  ((NumType) b).value);
                            case  ">": return new BoolType(((NumType) a).value >  ((NumType) b).value);
                            case  "<": return new BoolType(((NumType) a).value <  ((NumType) b).value);
                            case "<=": return new BoolType(((NumType) a).value >= ((NumType) b).value);
                            case ">=": return new BoolType(((NumType) a).value <= ((NumType) b).value);
                            case "//": return new NumType (Math.floor(((NumType) a).value /  ((NumType) b).value));
                        }
                    } else if (QType.isNum(b) && QType.isStr(a)) {
                        assert a instanceof StringType;
                        switch (((BinaryOperatorNode) node).operator.c) {
                            case  "*": return new StringType(StringUtils.mult(((StringType) a).value,
                                    ((NumType) b).value));
                            case  "/": return ListUtils.newListType(StringUtils.div(((StringType) a).value,
                                    ((NumType) b).value));
                            case  ">": return new BoolType(((StringType) a).value.length() >
                                    ((NumType) b).value);
                            case  "<": return new BoolType(((StringType) a).value.length() <
                                    ((NumType) b).value);
                            case "<=": return new BoolType(((StringType) a).value.length() <=
                                    ((NumType) b).value);
                            case ">=": return new BoolType(((StringType) a).value.length() >=
                                    ((NumType) b).value);
                            case "//": return ListUtils.newListType(StringUtils.mod(((StringType) a).value,
                                    ((NumType) b).value));
                        }
                    } else if (QType.isNum(b) && QType.isList(a)) {
                        assert a instanceof ListType;
                        switch (((BinaryOperatorNode) node).operator.c) {
                            case "*": return new ListType(ListUtils.mult(((ListType) a).values,
                                    ((NumType) b).value));
                            case "/": return new ListType(ListUtils.div(((ListType) a).values,
                                    ((NumType) b).value));
                            case ">": return new BoolType(((ListType) a).values.size() >
                                    ((NumType) b).value);
                            case "<": return new BoolType(((ListType) a).values.size() <
                                    ((NumType) b).value);
                            case "<=": return new BoolType(((ListType) a).values.size() <=
                                    ((NumType) b).value);
                            case ">=": return new BoolType(((ListType) a).values.size() >=
                                    ((NumType) b).value);
                            case "//": return new ListType(ListUtils.mod(((ListType) a).values,
                                    ((NumType) b).value));
                        }
                    } else if (QType.isList(a, b)) {
                        assert a instanceof ListType;
                        switch (((BinaryOperatorNode) node).operator.c) {
                            case "+": return ListUtils.concat((ListType) a, (ListType) b);
                            case "-": return new ListType(ListUtils.removeAll(((ListType) a).values,
                                    ((ListType) b).values));
                        }
                    } else if (QType.isStr(a, b)) {
                        assert a instanceof StringType;
                        switch (((BinaryOperatorNode) node).operator.c) {
                            case "+": return new StringType(((StringType) a).value + ((StringType) b).value);
                            case "-": return new StringType(((StringType) a).value.replaceAll(
                                    ((StringType) b).value, ""));
                        }
                    }
                    break;
                }
                case "==":
                case "is": {
                    BoolType c = Utilities.compare(a, b);
                    if (c != null)
                        return c;
                    break;
                }
                case "!=": {
                    BoolType c = Utilities.compare(a, b);
                    if (c != null)
                        return new BoolType(!c.value);
                    break;
                }
                case "^": {
                    if (QType.isNum(a, b))
                        return new NumType(Math.pow(((NumType) a).value, ((NumType) b).value));
                    break;
                }
                case "%": {
                    if (QType.isNum(a, b))
                        return new NumType((((NumType) a).value % ((NumType) b).value));
                    break;
                }
                case "and": {
                    if (QType.isBool(a, b))
                        return new BoolType(((BoolType) a).value && ((BoolType) b).value);
                    break;
                }
                case "or": {
                    if (QType.isBool(a, b))
                        return new BoolType(((BoolType) a).value || ((BoolType) b).value);
                    else if (a instanceof VoidType && b instanceof VoidType)
                        return Void;
                    else if (a instanceof VoidType)
                        return b;
                    else if (b instanceof VoidType)
                        return a;
                    else
                        return a;
                }
                case "is type of":
                case "instanceof": {
                    if (QType.isStr(b)) {
                        if ((QType.isNum(a) && ((StringType) b).value.equals("num")) ||
                                (QType.isBool(a) && ((StringType) b).value.equals("bool")) ||
                                (QType.isStr(a) && ((StringType) b).value.equals("string")) ||
                                (QType.isList(a) && ((StringType) b).value.equals("list")) ||
                                (QType.isCont(a) && ((StringType) b).value.equals("container")))
                            return new BoolType(true);
                        if (QType.isCont(a) && ( ((ContainerType) a).name().equals(((StringType) b).value) ||
                                ((ContainerType) a).like().equals(((StringType) b).value)))
                            return new BoolType(true);
                    }
                    break;
                }
                case "is same type as": {
                    if (QType.isNum(a, b) ||
                            QType.isList(a, b)||
                            QType.isCont(a, b)||
                            QType.isStr(a, b) ||
                            QType.isBool(a, b))
                        return new BoolType(true);
                    break;
                }
                case "=": {
                    if (!(((BinaryOperatorNode) node).lnode instanceof VariableNode))
                        throw new RuntimeStriker("run:binaryop:set:cannot place value to non-variable type");
                    scope.set(((VariableNode) ((BinaryOperatorNode) node).lnode).token.c, b.copy());
                    return Void;
                }
                case "<-": {
                    if (!(((BinaryOperatorNode) node).lnode instanceof VariableNode))
                        throw new RuntimeStriker("run:binaryop:set:cannot place value to non-variable type");
                    scope.set(((VariableNode) ((BinaryOperatorNode) node).lnode).token.c, b);
                    return Void;
                }
                case ":": {
                    Assert.require(QType.isNum(a, b), "run:binaryop:range:expected numbers");
                    ListType l = new ListType();
                    int step = ((NumType) a).value < ((NumType) b).value? 1 : -1;
                    double iterator = ((NumType) a).value;
                    double till = ((NumType) b).value;
                    while (true) {
                        if (((NumType) a).value < till) if (iterator > till) break;
                        if (((NumType) a).value > till) if (iterator < till) break;
                        l.values.add(new NumType(iterator));
                        iterator += step;
                    }
                    return l;
                }
                case "step": {
                    Assert.require(((BinaryOperatorNode) node).lnode instanceof BinaryOperatorNode,
                            "run:binaryop:step:expected range as lvalue");
                    Assert.require(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).operator.c.equals(":"),
                            "run:binaryop:step:expected range as lvalue");
                    QType rangeStart = run(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).lnode, scope);
                    QType rangeEnd = run(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).rnode, scope);
                    QType rangeStep = run(((BinaryOperatorNode) node).rnode, scope);
                    Assert.require(QType.isNum(rangeStart, rangeEnd, rangeStep),
                            "run:binaryop:step:expected numbers");
                    double iter = ((NumType) rangeStart).value;
                    boolean canRun = true;
                    ListType l = new ListType();
                    while (canRun) {
                        l.values.add(new NumType(iter));
                        iter += ((NumType) rangeStep).value;
                        if (((NumType) rangeStart).value < ((NumType) rangeEnd).value) {
                            if (((NumType) rangeEnd).value < iter)
                                canRun = false;
                        } else {
                            if (((NumType) rangeEnd).value > iter)
                                canRun = false;
                        }
                    }
                    return l;
                }
            }
            throw new RuntimeStriker("run:binaryop:no valid case for " + node);

        } else if (node instanceof BlockNode) {
            for (Node n : ((BlockNode) node).nodes)
                run(n, scope);

        } else if (node instanceof EveryBlockNode) {
            QType range = run(((EveryBlockNode) node).expr, scope);
            if (!(QType.isList(range) || QType.isStr(range))) // TODO container every-loop
                throw new RuntimeStriker("run:every-loop:invalid range");
            if (QType.isList(range)) {
                for (int i = 0; i < ((ListType) range).values.size(); i++) {
                    scope.set(((EveryBlockNode) node).variable.token.c,
                            ((ListType) range).values.get(i));
                    try {
                        run(((EveryBlockNode) node).nodes, scope);
                    } catch (RuntimeStriker striker) {
                        if (striker.type.equals(RuntimeStrikerType.BREAK))
                            break;
                        else if (striker.type.equals(RuntimeStrikerType.CONTINUE))
                            continue;
                        else if (striker.type.equals(RuntimeStrikerType.RETURN))
                            throw striker;
                        else if (striker.type.equals(RuntimeStrikerType.EXCEPTION))
                            throw striker;
                    }
                }
            } else if (QType.isStr(range)) {
                for (int i = 0; i < ((StringType) range).value.length(); i++) {
                    scope.set(((VariableNode) ((EveryBlockNode) node).variable).token.c,
                            new StringType(((StringType) range).value.charAt(i) + ""));
                    try {
                        run(((EveryBlockNode) node).nodes, scope);
                    } catch (RuntimeStriker striker) {
                        if (striker.type.equals(RuntimeStrikerType.BREAK))
                            break;
                        else if (striker.type.equals(RuntimeStrikerType.CONTINUE))
                            continue;
                        else if (striker.type.equals(RuntimeStrikerType.RETURN))
                            throw striker;
                        else if (striker.type.equals(RuntimeStrikerType.EXCEPTION))
                            throw striker;
                    }
                }
            }

        } else if (node instanceof FieldReferenceNode) {
            if (!(((FieldReferenceNode) node).rnode instanceof VariableNode))
                throw new RuntimeStriker("run:field set:cannot get value of non-variable type");
            return run(((FieldReferenceNode) node).lnode, scope).table.get(
                    ((VariableNode) ((FieldReferenceNode) node).rnode).token.c);

        } else if (node instanceof FieldSetNode) {
            QType parent = run(((FieldSetNode) node).lnode, scope);
            if (!(((FieldSetNode) node).rnode instanceof VariableNode))
                throw new RuntimeStriker("run:field set:cannot get value of non-variable type");
            parent.table.put(((VariableNode) ((FieldSetNode) node).rnode).token.c,
                    run(((FieldSetNode) node).value, scope));

        } else if (node instanceof FunctionCallNode) {
            QType parent = null;
            boolean isMetacall = false;
            if (((FunctionCallNode) node).id instanceof FieldReferenceNode) {
                parent = run(((FieldReferenceNode) ((FunctionCallNode) node).id).lnode, scope);
                isMetacall = !(parent instanceof ContainerType) || ((ContainerType) parent).isMeta();
            }
            List<QType> args = new ArrayList<>();
            if (isMetacall) args.add(parent);
            for (Node arg : ((MultiElementNode) ((FunctionCallNode) node).args).nodes) args.add(run(arg, scope));
            QType callee = run(((FunctionCallNode) node).id, scope);
            if (callee == null || callee instanceof VoidType)
                throw new RuntimeStriker("run:call:cannot call null");
            if (callee instanceof FuncType) {
                return ((FuncType) callee).run(this, args);
            } else if (callee instanceof ContainerType) {
                ContainerType ct = (ContainerType) callee;
                if (ct.table.containsKey("_builder")) {
                    if (!(ct.table.get("_builder") instanceof FuncType))
                        throw new RuntimeStriker("run:function:cannot call " + callee);
                    List<QType> builderArgs = new ArrayList<>(Collections.singletonList(ct));
                    builderArgs.addAll(args);
                    return ((FuncType) ct.table.get("_builder")).run(this, builderArgs);
                }
            }

        } else if (node instanceof EffectNode) {
            switch (((EffectNode) node).operator.c) {
                case "assert": {
                    QType v = run(((EffectNode) node).operand, scope);
                    if (v instanceof VoidType || (v instanceof BoolType && !((BoolType) v).value))
                        throw new RuntimeStriker("assert:" + ((EffectNode) node).operand.toString());
                }
                case "use":
                case "using":
                case "deploy": {
                    String lib = ((EffectNode) node).operand instanceof LiteralStringNode?
                            ((LiteralStringNode) ((EffectNode) node).operand).token.c :
                            (((EffectNode) node).operand instanceof VariableNode?
                            ((VariableNode) ((EffectNode) node).operand).token.c : null);
                    if (lib == null) throw new RuntimeStriker("run:use:invalid operand");
                    String id = lib.replaceAll("/", "_");
                    id = id.replaceAll(":", "_");
                    id = id.replaceAll("\\\\", "_");
                    id = id.replaceAll("\\.", "_");
                    QType loaded = null;
                    if (Runtime.nativeLibs.contains(lib))
                        loaded = getNative(lib);
                    else {
                        String code = IOManager.loadLibrary(lib);
                        Lexer lexer = new Lexer(code);
                        List<Token> tokens = lexer.lexAndFix();
                        Parser parser = new Parser(tokens);
                        Node node1 = parser.parse();
                        Runtime runtime = new Runtime(node1, io);
                        loaded = runtime.run(node1, runtime.scope);
                    }
                    if (((EffectNode) node).operator.c.equals("deploy"))
                        for (String key : loaded.table.keySet())
                            if (!key.startsWith("_"))
                                scope.set(key, loaded.table.get(key));
                    else
                        scope.set(id, loaded);
                    break;
                }
                case "throw": {
                    throw new RuntimeStriker(run(((EffectNode) node).operand, scope).toString());
                }
                case "return": {
                    throw new RuntimeStriker(run(((EffectNode) node).operand, scope));
                }
            }

        } else if (node instanceof IfBlockNode) {
            QType condition = run(((IfBlockNode) node).condition, scope);
            if (condition instanceof BoolType && ((BoolType) condition).value) {
                run(((IfBlockNode) node).nodes, scope);
            } else {
                for (Node linked : ((IfBlockNode) node).linkedNodes) {
                    if (linked instanceof ElseBlockNode) {
                        run(((IfBlockNode) node).nodes, scope);
                        break;
                    } else if (linked instanceof ElseIfBlockNode) {
                        QType elseIfCondition = run(((IfBlockNode) node).condition, scope);
                        if (elseIfCondition instanceof BoolType && ((BoolType) elseIfCondition).value) {
                            run(((IfBlockNode) node).nodes, scope);
                            break;
                        }
                    }
                }
            }

        } else if (node instanceof InstructionNode) {
            switch (((InstructionNode) node).token.c) {
                case "break": throw new RuntimeStriker(RuntimeStrikerType.BREAK);
                case "continue": throw new RuntimeStriker(RuntimeStrikerType.CONTINUE);
                case "breakpoint": {
                    io.consolePut(scope.dump() + "\n");
                    io.consoleInput("Press ENTER to continue");
                    break;
                }
                case "memory": {
                    io.consolePut(scope.dump() + "\n");
                    break;
                }
            }

        } else if (node instanceof LiteralBoolNode) {
            return new BoolType(((LiteralBoolNode) node).token.c.equals("true"));

        } else if (node instanceof LiteralContainerNode) {
            ContainerType container = new ContainerType(
                    ((LiteralContainerNode) node).name,
                    ((LiteralContainerNode) node).alike,
                    new HashMap<>(),
                    ((LiteralContainerNode) node).isMeta
            );
            QType parent = scope.get(((LiteralContainerNode) node).alike);
            if (!container.like().equals("container")) {
                if (!(parent instanceof ContainerType))
                    throw new RuntimeStriker("run:container" + ((LiteralContainerNode) node).name +
                            ":Inheritance error, cannot inherit from a non-container type");
                container = inheritContainer((ContainerType) parent, container);
            }
            for (Node n : ((LiteralContainerNode) node).initialize) {
                if (n instanceof BinaryOperatorNode && ((BinaryOperatorNode) n).lnode instanceof VariableNode) {
                    container.table.put(((VariableNode) ((BinaryOperatorNode) n).lnode).token.c,
                            run(((BinaryOperatorNode) n).rnode, scope));
                } else if (n instanceof LiteralFunctionNode) {
                    List<String> args = new ArrayList<>();
                    for (Node arg : ((MultiElementNode) ((LiteralFunctionNode) n).args).nodes)
                        if (arg instanceof VariableNode)
                            args.add(((VariableNode) arg).token.c);
                    FuncType f = new FuncType(((LiteralFunctionNode) n).name.c,
                            args, ((LiteralFunctionNode) n).code, ((LiteralFunctionNode) n).isStatic);
                    container.table.put(((LiteralFunctionNode) n).name.c, f);
                }
            }
            scope.set(((LiteralContainerNode) node).name, container);

        } else if (node instanceof LiteralEventNode) {
            // TODO

        } else if (node instanceof LiteralFunctionNode) {
            List<String> args = new ArrayList<>();
            for (Node arg : Parser.multiElementIfNeeded(((LiteralFunctionNode) node).args).nodes)
                if (arg instanceof VariableNode)
                    args.add(((VariableNode) arg).token.c);
            FuncType f = new FuncType(((LiteralFunctionNode) node).name.c,
                    args, ((LiteralFunctionNode) node).code, ((LiteralFunctionNode) node).isStatic);
            scope.set(((LiteralFunctionNode) node).name.c, f);
            return f;

        } else if (node instanceof LiteralListNode) {
            ListType list = new ListType();
            for (Node n : ((LiteralListNode) node).nodes)
                list.values.add(run(n, scope));
            return list;

        } else if (node instanceof LiteralNullNode) {
            return new VoidType();

        } else if (node instanceof LiteralNumNode) {
            return new NumType(Double.parseDouble(((LiteralNumNode) node).token.c));

        } else if (node instanceof LiteralStringNode) {
            return new StringType(((LiteralStringNode) node).token.c);

        } else if (node instanceof LoopStopBlockNode) {
            while (true) {
                try {
                    run(((LoopStopBlockNode) node).nodes, scope);
                    QType condition = run(((LoopStopBlockNode) node).condition, scope);
                    if (condition instanceof BoolType && ((BoolType) condition).value)
                        break;
                } catch (RuntimeStriker striker) {
                    if (striker.type.equals(RuntimeStrikerType.BREAK))
                        break;
                    else if (striker.type.equals(RuntimeStrikerType.CONTINUE))
                        continue;
                    else if (striker.type.equals(RuntimeStrikerType.RETURN))
                        throw striker;
                    else if (striker.type.equals(RuntimeStrikerType.EXCEPTION))
                        throw striker;
                }
            }

        } else if (node instanceof MultiElementNode) {
            ListType list = new ListType();
            for (Node n : ((MultiElementNode) node).nodes)
                list.values.add(run(n, scope));
            return list;

        } else if (node instanceof ThroughBlockNode) {
            QType baseV = null;
            QType ceilV = null;
            QType stepV = null;
            if (((ThroughBlockNode) node).range.operator.c.equals("step")) {
                stepV = run(((ThroughBlockNode) node).range.rnode, scope);
                baseV = run(((BinaryOperatorNode) ((ThroughBlockNode) node).range.lnode).lnode, scope);
                ceilV = run(((BinaryOperatorNode) ((ThroughBlockNode) node).range.lnode).rnode, scope);
            } else {
                baseV = run(((ThroughBlockNode) node).range.lnode, scope);
                ceilV = run(((ThroughBlockNode) node).range.rnode, scope);
            }
            if (!QType.isNum(baseV, ceilV))
                throw new RuntimeStriker("run:through:There are some non-num values");
            if (!(stepV instanceof NumType) && stepV != null)
                throw new RuntimeStriker("run:through:There are some non-num values");
            double iterator = ((NumType) baseV).value;
            double till = ((NumType) ceilV).value;
            double step = stepV == null? (iterator > till? -1 : 1) : ((NumType) stepV).value;
            while (true) {
                if (((NumType) baseV).value < till) if (iterator > till) break;
                if (((NumType) baseV).value > till) if (iterator < till) break;
                scope.set(((ThroughBlockNode) node).variable.token.c,
                        new NumType(iterator));
                try {
                    run(((ThroughBlockNode) node).nodes, scope);
                } catch (RuntimeStriker striker) {
                    if (striker.type.equals(RuntimeStrikerType.BREAK))
                        break;
                    else if (striker.type.equals(RuntimeStrikerType.CONTINUE))
                        continue;
                    else if (striker.type.equals(RuntimeStrikerType.RETURN))
                        throw striker;
                    else if (striker.type.equals(RuntimeStrikerType.EXCEPTION))
                        throw striker;
                }
                iterator += step;
            }

        } else if (node instanceof TryCatchBlockNode) {
            try {
                run(((TryCatchBlockNode) node).tryNodes, scope);
            } catch (RuntimeStriker striker) {
                scope.set(((TryCatchBlockNode) node).variable.token.c, striker.val);
                run(((TryCatchBlockNode) node).catchNodes, scope);
            }

        } else if (node instanceof UnaryOperatorNode) {
            switch (((UnaryOperatorNode) node).operator.c) {
                case "!":
                case "not": {
                    QType v = run(((UnaryOperatorNode) node).operand, scope);
                    Assert.require(QType.isBool(v),
                            "run:unary:!:Non-bool operand");
                    return new BoolType(!((BoolType) v).value);
                }
                case "negate":
                case "-": {
                    QType v = run(((UnaryOperatorNode) node).operand, scope);
                    Assert.require(QType.isNum(v),
                            "run:unary:-:Non-num operand");
                    return new NumType(-((NumType) v).value);
                }
                case "notnull":
                case "exists": {
                    QType v = run(((UnaryOperatorNode) node).operand, scope);
                    return new BoolType(!(v instanceof VoidType));
                }
            }

        } else if (node instanceof VariableNode) {
            return scope.get(((VariableNode) node).token.c);

        } else if (node instanceof WhileBlockNode) {
            while (true) {
                try {
                    QType condition = run(((WhileBlockNode) node).condition, scope);
                    if (condition instanceof BoolType && !((BoolType) condition).value)
                        break;
                    run(((WhileBlockNode) node).nodes, scope);
                } catch (RuntimeStriker striker) {
                    if (striker.type.equals(RuntimeStrikerType.BREAK))
                        break;
                    else if (striker.type.equals(RuntimeStrikerType.CONTINUE))
                        continue;
                    else if (striker.type.equals(RuntimeStrikerType.RETURN))
                        throw striker;
                    else if (striker.type.equals(RuntimeStrikerType.EXCEPTION))
                        throw striker;
                }
            }
        }

        return Void;
    }
}
