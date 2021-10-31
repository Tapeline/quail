package me.tapeline.quarkj.interpretertools;

import me.tapeline.quarkj.language.types.*;
import me.tapeline.quarkj.parsingtools.nodes.*;
import me.tapeline.quarkj.parsingtools.nodes.Node;
import me.tapeline.quarkj.utils.Utilities;

import java.util.Scanner;

public class Runtime {

    public final Node rootNode;
    public Memory scope;
    public final RuntimeConfig config;
    public TryCatchNode scopeExceptionHandler = null;

    public Runtime(Node rootNode, RuntimeConfig cfg) {
        this.rootNode = rootNode;
        this.scope = new Memory();
        this.config = cfg;
    }

    public void raiseException(String msg, int pos) {
        if (scopeExceptionHandler != null) {
            scope.set(scopeExceptionHandler.variable.token.c, new StringType(msg));
            NodeRunner_run(scopeExceptionHandler.catchNodes);
            scopeExceptionHandler = null;
        } else {
            System.err.println("[QNodeRunner] (X) " + msg);
            System.err.println("[QNodeRunner] At " + pos + " symbol in code.");
            System.exit(102);
        }
    }

    public QType NodeRunner_run(Node node) {
        if (node instanceof LiteralNumNode)
            return new NumType(Double.parseDouble(((LiteralNumNode) node).token.c));

        if (node instanceof LiteralStringNode)
            return new StringType(((LiteralStringNode) node).token.c);

        if (node instanceof LiteralBoolNode)
            return new BoolType(Boolean.parseBoolean(((LiteralBoolNode) node).token.c));

        if (node instanceof UnaryOperatorNode) {
            if (((UnaryOperatorNode) node).operator.c.equals("out")) {
                System.out.println(NodeRunner_run(((UnaryOperatorNode) node).operand));
                return new VoidType(true);
            } else if (((UnaryOperatorNode) node).operator.c.equals("put")) {
                System.out.print(NodeRunner_run( ((UnaryOperatorNode) node).operand ));
                return new VoidType(true);
            } else if (((UnaryOperatorNode) node).operator.c.equals("not")) {
                QType operand = NodeRunner_run(((UnaryOperatorNode) node).operand);
                if (operand instanceof BoolType) {
                    return new BoolType(!((BoolType) operand).value);
                } else {
                    raiseException("Unable to invert `" + operand.getClass().toString() + "`",
                            ((UnaryOperatorNode) node).operator.p);
                }
            } else if (((UnaryOperatorNode) node).operator.c.equals("input")) {
                System.out.print(NodeRunner_run( ((UnaryOperatorNode) node).operand ));
                Scanner sc = new Scanner(System.in);
                String input = sc.next();
                return new StringType(input);
            } else if (((UnaryOperatorNode) node).operator.c.equals("destroy")) {
                scope.mem.remove( ((VariableNode)((UnaryOperatorNode) node).operand).token.c);
                return new VoidType(true);
            } else if (((UnaryOperatorNode) node).operator.c.equals("declarestring")) {
                scope.set( ((VariableNode)((UnaryOperatorNode) node).operand).token.c, new StringType(""));
                return new VoidType(true);
            } else if (((UnaryOperatorNode) node).operator.c.equals("declarenum")) {
                scope.set( ((VariableNode)((UnaryOperatorNode) node).operand).token.c, new NumType(0));
                return new VoidType(true);
            } else if (((UnaryOperatorNode) node).operator.c.equals("declarebool")) {
                scope.set( ((VariableNode)((UnaryOperatorNode) node).operand).token.c, new BoolType(false));
                return new VoidType(true);
            } else if (((UnaryOperatorNode) node).operator.c.equals("numinput")) {
                System.out.print(NodeRunner_run( ((UnaryOperatorNode) node).operand ));
                Scanner sc = new Scanner(System.in);
                String rInput = sc.next();
                if (Utilities.isNumeric(rInput)) {
                    double input = Double.parseDouble(rInput);
                    return new NumType(input);
                } else {
                    raiseException("Unable to convert input string `" + rInput + "`" + " to NumType",
                            ((UnaryOperatorNode) node).operator.p);
                }
            } else if (((UnaryOperatorNode) node).operator.c.equals("boolinput")) {
                System.out.print(NodeRunner_run( ((UnaryOperatorNode) node).operand ));
                Scanner sc = new Scanner(System.in);
                boolean input = sc.nextBoolean();
                return new BoolType(input);
            } else if (((UnaryOperatorNode) node).operator.c.equals("exists")) {
                return new BoolType(scope.mem.containsKey(((VariableNode) ((UnaryOperatorNode) node).operand).token.c));
            } else if (((UnaryOperatorNode) node).operator.c.equals("tostring")) {
                QType valueRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                return new StringType(valueRaw.toString());
            } else if (((UnaryOperatorNode) node).operator.c.equals("tonum")) {
                QType valueRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                if (valueRaw instanceof StringType && Utilities.isNumeric(((StringType) valueRaw).value)) {
                    return new NumType(Double.parseDouble(((StringType) valueRaw).value));
                } else {
                    raiseException("Unable to convert `" + valueRaw + "` to NumType",
                            ((VariableNode) ((UnaryOperatorNode) node).operand).token.p);
                }
            } else if (((UnaryOperatorNode) node).operator.c.equals("tobool")) {
                QType valueRaw = NodeRunner_run(((UnaryOperatorNode) node).operand);
                if (valueRaw instanceof StringType && Utilities.isBoolean(((StringType) valueRaw).value)) {
                    return new BoolType(Boolean.parseBoolean(((StringType) valueRaw).value));
                } else {
                    raiseException("Unable to convert `" + valueRaw + "` to BoolType",
                            ((VariableNode) ((UnaryOperatorNode) node).operand).token.p);
                }
            }
        }

        if (node instanceof BinaryOperatorNode) {
            BinaryOperatorNode binNode = ((BinaryOperatorNode) node);
            if (binNode.operator.c.equals("+")) {
                QType a = NodeRunner_run(binNode.lnode);
                QType b = NodeRunner_run(binNode.rnode);
                if (a instanceof StringType || b instanceof StringType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                } else if (a instanceof BoolType || b instanceof BoolType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                }
                double af = ((NumType) a).value;
                double bf = ((NumType) b).value;
                return new NumType(af + bf);


            } else if (binNode.operator.c.equals("-")) {
                QType a = NodeRunner_run(binNode.lnode);
                QType b = NodeRunner_run(binNode.rnode);
                if (a instanceof StringType || b instanceof StringType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                } else if (a instanceof BoolType || b instanceof BoolType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                }
                double af = ((NumType) a).value;
                double bf = ((NumType) b).value;
                return new NumType(af - bf);


            } else if (binNode.operator.c.equals("*")) {
                QType a = NodeRunner_run(binNode.lnode);
                QType b = NodeRunner_run(binNode.rnode);
                if (a instanceof StringType || b instanceof StringType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                } else if (a instanceof BoolType || b instanceof BoolType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                }
                double af = ((NumType) a).value;
                double bf = ((NumType) b).value;
                return new NumType(af * bf);


            } else if (binNode.operator.c.equals("/")) {
                QType a = NodeRunner_run(binNode.lnode);
                QType b = NodeRunner_run(binNode.rnode);
                if (a instanceof StringType || b instanceof StringType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    return null;
                } else if (a instanceof BoolType || b instanceof BoolType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                    return null;
                }
                double af = ((NumType) a).value;
                double bf = ((NumType) b).value;
                return new NumType(af / bf);


            } else if (binNode.operator.c.equals("^")) {
                QType a = NodeRunner_run(binNode.lnode);
                QType b = NodeRunner_run(binNode.rnode);
                if (a instanceof StringType || b instanceof StringType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on StringType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                } else if (a instanceof BoolType || b instanceof BoolType) {
                    raiseException("At `" + node + "`\nUnable to do arithmetic operation on BoolType!" +
                            "\nDid you forget to convert it?", ((BinaryOperatorNode) node).operator.p);
                }
                double af = ((NumType) a).value;
                double bf = ((NumType) b).value;
                return new NumType(Math.pow(af, bf));


            } else if (binNode.operator.c.equals("=")) {
                QType result = NodeRunner_run(binNode.rnode);
                scope.set(((VariableNode) binNode.lnode).token.c, result);
                return result;


            } else if (binNode.operator.c.equals("==")) {
                QType operandA = NodeRunner_run(binNode.lnode);
                QType operandB = NodeRunner_run(binNode.rnode);
                if (operandA instanceof BoolType && operandB instanceof BoolType) {
                    return new BoolType(((BoolType) operandA).value == ((BoolType) operandB).value);
                } else if (operandA instanceof NumType && operandB instanceof NumType) {
                    return new BoolType(((NumType) operandA).value == ((NumType) operandB).value);
                } else if (operandA instanceof StringType && operandB instanceof StringType) {
                    return new BoolType(((StringType) operandA).value.equals(((StringType) operandB).value));
                }
                raiseException("Incomparable types `" + operandA.getClass().toString() + "` and `" +
                        operandB.getClass().toString() + "`", binNode.operator.p);


            } else if (binNode.operator.c.equals("!=")) {
                QType operandA = NodeRunner_run(binNode.lnode);
                QType operandB = NodeRunner_run(binNode.rnode);
                if (operandA instanceof BoolType && operandB instanceof BoolType) {
                    return new BoolType(((BoolType) operandA).value != ((BoolType) operandB).value);
                } else if (operandA instanceof NumType && operandB instanceof NumType) {
                    return new BoolType(((NumType) operandA).value != ((NumType) operandB).value);
                } else if (operandA instanceof StringType && operandB instanceof StringType) {
                    return new BoolType(!(((StringType) operandA).value.equals(((StringType) operandB).value)));
                }
                raiseException("Incomparable types `" + operandA.getClass().toString() + "` and `" +
                        operandB.getClass().toString() + "`", binNode.operator.p);


            } else if (binNode.operator.c.equals(">")) {
                QType operandA = NodeRunner_run(binNode.lnode);
                QType operandB = NodeRunner_run(binNode.rnode);
                if (operandA instanceof NumType && operandB instanceof NumType) {
                    return new BoolType(((NumType) operandA).value > ((NumType) operandB).value);
                }
                raiseException("Incomparable types `" + operandA.getClass().toString() + "` and `" +
                        operandB.getClass().toString() + "`", binNode.operator.p);


            } else if (binNode.operator.c.equals("<")) {
                QType operandA = NodeRunner_run(binNode.lnode);
                QType operandB = NodeRunner_run(binNode.rnode);
                if (operandA instanceof NumType && operandB instanceof NumType) {
                    return new BoolType(((NumType) operandA).value < ((NumType) operandB).value);
                }
                raiseException("Incomparable types `" + operandA.getClass().toString() + "` and `" +
                        operandB.getClass().toString() + "`", binNode.operator.p);


            } else if (binNode.operator.c.equals("<=")) {
                QType operandA = NodeRunner_run(binNode.lnode);
                QType operandB = NodeRunner_run(binNode.rnode);
                if (operandA instanceof NumType && operandB instanceof NumType) {
                    return new BoolType(((NumType) operandA).value <= ((NumType) operandB).value);
                }
                raiseException("Incomparable types `" + operandA.getClass().toString() + "` and `" +
                        operandB.getClass().toString() + "`", binNode.operator.p);


            } else if (binNode.operator.c.equals(">=")) {
                QType operandA = NodeRunner_run(binNode.lnode);
                QType operandB = NodeRunner_run(binNode.rnode);
                if (operandA instanceof NumType && operandB instanceof NumType) {
                    return new BoolType(((NumType) operandA).value >= ((NumType) operandB).value);
                }
                raiseException("Incomparable types `" + operandA.getClass().toString() + "` and `" +
                        operandB.getClass().toString() + "`", binNode.operator.p);


            } else if (binNode.operator.c.equals("..")) {
                QType operandA = NodeRunner_run(binNode.lnode);
                QType operandB = NodeRunner_run(binNode.rnode);
                if (operandA instanceof StringType && operandB instanceof StringType) {
                    return new StringType(((StringType) operandA).value + ((StringType) operandB).value);
                }
                raiseException("Unable to concatenate `" + operandA.getClass().toString() + "` and `" +
                        operandB.getClass().toString() + "`", binNode.operator.p);
            }
        } else if (node instanceof VariableNode) {
            if (  scope.mem.containsKey( ((VariableNode) node).token.c )  ) {
                return scope.mem.get(((VariableNode) node).token.c);
            } else {
                raiseException("Variable `" + ((VariableNode) node).token.c + "` is not declared in this scope.",
                        ((VariableNode) node).token.p);
            }
        } else if (node instanceof RootNode) {
            for (Node n : ((RootNode) node).nodes) {
                NodeRunner_run(n);
            }
        } else if (node instanceof BlockNode) {
            for (Node n : ((BlockNode) node).nodes) {
                NodeRunner_run(n);
            }
        } else if (node instanceof IfBlockNode) {
            BinaryOperatorNode conditionRaw = ((IfBlockNode) node).condition;
            QType condition = NodeRunner_run(conditionRaw);
            if (!(condition instanceof BoolType)) {
                raiseException("If got `" + condition.getClass().toString() + "` type instead of BoolType",
                        conditionRaw.operator.p);
            }
            if (((BoolType) condition).value) {
                NodeRunner_run(((IfBlockNode) node).nodes);
            } else {
                for (Node linkedNode : ((IfBlockNode) node).linkedNodes) {
                    if (linkedNode instanceof ElseBlockNode) {
                        NodeRunner_run(((ElseBlockNode) linkedNode).nodes);
                    } else if (linkedNode instanceof ElseIfBlockNode) {
                        BinaryOperatorNode linkedConditionRaw = ((ElseIfBlockNode) linkedNode).condition;
                        QType linkedCondition = NodeRunner_run(linkedConditionRaw);
                        if (!(linkedCondition instanceof BoolType)) {
                            raiseException("ElseIf got `" + condition.getClass().toString() +
                                    "` type instead of BoolType", linkedConditionRaw.operator.p);
                        }
                        if (((BoolType) linkedCondition).value) {
                            NodeRunner_run(((ElseIfBlockNode) linkedNode).nodes);
                        }
                    }
                }
            }
            return null;
        } else if (node instanceof InstructionNode) {
            if (((InstructionNode) node).operator.c.equals("milestone")) {
                System.out.println("<MILESTONE>");
                System.out.println("Memory:\n" + scope.mem.toString());
                System.out.println("AST:\n" + rootNode);
                System.out.println("<MILESTONE>");
            } else if (((InstructionNode) node).operator.c.equals("breakpoint")) {
                System.out.println("<MILESTONE>");
                System.out.println("Memory:\n" + scope.mem.toString());
                System.out.println("AST:\n" + rootNode);
                Scanner sc = new Scanner(System.in);
                System.out.println("<MILESTONE>\nType `c` and press ENTER to continue > ");
                sc.next();
            }
        } else if (node instanceof ThroughBlockNode) {
            String var = ((ThroughBlockNode) node).variable.token.c;
            Node rawRangeA = ((ThroughBlockNode) node).range.lnode;
            Node rawRangeB = ((ThroughBlockNode) node).range.rnode;
            QType rangeA = NodeRunner_run(rawRangeA);
            QType rangeB = NodeRunner_run(rawRangeB);
            if (rangeA instanceof NumType && rangeB instanceof NumType) {
                long a = Math.round(((NumType) rangeA).value);
                long b = Math.round(((NumType) rangeB).value);
                if (b > a) {
                    for (long i = a; i <= b; i++) {
                        scope.set(var, new NumType((double) i));
                        NodeRunner_run(((ThroughBlockNode) node).nodes);
                    }
                } else {
                    for (long i = a; i >= b; i--) {
                        scope.set(var, new NumType((double) i));
                        NodeRunner_run(((ThroughBlockNode) node).nodes);
                    }
                }
            }
        } else if (node instanceof TryCatchNode) {
            scopeExceptionHandler = (TryCatchNode) node;
            NodeRunner_run(((TryCatchNode) node).tryNodes);
        }

        return new VoidType(true);
    }

    public void run() {
        if (config.DEBUG) System.out.println(rootNode.toString());
        NodeRunner_run(rootNode);
    }

}
