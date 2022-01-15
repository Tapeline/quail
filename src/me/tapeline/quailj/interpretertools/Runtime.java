package me.tapeline.quailj.interpretertools;

import me.tapeline.quailj.QFileReader;
import me.tapeline.quailj.debugtools.AdvancedActionLogger;
import me.tapeline.quailj.language.types.*;
import me.tapeline.quailj.parsingtools.Parser;
import me.tapeline.quailj.parsingtools.nodes.*;
import me.tapeline.quailj.tokenizetools.Lexer;
import me.tapeline.quailj.utils.ListUtils;
import me.tapeline.quailj.utils.StringUtils;
import me.tapeline.quailj.utils.Utilities;

import java.util.*;

public class Runtime {

    public static VoidType Void = new VoidType(); // idk, maybe del dat
    public static List<String> nativeLibs = new ArrayList<>(Arrays.asList(
            "canvas",
            "popups"
    ));

    public final Node rootNode;
    private final AdvancedActionLogger aal;
    public Memory scope;
    public final RuntimeConfig config;
    public final HashMap<String, FuncType> eventHandlers = new HashMap<>();

    public Runtime(Node rootNode, RuntimeConfig cfg, AdvancedActionLogger aal) {
        this.rootNode = rootNode;
        this.scope = new Memory();
        this.config = cfg;
        this.aal = aal;
        defineBuiltIns();
    }

    public void defineBuiltIns() {
        this.scope.set("clock", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) {
                return new NumType((double) System.currentTimeMillis() / 1000.0);
            }
        });
        this.scope.set("nothing", new VoidType());
    }

    public QType getNativeLib(String name) {
        switch (name) {
            case "canvas": {
                return Void;
            }
            case "popups": {
                return Void;
            }
        }
        return Void;
    }

    public ContainerType overrideContainerContents(ContainerType dest, ContainerType src) {
        dest.content.putAll(src.content);
        dest.customBehaviour.putAll(src.customBehaviour);
        if (src.builder != null && !(src.builder.code.nodes.get(0) instanceof UnaryOperatorNode &&
                ((UnaryOperatorNode) src.builder.code.nodes.get(0)).operator.c.equals("return") &&
                ((UnaryOperatorNode) src.builder.code.nodes.get(0)).operand instanceof LiteralNullNode))
            dest.builder = src.builder;
        return dest;
    }

    public ContainerType inheritContainer(ContainerType parent, ContainerType child, boolean primary) {
        if (parent.like.equals("container")) {
            child = overrideContainerContents(child, parent);
            return child;
        } else {
            QType p = scope.get(parent.like);
            if (p instanceof ContainerType) {
                parent = inheritContainer((ContainerType) p, parent, true);
                child = overrideContainerContents(child, parent);
                return child;
            }
        }
        return child;
    }

    public QType run(Node node) throws RuntimeStriker {
        if (node instanceof BinaryOperatorNode) {
            QType a = run(((BinaryOperatorNode) node).lnode);
            QType b = run(((BinaryOperatorNode) node).rnode);
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
                        switch (((BinaryOperatorNode) node).operator.c) {
                            case "+": return ListUtils.concat((ListType) a, (ListType) b);
                            case "-": return new ListType(ListUtils.removeAll(((ListType) a).values,
                                    ((ListType) b).values));
                        }
                    } else if (QType.isStr(a, b)) {
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
                        if (QType.isCont(a) && ( ((ContainerType) a).name.equals(((StringType) b).value) ||
                                ((ContainerType) a).like.equals(((StringType) b).value)))
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
                    scope.set(((VariableNode) ((BinaryOperatorNode) node).lnode).token.c,
                            run(((BinaryOperatorNode) node).rnode));
                    return Void;
                }
            }
            throw new RuntimeStriker("run:binaryop:no valid case for " + node);
        } else if (node instanceof BlockNode) {
            for (Node n : ((BlockNode) node).nodes)
                run(n);
        } else if (node instanceof EveryBlockNode) {
            QType range = run(((EveryBlockNode) node).expr);
            if (!(QType.isList(range) || QType.isStr(range))) // TODO container every-loop
                throw new RuntimeStriker("run:everyloop:invalid range");
            if (!(((EveryBlockNode) node).variable instanceof VariableNode))
                throw new RuntimeStriker("run:everyloop:range iterator");
            if (QType.isList(range)) {
                for (int i = 0; i < ((ListType) range).values.size(); i++) {
                    scope.set(((VariableNode) ((EveryBlockNode) node).variable).token.c,
                            ((ListType) range).values.get(i));
                    try {
                        run(((EveryBlockNode) node).nodes);
                    } catch (RuntimeStriker striker) {
                        if (striker.type.equals(RuntimeStrikerTypes.BREAK))
                            break;
                        else if (striker.type.equals(RuntimeStrikerTypes.CONTINUE))
                            continue;
                        else if (striker.type.equals(RuntimeStrikerTypes.RETURN))
                            throw striker;
                        else if (striker.type.equals(RuntimeStrikerTypes.EXCEPTION))
                            throw striker;
                    }
                }
            } else if (QType.isStr(range)) {
                for (int i = 0; i < ((StringType) range).value.length(); i++) {
                    scope.set(((VariableNode) ((EveryBlockNode) node).variable).token.c,
                            new StringType(((StringType) range).value.charAt(i) + ""));
                    try {
                        run(((EveryBlockNode) node).nodes);
                    } catch (RuntimeStriker striker) {
                        if (striker.type.equals(RuntimeStrikerTypes.BREAK))
                            break;
                        else if (striker.type.equals(RuntimeStrikerTypes.CONTINUE))
                            continue;
                        else if (striker.type.equals(RuntimeStrikerTypes.RETURN))
                            throw striker;
                        else if (striker.type.equals(RuntimeStrikerTypes.EXCEPTION))
                            throw striker;
                    }
                }
            }
        } else if (node instanceof FieldReferenceNode) {
            // TODO field ref. run()
        } else if (node instanceof FieldSetNode) {
            // TODO field set. run()
        } else if (node instanceof FunctionCallNode) {
            QType callee = run(((FunctionCallNode) node).id);
            if (callee == null)
                throw new RuntimeStriker("run:function:cannot call null " + ((FunctionCallNode) node).id.toString());
            List<QType> args = new ArrayList<>();
            for (Node arg : ((MultiElementNode) ((FunctionCallNode) node).args).nodes) {
                args.add(run(arg));
            }
            if (callee instanceof FuncType)
                return ((FuncType) callee).run(this, args);
            else if (callee instanceof BuiltinFuncType)
                return ((BuiltinFuncType) callee).run(this, args);
            else
                throw new RuntimeStriker("run:function:cannot call " + callee.toString());
        } else if (node instanceof GroupNode) {
            return run(((GroupNode) node).node);
        } else if (node instanceof IfBlockNode) {
            QType cond = run(((IfBlockNode) node).condition);
            if (cond instanceof BoolType && ((BoolType) cond).value) {
                run(((IfBlockNode) node).nodes);
            } else {
                for (Node branch : ((IfBlockNode) node).linkedNodes) {
                    if (branch instanceof ElseIfBlockNode) {
                        QType condElseIf = run(((ElseIfBlockNode) branch).condition);
                        if (condElseIf instanceof BoolType && ((BoolType) condElseIf).value) {
                            run(((ElseIfBlockNode) branch).nodes);
                            break;
                        }
                    } else if (branch instanceof ElseBlockNode) {
                        run(((ElseBlockNode) branch).nodes);
                        break;
                    }
                }
            }
        } else if (node instanceof InstructionNode) {
            switch (((InstructionNode) node).operator.c) {
                case "milestone":
                case "memory":
                case "breakpoint": {
                    System.out.println(scope.dump());
                    if (((InstructionNode) node).operator.c.equals("breakpoint")) {
                        Scanner sc = new Scanner(System.in);
                        System.out.println("\nType `c` and press ENTER to continue > ");
                        sc.next();
                    }
                    break;
                }
                case "break":
                    throw new RuntimeStriker(RuntimeStrikerTypes.BREAK);
                case "continue":
                    throw new RuntimeStriker(RuntimeStrikerTypes.CONTINUE);
            }
        } else if (node instanceof LiteralNumNode) {
            return new NumType(Double.parseDouble(((LiteralNumNode) node).token.c));
        } else if (node instanceof LiteralStringNode) {
            return new StringType(((LiteralStringNode) node).token.c);
        } else if (node instanceof LiteralBoolNode) {
            return new BoolType(Boolean.parseBoolean(((LiteralBoolNode) node).token.c));
        } else if (node instanceof LiteralContainerNode) {
            if (((LiteralContainerNode) node).isMeta) {
                ContainerType ct = new ContainerType(new HashMap<String, QType>(), true);
                if (!((LiteralContainerNode) node).alike.equals("container")) {
                    ct.like = ((LiteralContainerNode) node).alike;
                    QType parent = scope.get(ct.like);
                    if (parent instanceof ContainerType) {
                        ct = inheritContainer((ContainerType) parent, ct, true);
                    }
                }
                for (Node init : ((LiteralContainerNode) node).initialize) {
                    if (init instanceof LiteralDefinitionNode) {
                        switch (((LiteralDefinitionNode) init).type.c) {
                            case "string": {
                                ct.content.put(((LiteralDefinitionNode) init).variable.c, new StringType(""));
                                return Void;
                            }
                            case "num": {
                                ct.content.put(((LiteralDefinitionNode) init).variable.c, new NumType(0));
                                return Void;
                            }
                            case "bool": {
                                ct.content.put(((LiteralDefinitionNode) init).variable.c, new BoolType(false));
                                return Void;
                            }
                        }
                    } else if (init instanceof BinaryOperatorNode &&
                            ((BinaryOperatorNode) init).operator.c.equals("=")) {
                        if (!(((BinaryOperatorNode) init).lnode instanceof VariableNode))
                            throw new RuntimeStriker("run:literal-container:init:cannot set value to non-variable");
                        ct.content.put(((VariableNode) ((BinaryOperatorNode) init).lnode).token.c,
                                run(((BinaryOperatorNode) init).rnode));
                    } else if (init instanceof LiteralFunctionNode) {
                        List<String> args = new ArrayList<>();
                        for (Node n : ((MultiElementNode) ((LiteralFunctionNode) init).args).nodes) {
                            if (n instanceof VariableNode)
                                args.add(((VariableNode) n).token.c);
                            else throw new RuntimeStriker("run:literal-container:init:literal-function:invalid args");
                        }
                        FuncType f = new FuncType(((LiteralFunctionNode) init).name.c,
                                args, ((LiteralFunctionNode) init).code);
                        ct.content.put(((LiteralFunctionNode) init).name.c, f);
                    }
                }
                if (((LiteralContainerNode) node).builder.name.c.equals("no-builder")) {
                    if (ct.builder != null && !(ct.builder.code.nodes.get(0) instanceof UnaryOperatorNode &&
                            ((UnaryOperatorNode) ct.builder.code.nodes.get(0)).operator.c.equals("return") &&
                            ((UnaryOperatorNode) ct.builder.code.nodes.get(0)).operand instanceof LiteralNullNode)) {
                        ct.builder = new FuncType("__builder__", new ArrayList<String>(), new BlockNode());
                    }
                } else {
                    List<String> args = new ArrayList<>();
                    for (Node n : ((MultiElementNode) ((LiteralContainerNode) node).builder.args).nodes) {
                        if (n instanceof VariableNode)
                            args.add(((VariableNode) n).token.c);
                        else throw new RuntimeStriker("run:literal-container:init:builder:invalid args");
                    }
                    ct.builder = new FuncType("__builder__", args, ((LiteralContainerNode) node).builder.code);
                }
                ct.name = ((LiteralContainerNode) node).name;
                ct.like = ((LiteralContainerNode) node).alike;
                scope.set(((LiteralContainerNode) node).name, ct);
            } else if (!((LiteralContainerNode) node).isMeta) {
                HashMap<String, QType> content = new HashMap<>();
                for (Node init : ((LiteralContainerNode) node).initialize) {
                    if (init instanceof LiteralDefinitionNode) {
                        switch (((LiteralDefinitionNode) init).type.c) {
                            case "string": {
                                content.put(((LiteralDefinitionNode) init).variable.c, new StringType(""));
                                return Void;
                            }
                            case "num": {
                                content.put(((LiteralDefinitionNode) init).variable.c, new NumType(0));
                                return Void;
                            }
                            case "bool": {
                                content.put(((LiteralDefinitionNode) init).variable.c, new BoolType(false));
                                return Void;
                            }
                        }
                    } else if (init instanceof BinaryOperatorNode &&
                            ((BinaryOperatorNode) init).operator.c.equals("=")) {
                        if (!(((BinaryOperatorNode) init).lnode instanceof VariableNode))
                            throw new RuntimeStriker("run:literal-container:init:cannot set value to non-variable");
                        content.put(((VariableNode) ((BinaryOperatorNode) init).lnode).token.c,
                                run(((BinaryOperatorNode) init).rnode));
                    } else if (init instanceof LiteralFunctionNode) {
                        List<String> args = new ArrayList<>();
                        for (Node n : ((MultiElementNode) ((LiteralFunctionNode) init).args).nodes) {
                            if (n instanceof VariableNode)
                                args.add(((VariableNode) n).token.c);
                            else throw new RuntimeStriker("run:literal-container:init:literal-function:invalid args");
                        }
                        FuncType f = new FuncType(((LiteralFunctionNode) init).name.c,
                                args, ((LiteralFunctionNode) init).code);
                        content.put(((LiteralFunctionNode) init).name.c, f);
                    }
                }
                ContainerType ct = new ContainerType(content, false);
                ct.name = ((LiteralContainerNode) node).name;
                if (((LiteralContainerNode) node).name.equals("_anonymous"))
                    return ct;
                else scope.set(((LiteralContainerNode) node).name, ct);
            }
        } else if (node instanceof LiteralDefinitionNode) {
            switch (((LiteralDefinitionNode) node).type.c) {
                case "string": {
                    scope.set(((LiteralDefinitionNode) node).variable.c, new StringType(""));
                    return Void;
                }
                case "num": {
                    scope.set(((LiteralDefinitionNode) node).variable.c, new NumType(0));
                    return Void;
                }
                case "bool": {
                    scope.set(((LiteralDefinitionNode) node).variable.c, new BoolType(false));
                    return Void;
                }
            }
        } else if (node instanceof LiteralEventNode) {
            FuncType f = new FuncType(((LiteralEventNode) node).name.c,
                    Collections.singletonList(((LiteralEventNode) node).var.c),
                    ((LiteralEventNode) node).code);
            eventHandlers.put(((LiteralEventNode) node).name.c, f);
        } else if (node instanceof LiteralFunctionNode) {
            List<String> args = new ArrayList<>();
            for (Node n : ((MultiElementNode) ((LiteralFunctionNode) node).args).nodes) {
                if (n instanceof VariableNode)
                    args.add(((VariableNode) n).token.c);
                else throw new RuntimeStriker("run:literalfunction:invalid args");
            }
            FuncType f = new FuncType(((LiteralFunctionNode) node).name.c,
                    args, ((LiteralFunctionNode) node).code);
            if (f.name.equals("that"))
                return f;
            else
                scope.set(f.name, f);
        } else if (node instanceof LiteralListNode) {
            ListType l = new ListType();
            for (Node n : ((LiteralListNode) node).nodes)
                l.values.add(run(n));
            return l;
        } else if (node instanceof LiteralNullNode) {
            return Void;
        } else if (node instanceof LoopStopBlockNode) {
            while (true) {
                try {
                    run(((LoopStopBlockNode) node).nodes);
                } catch (RuntimeStriker striker) {
                    if (striker.type.equals(RuntimeStrikerTypes.BREAK))
                        break;
                    else if (striker.type.equals(RuntimeStrikerTypes.CONTINUE))
                        continue;
                    else if (striker.type.equals(RuntimeStrikerTypes.RETURN))
                        throw striker;
                    else if (striker.type.equals(RuntimeStrikerTypes.EXCEPTION))
                        throw striker;
                }
                QType cond = run(((LoopStopBlockNode) node).condition);
                if (!(cond instanceof BoolType && ((BoolType) cond).value))
                    return Void;
            }
        } else if (node instanceof ThroughBlockNode) {
            double iterator;
            double threshold;
            QType rangeStart = run(((ThroughBlockNode) node).range.lnode);
            QType rangeEnd = run(((ThroughBlockNode) node).range.rnode);
            if (!(QType.isNum(rangeStart) && QType.isNum(rangeEnd)))
                throw new RuntimeStriker("run:through:cannot iterate through non-num range");
            else {
                iterator = ((NumType) rangeStart).value;
                threshold = ((NumType) rangeEnd).value;
            }
            double step = ((NumType) rangeStart).value < threshold ? 1 : -1;
            if (!(((ThroughBlockNode) node).variable instanceof VariableNode))
                throw new RuntimeStriker("run:through:cannot iterate with non-variable iterator");
            while (iterator < threshold) {
                try {
                    scope.set(((VariableNode) ((ThroughBlockNode) node).variable).token.c,
                            new NumType(iterator));
                    run(((ThroughBlockNode) node).nodes);
                    iterator += step;
                } catch (RuntimeStriker striker) {
                    if (striker.type.equals(RuntimeStrikerTypes.BREAK))
                        break;
                    else if (striker.type.equals(RuntimeStrikerTypes.CONTINUE))
                        continue;
                    else if (striker.type.equals(RuntimeStrikerTypes.RETURN))
                        throw striker;
                    else if (striker.type.equals(RuntimeStrikerTypes.EXCEPTION))
                        throw striker;
                }
            }
        } else if (node instanceof TryCatchNode) {
            try {
                run(((TryCatchNode) node).tryNodes);
            } catch (RuntimeStriker striker) {
                if (striker.type.equals(RuntimeStrikerTypes.EXCEPTION)) {
                    scope.set(((TryCatchNode) node).variable.token.c, new StringType(striker.msg));
                    run(((TryCatchNode) node).catchNodes);
                    scope.remove(((TryCatchNode) node).variable.token.c);
                }
            }
        } else if (node instanceof UnaryOperatorNode) {
            switch (((UnaryOperatorNode) node).operator.c) {
                case "reference to": {
                    if (((UnaryOperatorNode) node).operand instanceof VariableNode)
                        return new RefType(((VariableNode) ((UnaryOperatorNode) node).operand).token.c);
                    else
                        throw new RuntimeStriker(
                                "run:unaryop:reference:cannot make reference to non-variable value");
                }
                case "not":
                case "negate": {
                    QType v = run(((UnaryOperatorNode) node).operand);
                    if (v instanceof BoolType)
                        return new BoolType(!((BoolType) v).value);
                    else if (v instanceof NumType)
                        return new NumType(-((NumType) v).value);
                    else throw new RuntimeStriker("run:unaryop:not-negate:unacceptable value");
                }
                case "my": {
                    break; // TODO WITH OOP
                }
                case "out": {
                    System.out.println(run(((UnaryOperatorNode) node).operand).toString());
                }
                case "put": {
                    System.out.print(run(((UnaryOperatorNode) node).operand).toString());
                }
                case "input": {
                    Scanner sc = new Scanner(System.in);
                    return new StringType(sc.nextLine());
                }
                case "exists": {
                    if (((UnaryOperatorNode) node).operand instanceof VariableNode)
                        return new BoolType(scope.get(((VariableNode)
                                ((UnaryOperatorNode) node).operand).token.c) != null);
                    else
                        throw new RuntimeStriker(
                                "run:unaryop:exists:cannot check non-variable value");
                }
                case "destroy": {
                    if (((UnaryOperatorNode) node).operand instanceof VariableNode)
                        scope.remove(((VariableNode) ((UnaryOperatorNode) node).operand).token.c);
                    else
                        throw new RuntimeStriker(
                                "run:unaryop:exists:cannot destroy non-variable value");
                }
                case "assert": {
                    QType v = run(((UnaryOperatorNode) node).operand);
                    if (v instanceof BoolType && ((BoolType) v).value)
                        return new BoolType(true);
                    else throw new RuntimeStriker("assertion error");
                }
                case "notnull": {
                    QType v = run(((UnaryOperatorNode) node).operand);
                    return new BoolType(v != null && !(v instanceof VoidType));
                }
                case "throw": {
                    throw new RuntimeStriker(run(((UnaryOperatorNode) node).operand).toString());
                }
                case "use":
                case "using":
                case "deploy": {
                    String path = "";
                    if (((UnaryOperatorNode) node).operand instanceof VariableNode) {
                        path = ((VariableNode) ((UnaryOperatorNode) node).operand).token.c;
                    } else if (((UnaryOperatorNode) node).operand instanceof FieldReferenceNode) {
                        path = QFileReader.resolvePathFromDotNotation(((UnaryOperatorNode) node).operand);
                    } else if (((UnaryOperatorNode) node).operand instanceof LiteralStringNode) {
                        path = ((LiteralStringNode) ((UnaryOperatorNode) node).operand).token.c;
                    }

                    if (!nativeLibs.contains(path)) {
                        String lib = QFileReader.loadLibrary(path);
                        if (lib == null) throw new RuntimeStriker("run:use:import failed");

                        Lexer l = new Lexer(lib, aal);
                        l.lex();

                        Parser p = new Parser(l.fixBooleans(), aal);

                        Runtime compiler = new Runtime(p.parseCode(), new RuntimeConfig(), aal);

                        QType compiled = compiler.runTree();

                        scope.set(path.split("/")[path.split("/").length - 1], compiled);
                    } else {
                        scope.set(path, getNativeLib(path));
                    }
                    return Void;
                }
                case "return": {
                    throw new RuntimeStriker(run(((UnaryOperatorNode) node).operand));
                }
            }
        } else if (node instanceof VariableNode) {
            return scope.get(((VariableNode) node).token.c);
        } else if (node instanceof WhileBlockNode) {
            while (true) {
                QType cond = run(((WhileBlockNode) node).condition);
                if (!(cond instanceof BoolType && ((BoolType) cond).value))
                    return Void;
                try {
                    run(((WhileBlockNode) node).nodes);
                } catch (RuntimeStriker striker) {
                    if (striker.type.equals(RuntimeStrikerTypes.BREAK))
                        break;
                    else if (striker.type.equals(RuntimeStrikerTypes.CONTINUE))
                        continue;
                    else if (striker.type.equals(RuntimeStrikerTypes.RETURN))
                        throw striker;
                    else if (striker.type.equals(RuntimeStrikerTypes.EXCEPTION))
                        throw striker;
                }
            }
        } else if (node instanceof RootNode) {
            for (Node n : ((RootNode) node).nodes)
                run(n);
        }
        return Void;
    }

    public QType runTree() throws RuntimeStriker {
        try {
            if (config.DEBUG) System.out.println(rootNode.toString());
            return run(rootNode);
        } catch (RuntimeStriker striker) {
            if (striker.type.equals(RuntimeStrikerTypes.RETURN))
                return striker.retVal;
            else throw striker;
        }
    }

}
