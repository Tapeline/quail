package me.tapeline.quailj.interpretertools;

import me.tapeline.quailj.debugtools.AdvancedActionLogger;
import me.tapeline.quailj.language.types.*;
import me.tapeline.quailj.parsingtools.nodes.*;

public class Runtime {

    public static VoidType Void = new VoidType(); // idk, maybe del dat

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
                    }
                    break;
                }
                case "==":
                case "is": {
                    break;
                }
                case "!=": {
                    break;
                }
                case "^": {
                    break;
                }
                case "%": {
                    break;
                }
                case "and": {
                    break;
                }
                case "or": {
                    break;
                }
                case "is type of":
                case "instanceof": {
                    break;
                }
                case "is same type as": {
                    break;
                }
            }
            throw new RuntimeStriker("run:binaryop:no valid case for " + ((BinaryOperatorNode) node).operator.c);
        }
        return Void;
    }

    public QType runTree() throws RuntimeStriker {
        if (config.DEBUG) System.out.println(rootNode.toString());
        return run(rootNode);
    }

}
