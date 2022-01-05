package me.tapeline.quailj.interpretertools;

import me.tapeline.quailj.Language;
import me.tapeline.quailj.QFileReader;
import me.tapeline.quailj.RuntimeWrapper;
import me.tapeline.quailj.debugtools.AdvancedActionLogger;
import me.tapeline.quailj.language.types.*;
import me.tapeline.quailj.parsingtools.nodes.*;
import me.tapeline.quailj.tokenizetools.tokens.Token;
import me.tapeline.quailj.tokenizetools.tokens.TokenType;
import me.tapeline.quailj.utils.ListUtils;
import me.tapeline.quailj.utils.NumUtils;
import me.tapeline.quailj.utils.StringUtils;
import me.tapeline.quailj.utils.Utilities;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Runtime {

    public final Node rootNode;
    private final String code;
    private final AdvancedActionLogger aal;
    public Memory scope;
    public final RuntimeConfig config;

    public Runtime(Node rootNode, RuntimeConfig cfg, String code, AdvancedActionLogger aal) {
        this.rootNode = rootNode;
        this.scope = new Memory();
        this.config = cfg;
        this.code = code;
        this.aal = aal;
    }

    public boolean requireType(QType t, String... c) {
        for (String cc : c) {
            switch (cc) {
                case "str" : if (t instanceof StringType) return true; break;
                case "bool": if (t instanceof BoolType)   return true; break;
                case "null": if (t instanceof VoidType)   return true; break;
                case "func": if (t instanceof FuncType)   return true; break;
                case "num" : if (t instanceof NumType)    return true; break;
                case "ref" : if (t instanceof RefType)    return true; break;
                case "list": if (t instanceof ListType)   return true; break;
                case "container":
                case "cont": if (t instanceof ContainerType && !((ContainerType) t).isMeta)    return true; break;
                case "metacontainer":
                case "metacont": if (t instanceof ContainerType && ((ContainerType) t).isMeta) return true; break;
                default: if (t instanceof ContainerType && ((ContainerType) t).isMeta &&
                            (((ContainerType) t).name.equals(cc) ||
                             ((ContainerType) t).like.equals(cc))) return true; break;
            }
        }
        return false;
    }

    public QType raiseException(String msg, int pos) {
        DirectInstructionType exception = new DirectInstructionType(DirectInstruction.EXCEPTION);
        exception.data.put("msg", new StringType(msg));
        exception.data.put("symbol", new NumType(pos));
        aal.err(exception);
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
            return new VoidType();
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
            return new VoidType();
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
                    return new VoidType();
                case "put":
                    System.out.print(NodeRunner_run(((UnaryOperatorNode) node).operand));
                    return new VoidType();
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
                    scope.remove(((VariableNode) ((UnaryOperatorNode) node).operand).token.c);
                    return new VoidType();
                case "exists":
                    return new BoolType(scope.mem.containsKey(
                            ((VariableNode) ((UnaryOperatorNode) node).operand).token.c));
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
                    return raiseException(NodeRunner_run(((UnaryOperatorNode) node).operand).toString(),
                            ((UnaryOperatorNode) node).operator.p);
                }
                case "using":
                case "deploy":
                case "use": {
                    String path = "";
                    if (((UnaryOperatorNode) node).operand instanceof VariableNode)
                        path = ((VariableNode) ((UnaryOperatorNode) node).operand).token.c;
                    QType pathRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                    if (!(pathRaw instanceof StringType)) return raiseException(
                            "`import` accepts only StringType or Variable values but not " +
                            pathRaw.getClass().toString(), ((UnaryOperatorNode) node).operator.p);
                    else path = ((StringType) pathRaw).value;
                    RuntimeWrapper internalRuntime = new RuntimeWrapper(QFileReader.read(path), false);
                    QType result = internalRuntime.run();
                    if (result instanceof DirectInstructionType && ((DirectInstructionType) result).i.equals(
                            DirectInstruction.EXCEPTION)) {
                        return raiseException("Module \"" + path + "\" failed! " +
                                ((DirectInstructionType) result).data, ((UnaryOperatorNode) node).operator.p);
                    } else if (result instanceof DirectInstructionType && ((DirectInstructionType) result).i.equals(
                            DirectInstruction.RETURN)) {
                        scope.set(path, ((DirectInstructionType) result).data.get("value"));
                        return ((DirectInstructionType) result).data.get("value");
                    }
                    return new VoidType();
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
            int posForException = binNode.operator.p;
            QType a = NodeRunner_run(binNode.lnode);
            QType b = NodeRunner_run(binNode.rnode);
            switch (binNode.operator.c) {
                case "+": {
                    if (a instanceof StringType && b instanceof StringType)
                        return new StringType(((StringType) a).value + ((StringType) b).value);
                    else if (a instanceof NumType && b instanceof NumType)
                        return new NumType(((NumType) a).value + ((NumType) b).value);
                    else if (a instanceof ListType && b instanceof ListType)
                        return ListUtils.concat((ListType) a, (ListType) b);
                    else if (a instanceof ListType)
                        return ListUtils.newListType(ListUtils.add(((ListType) a).values, b));
                    else if (b instanceof ListType)
                        return ListUtils.newListType(ListUtils.addFront(((ListType) b).values, a));
                    else return raiseException("Cannot apply `+` to " + a.getClass().toString() + " and " +
                                b.getClass().toString(), posForException);
                }
                case "-": {
                    if (a instanceof NumType && b instanceof NumType)
                        return new NumType(((NumType) a).value - ((NumType) b).value);
                    else if (a instanceof ListType)
                        return ListUtils.newListType(ListUtils.remove(((ListType) a).values, b));
                    else return raiseException("Cannot apply `-` to " + a.getClass().toString() + " and " +
                                b.getClass().toString(), posForException);
                }
                case "*": {
                    if (a instanceof StringType && b instanceof NumType)
                        return new StringType(StringUtils.mult(((StringType) a).value, ((NumType) b).value));
                    else if (a instanceof NumType && b instanceof NumType)
                        return new NumType(((NumType) a).value * ((NumType) b).value);
                    else if (a instanceof ListType && b instanceof NumType)
                        return ListUtils.newListType(ListUtils.mult(((ListType) a).values, ((NumType) b).value));
                    else return raiseException("Cannot apply `*` to " + a.getClass().toString() + " and " +
                                b.getClass().toString(), posForException);
                }
                case "/":
                case "^": {
                    if (a instanceof StringType || b instanceof StringType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    else if (a instanceof BoolType || b instanceof BoolType)
                        return raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                                "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    double af = ((NumType) a).value;
                    double bf = ((NumType) b).value;
                    switch (binNode.operator.c) {
                        case "/": return new NumType(af / bf);
                        case "^": return new NumType(Math.pow(af, bf));
                    }
                }
                case "is":
                case "==": {
                    BoolType r = Utilities.compare(a, b);
                    if (r == null) return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                    return r;
                }
                case "is not":
                case "!=": {
                    BoolType r = Utilities.compare(a, b);
                    if (r == null) return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                    return new BoolType(!r.value);
                }
                case ">":
                case "<":
                case "<=":
                case ">=": {
                    double af;
                    double bf;
                    if (a instanceof StringType && b instanceof StringType) {
                        af = ((StringType) a).value.length();
                        bf = ((StringType) b).value.length();
                    } else if (a instanceof ListType && b instanceof ListType) {
                        af = ((ListType) a).values.size();
                        bf = ((ListType) b).values.size();
                    } else if (a instanceof NumType && b instanceof NumType) {
                        af = ((NumType) a).value;
                        bf = ((NumType) a).value;
                    } else return raiseException("Incomparable types `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                    switch (binNode.operator.c) {
                        case ">":  return new BoolType(af >  bf);
                        case "<":  return new BoolType(af <  bf);
                        case "<=": return new BoolType(af <= bf);
                        case ">=": return new BoolType(af >= bf);
                    }
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
                    else if (a instanceof VoidType && b instanceof VoidType) return new VoidType();
                    else if (a instanceof VoidType) return b;
                    else if (b instanceof VoidType) return a;
                    return raiseException("Unable to apply `or` to `" + a.getClass().toString() + "` and `" +
                            b.getClass().toString() + "`", binNode.operator.p);
                }
                case "is same type as": {
                    return new BoolType((a instanceof ContainerType && ((ContainerType) a).isMeta
                            && b instanceof ContainerType && ((ContainerType) b).isMeta)?
                            ((ContainerType) a).name.equals(((ContainerType) b).name) :
                                    a.getClass().equals(b.getClass()));
                }
                case "is type of":
                case "instanceof": {
                    if (!(b instanceof StringType))
                        return raiseException("`instanceof` accepts only StringType as 2nd argument!",
                                posForException);
                    return new BoolType(requireType(a, ((StringType) b).value));
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
            String var = ((VariableNode) ((ThroughBlockNode) node).variable).token.c;
            Node rawRangeA = ((ThroughBlockNode) node).range.lnode;
            Node rawRangeB = ((ThroughBlockNode) node).range.rnode;
            QType rangeA = NodeRunner_run(rawRangeA);
            QType rangeB = NodeRunner_run(rawRangeB);
            QType returnValue = new VoidType();
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
                return raiseException("A and B should be NumType values!",
                        ((VariableNode) ((ThroughBlockNode) node).variable).token.p);
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
            if (((FunctionCallNode) node).id instanceof FieldReferenceNode) {

            } else if (((FunctionCallNode) node).id instanceof VariableNode) {

            }
        } else if (node instanceof FieldReferenceNode) {

            return raiseException("Method/Field `" + ((FieldReferenceNode) node).rnode.toString() +
                            "` is not defined for `" + ((FieldReferenceNode) node).lnode.toString() + "`",
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
            String var = ((VariableNode) ((EveryBlockNode) node).variable).token.c;
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
                    ((VariableNode) ((EveryBlockNode) node).variable).token.p);
        }
        return new VoidType();
    }

    public QType run() {
        if (config.DEBUG) System.out.println(rootNode.toString());
        return NodeRunner_run(rootNode);
    }

}
