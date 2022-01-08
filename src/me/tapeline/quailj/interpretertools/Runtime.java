package me.tapeline.quailj.interpretertools;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.tapeline.quailj.debugtools.AdvancedActionLogger;
import me.tapeline.quailj.language.types.*;
import me.tapeline.quailj.parsingtools.nodes.*;
import me.tapeline.quailj.utils.ListUtils;
import me.tapeline.quailj.utils.StringUtils;
import me.tapeline.quailj.utils.Utilities;

import java.util.*;

public class Runtime {

    public static VoidType Void = new VoidType(); // idk, maybe del dat

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
                    break;
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
            // TODO function run()
        } else if (node instanceof GroupNode) {
            return run(((GroupNode) node).node);
        } else if (node instanceof IfBlockNode) {
            // TODO if node run()
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
            // TODO Literal container node run()
        } else if (node instanceof LiteralDefinitionNode) {
            // TODO Literal def. node run()
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
            // TODO loop-stop run()
        } else if (node instanceof ThroughBlockNode) {
            // TODO through run()
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
                                "run:unaryop:referenceto:cannot make reference to non-variable value");
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
                case "using": {
                    // TODO lib stuff
                }
                case "deploy": {
                    // TODO lib stuff
                }
                case "return": {
                    throw new RuntimeStriker(run(((UnaryOperatorNode) node).operand));
                }
            }
        } else if (node instanceof VariableNode) {
            return scope.get(((VariableNode) node).token.c);
        } else if (node instanceof WhileBlockNode) {
            // TODO while run()
        } else if (node instanceof RootNode) {
            for (Node n : ((RootNode) node).nodes)
                run(n);
        }
        return Void;
    }

    public QType runTree() throws RuntimeStriker {
        if (config.DEBUG) System.out.println(rootNode.toString());
        return run(rootNode);
    }

}
