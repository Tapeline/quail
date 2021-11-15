package me.tapeline.quarkj.interpretertools;

import me.tapeline.quarkj.QFileReader;
import me.tapeline.quarkj.RuntimeWrapper;
import me.tapeline.quarkj.language.types.*;
import me.tapeline.quarkj.parsingtools.nodes.*;
import me.tapeline.quarkj.parsingtools.nodes.Node;
import me.tapeline.quarkj.tokenizetools.tokens.Token;
import me.tapeline.quarkj.tokenizetools.tokens.TokenType;
import me.tapeline.quarkj.utils.ListUtils;
import me.tapeline.quarkj.utils.NumUtils;
import me.tapeline.quarkj.utils.StringUtils;
import me.tapeline.quarkj.utils.Utilities;

import java.util.*;

public class Runtime {

    public final Node rootNode;
    private final String code;
    public Memory scope;
    public final RuntimeConfig config;

    public Runtime(Node rootNode, RuntimeConfig cfg, String code) {
        this.rootNode = rootNode;
        this.scope = new Memory();
        this.config = cfg;
        this.code = code;
    }

    public QType raiseException(String msg, int pos) {
        DirectInstructionType exception = new DirectInstructionType(DirectInstruction.EXCEPTION);
        exception.data.put("msg", new StringType(msg));
        exception.data.put("symbol", new NumType(pos));
        return exception;
    }

    public QType NodeRunner_run(Node node) {
        if (node instanceof LiteralNumNode)
            return new NumType(Double.parseDouble(((LiteralNumNode) node).token.c));

        if (node instanceof LiteralStringNode)
            return new StringType(((LiteralStringNode) node).token.c);

        if (node instanceof LiteralBoolNode)
            return new BoolType(Boolean.parseBoolean(((LiteralBoolNode) node).token.c));

        if (node instanceof LiteralDefinitionNode) {
            switch (((LiteralDefinitionNode) node).type.c) {
                case "bool": {
                    scope.mem.put(((LiteralDefinitionNode) node).variable.c, new BoolType(false));
                    break;
                }
                case "num": {
                    scope.mem.put(((LiteralDefinitionNode) node).variable.c, new NumType(0));
                    break;
                }
                case "string": {
                    scope.mem.put(((LiteralDefinitionNode) node).variable.c, new StringType(""));
                    break;
                }
            }
            return new VoidType(true);
        }

        if (node instanceof LiteralFunctionNode) {
            List<String> args = new ArrayList<>();
            if (((LiteralFunctionNode) node).args instanceof MultiElementNode)
                for (Node n : ((MultiElementNode) ((LiteralFunctionNode) node).args).nodes)
                    args.add(((VariableNode) n).token.c);
            else
                args.add(((VariableNode) ((LiteralFunctionNode) node).args).token.c);
            scope.set(((LiteralFunctionNode) node).name.c, new FuncType(((LiteralFunctionNode) node).name.c,
                    args, ((LiteralFunctionNode) node).code));
            return new VoidType(true);
        }

        if (node instanceof LiteralListNode) {
            ListType list = new ListType();
            for (Node v : ((LiteralListNode) node).nodes)
                list.values.add(NodeRunner_run(v));
            return list;
        }

        if (node instanceof UnaryOperatorNode) {
            switch (((UnaryOperatorNode) node).operator.c) {
                case "out":
                    System.out.println(NodeRunner_run(((UnaryOperatorNode) node).operand));
                    return new VoidType(true);
                case "put":
                    System.out.print(NodeRunner_run(((UnaryOperatorNode) node).operand));
                    return new VoidType(true);
                case "not":
                    QType operand = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    if (operand instanceof BoolType)
                        return new BoolType(!((BoolType) operand).value);
                    else
                        return raiseException("Unable to invert `" + operand.getClass().toString() + "`",
                                ((UnaryOperatorNode) node).operator.p);
                case "input": {
                    System.out.print(NodeRunner_run(((UnaryOperatorNode) node).operand));
                    Scanner sc = new Scanner(System.in);
                    String input = sc.next();
                    return new StringType(input);
                }
                case "destroy":
                    scope.mem.remove(((VariableNode) ((UnaryOperatorNode) node).operand).token.c);
                    return new VoidType(true);
                case "numinput": {
                    System.out.print(NodeRunner_run(((UnaryOperatorNode) node).operand));
                    Scanner sc = new Scanner(System.in);
                    String rInput = sc.next();
                    if (Utilities.isNumeric(rInput)) {
                        double input = Double.parseDouble(rInput);
                        return new NumType(input);
                    } else
                        return raiseException("Unable to convert input string `" + rInput + "`" + " to NumType",
                                ((UnaryOperatorNode) node).operator.p);
                }
                case "boolinput": {
                    System.out.print(NodeRunner_run(((UnaryOperatorNode) node).operand));
                    Scanner sc = new Scanner(System.in);
                    boolean input = sc.nextBoolean();
                    return new BoolType(input);
                }
                case "exists":
                    return new BoolType(scope.mem.containsKey(((VariableNode) ((UnaryOperatorNode) node).operand).token.c));
                case "tostring": {
                    QType valueRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    return new StringType(valueRaw.toString());
                }
                case "tonum": {
                    QType valueRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    if (valueRaw instanceof StringType && Utilities.isNumeric(((StringType) valueRaw).value))
                        return new NumType(Double.parseDouble(((StringType) valueRaw).value));
                    else
                        return raiseException("Unable to convert `" + valueRaw + "` to NumType",
                                ((VariableNode) ((UnaryOperatorNode) node).operand).token.p);
                }
                case "tobool": {
                    QType valueRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    if (valueRaw instanceof StringType && Utilities.isBoolean(((StringType) valueRaw).value))
                        return new BoolType(Boolean.parseBoolean(((StringType) valueRaw).value));
                    else
                        return raiseException("Unable to convert `" + valueRaw + "` to BoolType",
                                ((VariableNode) ((UnaryOperatorNode) node).operand).token.p);
                }
                case "assert": {
                    QType valueRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    if (valueRaw instanceof BoolType && ((BoolType) valueRaw).value)
                        return new BoolType(true);
                    else
                        return raiseException("Assertion error! " + valueRaw + " is not BoolType:true",
                                ((UnaryOperatorNode) node).operator.p);
                }
                case "return": {
                    DirectInstructionType di = new DirectInstructionType(DirectInstruction.RETURN);
                    di.data.put("value", NodeRunner_run(((UnaryOperatorNode) node).operand));
                    return di;
                }
                case "block": {
                    scope.finalize(((VariableNode) ((UnaryOperatorNode) node).operand).token.c);
                }
                case "notnull": {
                    return new BoolType(!(NodeRunner_run(((UnaryOperatorNode) node).operand) instanceof VoidType));
                }
                case "throw": {
                    return raiseException(NodeRunner_run(((UnaryOperatorNode) node).operand).toString(), ((UnaryOperatorNode) node).operator.p);
                }
                case "import": {
                    QType pathRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    if (pathRaw instanceof StringType) {
                        RuntimeWrapper internalRuntime = new RuntimeWrapper(QFileReader.read(
                                ((StringType) pathRaw).value), false);
                        QType result = internalRuntime.run();
                        if (result instanceof DirectInstructionType && ((DirectInstructionType) result).i.equals(
                                DirectInstruction.EXCEPTION)) {
                            return raiseException("Module \"" + ((StringType) pathRaw).value + "\" failed! " +
                                    ((DirectInstructionType) result).data, ((UnaryOperatorNode) node).operator.p);
                        } else if (result instanceof DirectInstructionType && ((DirectInstructionType) result).i.equals(
                                DirectInstruction.RETURN)) {
                            return ((DirectInstructionType) result).data.get("value");
                        }
                        return new VoidType(true);
                    } else return raiseException("`import` accepts only StringType values but not " +
                            pathRaw.getClass().toString(), ((UnaryOperatorNode) node).operator.p);
                }
                case "file_new": {
                    QType pathRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    if (pathRaw instanceof StringType) {
                        QFileReader.create(((StringType) pathRaw).value);
                    } else return raiseException("`file_new` accepts only StringType `path` but not " +
                            pathRaw.getClass().toString(), ((UnaryOperatorNode) node).operator.p);
                }
                case "file_exists": {
                    QType pathRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    if (pathRaw instanceof StringType) {
                        return new BoolType(QFileReader.exists(((StringType) pathRaw).value));
                    } else return raiseException("`file_exists` accepts only StringType `path` but not " +
                            pathRaw.getClass().toString(), ((UnaryOperatorNode) node).operator.p);
                }
                case "file_write": {
                    if (!(((UnaryOperatorNode) node).operand instanceof MultiElementNode &&
                            ((MultiElementNode) ((UnaryOperatorNode) node).operand).nodes.size() == 2))
                        return raiseException("`file_write` accepts 2 parameters in () but not " +
                                ((UnaryOperatorNode) node).operand, ((UnaryOperatorNode) node).operator.p);
                    MultiElementNode args = (MultiElementNode) ((UnaryOperatorNode) node).operand;
                    QType pathRaw = NodeRunner_run(args.nodes.get(0));
                    QType dataRaw = NodeRunner_run(args.nodes.get(1));
                    if (pathRaw instanceof StringType) {
                        if (dataRaw instanceof StringType) {
                            QFileReader.write(((StringType) pathRaw).value, ((StringType) dataRaw).value);
                        } else return raiseException("`file_exists` accepts only StringType `data` but not " +
                                pathRaw.getClass().toString(), ((UnaryOperatorNode) node).operator.p);
                    } else return raiseException("`file_write` accepts only StringType `path` but not " +
                            pathRaw.getClass().toString(), ((UnaryOperatorNode) node).operator.p);
                }
            }
        }

        if (node instanceof BinaryOperatorNode) {
            BinaryOperatorNode binNode = ((BinaryOperatorNode) node);
            if (binNode.operator.c.equals("=")) {
                if (scope.finalized.contains(((VariableNode) binNode.lnode).token.c)) {
                    return raiseException("`" + ((VariableNode) binNode.lnode).token.c + "` is blocked (finalized)",
                            ((VariableNode) binNode.lnode).token.p);
                }
                QType result = NodeRunner_run(binNode.rnode);
                scope.set(((VariableNode) binNode.lnode).token.c, result);
                return result;
            }
            QType a = NodeRunner_run(binNode.lnode);
            QType b = NodeRunner_run(binNode.rnode);
            switch (binNode.operator.c) {
                case "+": {
                    if (a instanceof StringType || b instanceof StringType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    else if (a instanceof BoolType || b instanceof BoolType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    double af = ((NumType) a).value;
                    double bf = ((NumType) b).value;
                    return new NumType(af + bf);
                }
                case "-": {
                    if (a instanceof StringType || b instanceof StringType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    else if (a instanceof BoolType || b instanceof BoolType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    double af = ((NumType) a).value;
                    double bf = ((NumType) b).value;
                    return new NumType(af - bf);
                }
                case "*": {
                    if (a instanceof StringType || b instanceof StringType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    else if (a instanceof BoolType || b instanceof BoolType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    double af = ((NumType) a).value;
                    double bf = ((NumType) b).value;
                    return new NumType(af * bf);
                }
                case "/": {
                    if (a instanceof StringType || b instanceof StringType) {
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    } else if (a instanceof BoolType || b instanceof BoolType) {
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    }
                    double af = ((NumType) a).value;
                    double bf = ((NumType) b).value;
                    return new NumType(af / bf);
                }
                case "^": {
                    if (a instanceof StringType || b instanceof StringType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    else if (a instanceof BoolType || b instanceof BoolType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    double af = ((NumType) a).value;
                    double bf = ((NumType) b).value;
                    return new NumType(Math.pow(af, bf));
                }
                case "==": {
                    if (a instanceof BoolType && b instanceof BoolType)
                        return new BoolType(((BoolType) a).value == ((BoolType) b).value);
                    else if (a instanceof NumType && b instanceof NumType)
                        return new BoolType(((NumType) a).value == ((NumType) b).value);
                    else if (a instanceof StringType && b instanceof StringType)
                        return new BoolType(((StringType) a).value.equals(((StringType) b).value));
                    return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case "!=": {
                    if (a instanceof BoolType && b instanceof BoolType)
                        return new BoolType(((BoolType) a).value != ((BoolType) b).value);
                    else if (a instanceof NumType && b instanceof NumType)
                        return new BoolType(((NumType) a).value != ((NumType) b).value);
                    else if (a instanceof StringType && b instanceof StringType)
                        return new BoolType(!(((StringType) a).value.equals(((StringType) b).value)));
                    return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case ">": {
                    if (a instanceof NumType && b instanceof NumType)
                        return new BoolType(((NumType) a).value > ((NumType) b).value);
                    return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case "<": {
                    if (a instanceof NumType && b instanceof NumType)
                        return new BoolType(((NumType) a).value < ((NumType) b).value);
                    return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case "<=": {
                    if (a instanceof NumType && b instanceof NumType)
                        return new BoolType(((NumType) a).value <= ((NumType) b).value);
                    return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case ">=": {
                    if (a instanceof NumType && b instanceof NumType)
                        return new BoolType(((NumType) a).value >= ((NumType) b).value);
                    return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case "..": {
                    if (a instanceof StringType && b instanceof StringType)
                        return new StringType(((StringType) a).value + ((StringType) b).value);
                    return raiseException("Unable to concatenate `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case "and": {
                    if (a instanceof BoolType && b instanceof BoolType)
                        return new BoolType(((BoolType) a).value && ((BoolType) b).value);
                    return raiseException("Unable to apply `and` to `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case "or": {
                    if (a instanceof BoolType && b instanceof BoolType)
                        return new BoolType(((BoolType) a).value || ((BoolType) b).value);
                    return raiseException("Unable to apply `or` to `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
            }
        } else if (node instanceof VariableNode) {
            if (  scope.mem.containsKey( ((VariableNode) node).token.c )  ) {
                return scope.mem.get(((VariableNode) node).token.c);
            } else {
                return raiseException("Variable `" + ((VariableNode) node).token.c + "` is not declared in this scope.",
                        ((VariableNode) node).token.p);
            }
        } else if (node instanceof RootNode) {
            for (Node n : ((RootNode) node).nodes) {
                QType result = NodeRunner_run(n);
                if (result instanceof DirectInstructionType) return result;
            }
        } else if (node instanceof BlockNode) {
            for (Node n : ((BlockNode) node).nodes) {
                QType result = NodeRunner_run(n);
                if (result instanceof DirectInstructionType) return result;
            }
        } else if (node instanceof IfBlockNode) {
            BinaryOperatorNode conditionRaw = ((IfBlockNode) node).condition;
            QType condition = NodeRunner_run(conditionRaw);
            if (!(condition instanceof BoolType)) {
                return raiseException("If got `" + condition.getClass().toString() + "` type instead of BoolType",
                        conditionRaw.operator.p);
            }
            if (((BoolType) condition).value) {
                QType result = NodeRunner_run(((IfBlockNode) node).nodes);
                if (result instanceof DirectInstructionType) return result;
            } else {
                for (Node linkedNode : ((IfBlockNode) node).linkedNodes) {
                    if (linkedNode instanceof ElseBlockNode) {
                        QType result = NodeRunner_run(((ElseBlockNode) linkedNode).nodes);
                        if (result instanceof DirectInstructionType) return result;
                    } else if (linkedNode instanceof ElseIfBlockNode) {
                        BinaryOperatorNode linkedConditionRaw = ((ElseIfBlockNode) linkedNode).condition;
                        QType linkedCondition = NodeRunner_run(linkedConditionRaw);
                        if (!(linkedCondition instanceof BoolType)) {
                            return raiseException("ElseIf got `" + condition.getClass().toString() +
                                    "` type instead of BoolType", linkedConditionRaw.operator.p);
                        }
                        if (((BoolType) linkedCondition).value) {
                            QType result = NodeRunner_run(((ElseIfBlockNode) linkedNode).nodes);
                            if (result instanceof DirectInstructionType) return result;
                        }
                    }
                }
            }
            return null;
        } else if (node instanceof InstructionNode) {
            switch (((InstructionNode) node).operator.c) {
                case "milestone":
                    System.out.println("<MILESTONE>");
                    System.out.println(scope.dump());
                    System.out.println("AST:\n" + rootNode);
                    System.out.println("<MILESTONE>");
                    break;
                case "breakpoint": {
                    System.out.println("<MILESTONE>");
                    System.out.println(scope.dump());
                    System.out.println("AST:\n" + rootNode);
                    Scanner sc = new Scanner(System.in);
                    System.out.println("<MILESTONE>\nType `c` and press ENTER to continue > ");
                    sc.next();
                    break;
                }
                case "continue": {
                    return new DirectInstructionType(DirectInstruction.CONTINUE);
                }
                case "break": {
                    return new DirectInstructionType(DirectInstruction.BREAK);
                }
                case "memory": {
                    System.out.println("<MEMDUMP>");
                    System.out.println(scope.dump());
                    System.out.println("<MEMDUMP>");
                    break;
                }
                case "nothing": break;
            }
        } else if (node instanceof ThroughBlockNode) {
            String var = ((ThroughBlockNode) node).variable.token.c;
            Node rawRangeA = ((ThroughBlockNode) node).range.lnode;
            Node rawRangeB = ((ThroughBlockNode) node).range.rnode;
            QType rangeA = NodeRunner_run(rawRangeA);
            QType rangeB = NodeRunner_run(rawRangeB);
            QType returnValue = new VoidType(true);
            if (rangeA instanceof NumType && rangeB instanceof NumType) {
                long a = Math.round(((NumType) rangeA).value);
                long b = Math.round(((NumType) rangeB).value);
                if (b > a) {
                    for (long i = a; i <= b; i++) {
                        scope.set(var, new NumType((double) i));
                        QType result = NodeRunner_run(((ThroughBlockNode) node).nodes);
                        if (result instanceof DirectInstructionType) {
                            if (((DirectInstructionType) result).i.equals(DirectInstruction.CONTINUE))
                                continue;
                            else if (((DirectInstructionType) result).i.equals(DirectInstruction.BREAK))
                                break;
                            else
                                return result;
                        }
                    }
                } else {
                    for (long i = a; i >= b; i--) {
                        scope.set(var, new NumType((double) i));
                        QType result = NodeRunner_run(((ThroughBlockNode) node).nodes);
                        if (result instanceof DirectInstructionType) {
                            if (((DirectInstructionType) result).i.equals(DirectInstruction.CONTINUE))
                                continue;
                            else if (((DirectInstructionType) result).i.equals(DirectInstruction.BREAK))
                                break;
                            else
                                return result;
                        }
                    }
                }
                return returnValue;
            } else {
                return raiseException("A and B should be NumType values!", ((ThroughBlockNode) node).variable.token.p);
            }
        } else if (node instanceof TryCatchNode) {
            QType result = NodeRunner_run(((TryCatchNode) node).tryNodes);
            if (result instanceof DirectInstructionType) {
                if (((DirectInstructionType) result).i.equals(DirectInstruction.EXCEPTION)) {
                    scope.set(((TryCatchNode) node).variable.token.c,
                            ((DirectInstructionType) result).data.get("msg"));
                    return NodeRunner_run(((TryCatchNode) node).catchNodes);
                } else return result;
            }
            return result;
        } else if (node instanceof FunctionCallNode) {
            if (!scope.mem.containsKey(((FunctionCallNode) node).operator.c)) return raiseException(
                    "Function `" + ((FunctionCallNode) node).operator.c + "` is not declared in this scope",
                    ((FunctionCallNode) node).operator.p);
            HashMap<String, QType> backupCollidingVariables = new HashMap<>();
            QType functionRaw = scope.mem.get(((FunctionCallNode) node).operator.c);
            if (!(functionRaw instanceof FuncType)) {
                return raiseException("`" + ((FunctionCallNode) node).operator.c + "` is not a function",
                        ((FunctionCallNode) node).operator.p);
            }
            FuncType function = ((FuncType) functionRaw);
            for (String varName : function.args)
                if (scope.mem.containsKey(varName))
                    backupCollidingVariables.put(varName, scope.mem.get(varName));
            if (((FunctionCallNode) node).operand instanceof MultiElementNode) {
                MultiElementNode argsCasted = (MultiElementNode) ((FunctionCallNode) node).operand;
                for (int i = 0; i < function.args.size(); i++) {
                    if (i < argsCasted.nodes.size())
                        scope.set(function.args.get(i), NodeRunner_run(argsCasted.nodes.get(i)));
                    else
                        scope.set(function.args.get(i), new VoidType(true));
                }
            } else {
                for (int i = 0; i < function.args.size(); i++) {
                    if (i == 0)
                        scope.set(function.args.get(i), NodeRunner_run(((FunctionCallNode) node).operand));
                    else
                        scope.set(function.args.get(i), new VoidType(true));
                }
            }
            QType returnValue = NodeRunner_run(function.code);
            if (returnValue instanceof DirectInstructionType &&
                    ((DirectInstructionType) returnValue).i.equals(DirectInstruction.RETURN))
                returnValue = ((DirectInstructionType) returnValue).data.get("value");
            for (String key : function.args) {
                if (backupCollidingVariables.containsKey(key))
                    scope.set(key, backupCollidingVariables.get(key));
                else
                    scope.mem.remove(key);
            }
            return returnValue;
        } else if (node instanceof FieldReferenceNode) {
            Node objectRaw = ((FieldReferenceNode) node).lnode;
            Node field = ((FieldReferenceNode) node).rnode;
            QType object = NodeRunner_run(objectRaw);
            if (field instanceof FunctionCallNode && ((FunctionCallNode) field).operator.c.equals("_runner")) {
                if (!(object instanceof FuncType)) return raiseException(object.toString() +
                        "should be FuncType if you want to call _runner", ((FunctionCallNode) field).operator.p);
                FunctionCallNode functionCallNode = new FunctionCallNode(
                        new Token(TokenType.ID, ((FuncType) object).name,
                        ((FunctionCallNode) field).operator.p),
                        ((FunctionCallNode) field).operand);
                return NodeRunner_run(functionCallNode);
            }
            if (!(field instanceof VariableNode || field instanceof FunctionCallNode)) {
                if (!(object instanceof FuncType)) return raiseException(object.toString() +
                        "should be FuncType if you want to call .()", ((FieldReferenceNode) node).operator.p);
                FunctionCallNode functionCallNode = new FunctionCallNode(
                        new Token(TokenType.ID, ((FuncType) object).name, ((FieldReferenceNode) node).operator.p),
                        field);
                return NodeRunner_run(functionCallNode);
            }
            if (object instanceof StringType) {
                if (field instanceof VariableNode) {
                    switch (((VariableNode) field).token.c) {
                        case "reverse":
                            return new StringType(StringUtils.reverse(((StringType) object).value));
                        case "capital":
                            return new StringType(StringUtils.capitalize(((StringType) object).value));
                        case "upper":
                            return new StringType(StringUtils.upper(((StringType) object).value));
                        case "lower":
                            return new StringType(StringUtils.lower(((StringType) object).value));
                        case "length":
                        case "len":
                            return new NumType(StringUtils.len(((StringType) object).value));
                    }
                } else if (field instanceof FunctionCallNode){
                    switch (((FunctionCallNode) field).operator.c) {
                        case "sub": {
                            if (((FunctionCallNode) field).operand instanceof MultiElementNode) {
                                QType a = NodeRunner_run(((MultiElementNode)((FunctionCallNode)
                                        field).operand).nodes.get(0));
                                QType b = NodeRunner_run(((MultiElementNode)((FunctionCallNode)
                                        field).operand).nodes.get(1));
                                if (a instanceof NumType && b instanceof NumType) {
                                    return new StringType(StringUtils.sub(((StringType) object).value,
                                            (int) Math.round(((NumType) a).value),
                                            (int) Math.round(((NumType) b).value)));
                                } else {
                                    return raiseException("`a`, `b` expected to be NumType's, not `" + a.getClass().toString() +
                                                    "`, `" + b.getClass().toString() + "`",
                                            ((FieldReferenceNode) node).operator.p);
                                }
                            } else {
                                QType a = NodeRunner_run(((FunctionCallNode) field).operand);
                                if (a instanceof NumType) {
                                    return new StringType(StringUtils.sub(((StringType) object).value,
                                            (int) Math.round(((NumType) a).value)));
                                } else {
                                    return raiseException("`a` expected to be NumType's, not `" + a.getClass().toString() +
                                            "`", ((FieldReferenceNode) node).operator.p);
                                }
                            }
                        }
                        case "at": {
                            if (((FunctionCallNode) field).operand instanceof MultiElementNode) break;
                            QType pos = NodeRunner_run(((FunctionCallNode) field).operand);
                            if (pos instanceof NumType) {
                                return new StringType(StringUtils.at(((StringType) object).value,
                                        (int) Math.round(((NumType) pos).value)));
                            } else {
                                return raiseException("`pos` expected to be NumType, not `" + pos.getClass().toString() + "`",
                                        ((FieldReferenceNode) node).operator.p);
                            }
                        }
                        case "split": {
                            if (((FunctionCallNode) field).operand instanceof MultiElementNode) break;
                            QType delimiter = NodeRunner_run(((FunctionCallNode) field).operand);
                            if (delimiter instanceof StringType) {
                                List<QType> l = new ArrayList<>();
                                for (String s : ((StringType) object).value.split(((StringType) delimiter).value))
                                    l.add(new StringType(s));
                                return ListUtils.newListType(l);
                            } else return raiseException("`split` accepts only StringType, but not "
                                    + delimiter.toString(), ((FieldReferenceNode) node).operator.p);
                        }
                    }
                }
            } else if (object instanceof NumType) {
                if (field instanceof VariableNode) {
                    switch (((VariableNode) field).token.c) {
                        case "round":
                            return new NumType(NumUtils.round(((NumType) object).value));
                        case "floor":
                            return new NumType(NumUtils.floor(((NumType) object).value));
                        case "ceil":
                            return new NumType(NumUtils.ceil(((NumType) object).value));
                    }
                }
            } else if (object instanceof ListType) {
                if (field instanceof VariableNode) {
                    switch (((VariableNode) field).token.c) {
                        case "reverse":
                            return ListUtils.newListType(ListUtils.reverse(((ListType) object).values));
                        case "length":
                        case "len": {
                            return new NumType(((ListType) object).values.size());
                        }
                    }
                } else if (field instanceof FunctionCallNode) {
                    switch (((FunctionCallNode) field).operator.c) {
                        case "sub": {
                            if (((FunctionCallNode) field).operand instanceof MultiElementNode) {
                                QType a = NodeRunner_run(((MultiElementNode)((FunctionCallNode)
                                        field).operand).nodes.get(0));
                                QType b = NodeRunner_run(((MultiElementNode)((FunctionCallNode)
                                        field).operand).nodes.get(1));
                                if (a instanceof NumType && b instanceof NumType) {
                                    return ListUtils.newListType(ListUtils.sub(((ListType) object).values,
                                            (int) Math.round(((NumType) a).value),
                                            (int) Math.round(((NumType) b).value)));
                                } else {
                                    return raiseException("`a`, `b` expected to be NumType's, not `" + a.getClass().toString() +
                                                    "`, `" + b.getClass().toString() + "`",
                                            ((FieldReferenceNode) node).operator.p);
                                }
                            } else {
                                QType a = NodeRunner_run(((FunctionCallNode) field).operand);
                                if (a instanceof NumType) {
                                    return ListUtils.newListType(ListUtils.sub(((ListType) object).values,
                                            (int) Math.round(((NumType) a).value)));
                                } else {
                                    return raiseException("`a` expected to be NumType's, not `" + a.getClass().toString() +
                                            "`", ((FieldReferenceNode) node).operator.p);
                                }
                            }
                        }
                        case "at": {
                            if (((FunctionCallNode) field).operand instanceof MultiElementNode) break;
                            QType pos = NodeRunner_run(((FunctionCallNode) field).operand);
                            if (pos instanceof NumType) {
                                return ListUtils.at(((ListType) object).values,
                                        (int) Math.round(((NumType) pos).value));
                            } else {
                                return raiseException("`pos` expected to be NumType, not `" + pos.getClass().toString() + "`",
                                        ((FieldReferenceNode) node).operator.p);
                            }
                        }
                        case "append":
                        case "push":
                        case "add": {
                            QType content = NodeRunner_run(((FunctionCallNode) field).operand);
                            ListType newList = ListUtils.newListType(ListUtils.add(((ListType) object).values,
                                    content));
                            if (objectRaw instanceof VariableNode)
                                scope.set(((VariableNode) objectRaw).token.c, newList);
                            return newList;
                        }
                        case "remove":
                        case "drop": {
                            QType content = NodeRunner_run(((FunctionCallNode) field).operand);
                            ListType newList = ListUtils.newListType(ListUtils.drop(((ListType) object).values,
                                    content));
                            if (objectRaw instanceof VariableNode)
                                scope.set(((VariableNode) objectRaw).token.c, newList);
                            return newList;
                        }
                        case "overlap":
                        case "over": {
                            if (!(((FunctionCallNode) field).operand instanceof MultiElementNode)) break;
                            QType pos = NodeRunner_run(((MultiElementNode)((FunctionCallNode)
                                    field).operand).nodes.get(0));
                            QType item = NodeRunner_run(((MultiElementNode)((FunctionCallNode)
                                    field).operand).nodes.get(1));
                            if (!(pos instanceof NumType)) break;
                            List<QType> l = ((ListType) object).values;
                            if (l.size() <= NumUtils.round(((NumType) pos).value)) {
                                return raiseException("Index out of bounds", ((FunctionCallNode) field).operator.p);
                            }
                            l.set(NumUtils.round(((NumType) pos).value), item);
                            return ListUtils.newListType(l);
                        }
                    }
                }
            }
            return raiseException("Method/Field `" + field.toString() + "` is not defined for `" + objectRaw + "`",
                    ((FieldReferenceNode) node).operator.p);
        } else if (node instanceof LoopStopBlockNode) {
            BinaryOperatorNode conditionRaw = ((LoopStopBlockNode) node).condition;
            while (true) {
                QType result = NodeRunner_run(((LoopStopBlockNode) node).nodes);
                if (result instanceof DirectInstructionType) {
                    if (((DirectInstructionType) result).i.equals(DirectInstruction.CONTINUE))
                        continue;
                    else if (((DirectInstructionType) result).i.equals(DirectInstruction.BREAK))
                        break;
                    else
                        return result;
                }
                QType condition = NodeRunner_run(conditionRaw);
                if (condition instanceof BoolType && ((BoolType) condition).value) break;
            }
        } else if (node instanceof WhileBlockNode) {
            BinaryOperatorNode conditionRaw = ((WhileBlockNode) node).condition;
            while (true) {
                QType condition = NodeRunner_run(conditionRaw);
                if (!(condition instanceof BoolType && ((BoolType) condition).value)) break;
                QType result = NodeRunner_run(((WhileBlockNode) node).nodes);
                if (result instanceof DirectInstructionType) {
                    if (((DirectInstructionType) result).i.equals(DirectInstruction.CONTINUE))
                        continue;
                    else if (((DirectInstructionType) result).i.equals(DirectInstruction.BREAK))
                        break;
                    else
                        return result;
                }
            }
        } else if (node instanceof EveryBlockNode) {
            QType list = NodeRunner_run(((EveryBlockNode) node).expr);
            String var = ((EveryBlockNode) node).variable.token.c;
            if (list instanceof ListType) {
                for (QType value : ((ListType) list).values) {
                    scope.set(var, value);
                    QType result = NodeRunner_run(((EveryBlockNode) node).nodes);
                    if (result instanceof DirectInstructionType) {
                        if (((DirectInstructionType) result).i.equals(DirectInstruction.CONTINUE))
                            continue;
                        else if (((DirectInstructionType) result).i.equals(DirectInstruction.BREAK))
                            break;
                        else
                            return result;
                    }
                }
            } else if (list instanceof StringType) {
                for (int i = 0; i < ((StringType) list).value.length(); i++) {
                    scope.set(var, new StringType(((StringType) list).value.charAt(i) + ""));
                    QType result = NodeRunner_run(((EveryBlockNode) node).nodes);
                    if (result instanceof DirectInstructionType) {
                        if (((DirectInstructionType) result).i.equals(DirectInstruction.CONTINUE))
                            continue;
                        else if (((DirectInstructionType) result).i.equals(DirectInstruction.BREAK))
                            break;
                        else
                            return result;
                    }
                }
            } else return raiseException("`every` accepts only iterables, but not " + list.toString(),
                    ((EveryBlockNode) node).variable.token.p);
        }

        return new VoidType(true);
    }

    public QType run() {
        if (config.DEBUG) System.out.println(rootNode.toString());
        return NodeRunner_run(rootNode);
    }

}
