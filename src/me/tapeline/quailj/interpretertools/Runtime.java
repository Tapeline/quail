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
import org.json.JSONObject;

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
    public final String codepath;
    public final String code;
    public final RuntimeConfig config;
    public final HashMap<String, FuncType> eventHandlers = new HashMap<>();
    public Node current;

    public Runtime(Node rootNode, RuntimeConfig cfg, AdvancedActionLogger aal, String codepath, String code) {
        this.rootNode = rootNode;
        this.codepath = codepath;
        this.code = code;
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

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });
        this.scope.set("millis", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) {
                return new NumType((double) System.currentTimeMillis());
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });
        this.scope.set("tostring", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                if (args.get(0) instanceof ContainerType &&
                        ((ContainerType) args.get(0)).content.containsKey("__tostring__") &&
                        ((ContainerType) args.get(0)).content.get("__tostring__") instanceof FuncType) {
                    List<QType> metaArgs = new ArrayList<>(Collections.singletonList(args.get(0)));
                    metaArgs.addAll(args);
                    return ((FuncType) ((ContainerType) args.get(0)).content.get("__tostring__")).
                            metaRun(runtime, metaArgs);
                } else return new StringType(args.get(0).toString());
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });
        this.scope.set("tonumber", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                if (args.get(0) instanceof ContainerType &&
                        ((ContainerType) args.get(0)).content.containsKey("__tonumber__") &&
                        ((ContainerType) args.get(0)).content.get("__tonumber__") instanceof FuncType) {
                    List<QType> metaArgs = new ArrayList<>(Collections.singletonList(args.get(0)));
                    metaArgs.addAll(args);
                    return ((FuncType) ((ContainerType) args.get(0)).content.get("__tonumber__")).
                            metaRun(runtime, metaArgs);
                } else {
                    try {
                        double d = Double.parseDouble(args.get(0).toString());
                        return new NumType(d);
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeStriker("tonumber:cannot parse number");
                    }
                }
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });

        this.scope.set("tobool", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                if (args.get(0) instanceof ContainerType &&
                        ((ContainerType) args.get(0)).content.containsKey("__tobool__") &&
                        ((ContainerType) args.get(0)).content.get("__tobool__") instanceof FuncType) {
                    List<QType> metaArgs = new ArrayList<>(Collections.singletonList(args.get(0)));
                    metaArgs.addAll(args);
                    return ((FuncType) ((ContainerType) args.get(0)).content.get("__tobool__")).
                            metaRun(runtime, metaArgs);
                } else {
                    try {
                        boolean b = Boolean.parseBoolean(args.get(0).toString());
                        return new BoolType(b);
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeStriker("tobool:cannot parse bool");
                    }
                }
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });

        this.scope.set("filewrite", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                if (args.size() < 2)
                    throw new RuntimeStriker("filewrite:invalid args length");
                QFileReader.write(args.get(0).toString(), args.get(1).toString());
                return Void;
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });

        this.scope.set("fileread", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                if (args.size() < 1)
                    throw new RuntimeStriker("fileread:invalid args length");
                return new StringType(QFileReader.read(args.get(0).toString()));
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });

        this.scope.set("filenew", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                if (args.size() < 1)
                    throw new RuntimeStriker("filenew:invalid args length");
                QFileReader.create(args.get(0).toString());
                return Void;
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });

        this.scope.set("out", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                System.out.println(args.size() > 1? args.get(0).toString() : "");
                return Void;
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });

        this.scope.set("put", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                System.out.print(args.size() > 1? args.get(0).toString() : "");
                return Void;
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });

        this.scope.set("input", new BuiltinFuncType() {
            @Override
            public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                Scanner sc = new Scanner(System.in);
                return new StringType(sc.nextLine());
            }

            @Override
            public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                return null;
            }
        });
        this.scope.set("nothing", new VoidType());
        this.scope.set("million", new NumType(1000000D));
        this.scope.set("billion", new NumType(1000000000D));
        this.scope.set("trillion", new NumType(1000000000000D));
    }

    public QType getNativeLib(String name) {
        switch (name) {
            case "storage": {
                HashMap<String, QType> content = new HashMap<>();
                content.put("loadjson", new BuiltinFuncType() {
                    @Override
                    public QType run(Runtime runtime, List<QType> args) throws RuntimeStriker {
                        if (args.size() < 1 || !(args.get(0) instanceof StringType))
                            throw new RuntimeStriker("storage:loadjson:invalid args");
                        String c = QFileReader.read(((StringType) args.get(0)).value);
                        if (c == null)
                            throw new RuntimeStriker("storage:loadjson:file is null");
                        JSONObject cfg = new JSONObject(c);
                        HashMap<String, QType> parsed = new HashMap<>();
                        return null;
                    }

                    @Override
                    public QType metaRun(Runtime runtime, List<QType> metaArgs) {
                        return null;
                    }
                });
                return new ContainerType(content, false);
            }
        }
        return Void;
    }

    public QType runFieldFunction(QType v, String name, List<QType> args, Memory scope) throws RuntimeStriker {
        if (QType.isStr(v)) {
            switch (name) {
                case "at": {
                    if (!(args.get(0) instanceof NumType))
                        throw new RuntimeStriker("run:field:string:at-func:1st arg is not number");
                    return new StringType(StringUtils.at(((StringType) v).value,
                                    (int) Math.round(((NumType) args.get(0)).value)));
                }
                case "sub": {
                    if (args.size() == 1) {
                        if (!(args.get(0) instanceof NumType))
                            throw new RuntimeStriker("run:field:string:at-func:1st arg is not number");
                        return new StringType(StringUtils.sub(((StringType) v).value,
                                (int) Math.round(((NumType) args.get(0)).value)));
                    } else if (args.size() > 1) {
                        if (QType.isNum(args.get(0), args.get(0)))
                            throw new RuntimeStriker("run:field:string:at-func:1st and 2nd args is not number");
                        return new StringType(StringUtils.sub(((StringType) v).value,
                                (int) Math.round(((NumType) args.get(0)).value),
                                (int) Math.round(((NumType) args.get(1)).value)));
                    }
                }
                case "split": {
                    if (args.size() > 1 && !(args.get(0) instanceof StringType))
                        throw new RuntimeStriker("run:field:string:split-func:delimiter must be string type");
                    return new ListType(StringUtils.split(((StringType) v).value,
                            args.size() < 1? " " : ((StringType) args.get(0)).value));
                }
            }
        } else if (QType.isList(v)) {

        }
        throw new RuntimeStriker("run:field:cannot run " + name);
    }

    public QType runField(FieldReferenceNode node, Memory scope) throws RuntimeStriker {
        QType v = run(node.lnode, scope);
        if (!(node.rnode instanceof VariableNode))
            throw new RuntimeStriker("run:field:cannot run non-variable node");
        if (QType.isStr(v)) {
            switch (((VariableNode) node.rnode).token.c) {
                case "reverse":
                    return new StringType(StringUtils.reverse(((StringType) v).value));
                case "capitalize":
                    return new StringType(StringUtils.capitalize(((StringType) v).value));
                case "lower":
                    return new StringType(StringUtils.lower(((StringType) v).value));
                case "upper":
                    return new StringType(StringUtils.upper(((StringType) v).value));
                case "length":
                case "len":
                    return new NumType(StringUtils.len(((StringType) v).value));
            }
        } else if (QType.isNum(v)) {
            switch (((VariableNode) node.rnode).token.c) {
                case "floor":
                    return new NumType(Math.floor(((NumType) v).value));
                case "round":
                    return new NumType(Math.round(((NumType) v).value));
                case "ceil":
                    return new NumType(Math.ceil(((NumType) v).value));
                case "absolute":
                case "abs":
                    return new NumType(Math.abs(((NumType) v).value));
                case "negated":
                case "neg":
                    return new NumType(-((NumType) v).value);
            }
        } else if (QType.isList(v)) {
            switch (((VariableNode) node.rnode).token.c) {
                case "length":
                case "len":
                    return new NumType(((ListType) v).values.size());
                case "reverse":
                    return new ListType(ListUtils.reverse(((ListType) v).values));
            }
        }
        throw new RuntimeStriker("run:field:cannot run " + ((VariableNode) node.rnode).token.c);
    }

    public ContainerType overrideContainerContents(ContainerType dest, ContainerType src) {
        dest.content.putAll(src.content);
        return dest;
    }

    public ContainerType inheritContainer(ContainerType parent, ContainerType child) {
        if (parent.like.equals("container")) {
            child = overrideContainerContents(child, parent);
            return child;
        } else {
            QType p = scope.get(parent.like);
            if (p instanceof ContainerType) {
                parent = inheritContainer((ContainerType) p, parent);
                child = overrideContainerContents(child, parent);
                return child;
            }
        }
        return child;
    }

    public QType run(Node node) throws RuntimeStriker {
        return run(node, this.scope);
    }

    public QType run(Node node, Memory scope) throws RuntimeStriker {
        current = node;
        if (node instanceof BinaryOperatorNode) {
            QType a = run(((BinaryOperatorNode) node).lnode, scope);
            QType b = run(((BinaryOperatorNode) node).rnode, scope);
            if (a instanceof ContainerType) {
                String containerImpl = Utilities.transformOp(((BinaryOperatorNode) node).operator.c);
                if (((ContainerType) a).content.containsKey(containerImpl) &&
                        ((ContainerType) a).content.get(containerImpl) instanceof FuncType) {
                    List<QType> metaArgs = new ArrayList<>(Arrays.asList(a, b));
                    return ((FuncType) ((ContainerType) a).content.get(containerImpl)).
                            metaRun(this, metaArgs);
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
                            run(((BinaryOperatorNode) node).rnode, scope));
                    return Void;
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
            if (!(((EveryBlockNode) node).variable instanceof VariableNode))
                throw new RuntimeStriker("run:every-loop:range iterator");
            if (QType.isList(range)) {
                for (int i = 0; i < ((ListType) range).values.size(); i++) {
                    scope.set(((VariableNode) ((EveryBlockNode) node).variable).token.c,
                            ((ListType) range).values.get(i));
                    try {
                        run(((EveryBlockNode) node).nodes, scope);
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
                        run(((EveryBlockNode) node).nodes, scope);
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
            QType parent = run(((FieldReferenceNode) node).lnode, scope);
            if (QType.isNum(parent) || QType.isList(parent) || QType.isStr(parent))
                return runField((FieldReferenceNode) node, scope);
            if (!(((FieldReferenceNode) node).rnode instanceof VariableNode))
                throw new RuntimeStriker("run:field-set:cannot get value of non-variable type");
            if (parent instanceof ContainerType) {
                return ((ContainerType) parent).content.get(((VariableNode)
                        ((FieldReferenceNode) node).rnode).token.c);
            }
        } else if (node instanceof FieldSetNode) {
            QType parent = run(((FieldSetNode) node).lnode, scope);
            if (!(((FieldSetNode) node).rnode instanceof VariableNode))
                throw new RuntimeStriker("run:field-set:cannot place value to non-variable type");
            if (parent instanceof ContainerType) {
                ((ContainerType) parent).content.put(((VariableNode) ((FieldSetNode) node).rnode).token.c,
                        run(((FieldSetNode) node).value, scope));
            }
        } else if (node instanceof FunctionCallNode) {
            boolean isMetacall = false;
            QType callee = run(((FunctionCallNode) node).id, scope);
            if (callee == null)
                throw new RuntimeStriker("run:function:cannot call null " + ((FunctionCallNode) node).id.toString());
            QType parent = null;
            if (((FunctionCallNode) node).id instanceof FieldReferenceNode) {
                parent = run(((FieldReferenceNode) ((FunctionCallNode) node).id).lnode, scope);
                if (parent instanceof ContainerType && ((ContainerType) parent).isMeta)
                    isMetacall = true;
            }
            List<QType> args = new ArrayList<>();
            for (Node arg : ((MultiElementNode) ((FunctionCallNode) node).args).nodes) {
                args.add(run(arg, scope));
            }
            if (callee instanceof FuncType) {
                if (((FuncType) callee).restrictMetacalls || (!isMetacall && parent == null))
                    return ((FuncType) callee).run(this, args);
                else {
                    List<QType> metaArgs = new ArrayList<>(Collections.singletonList(parent));
                    metaArgs.addAll(args);
                    return ((FuncType) callee).metaRun(this, metaArgs);
                }
            } else if (callee instanceof BuiltinFuncType) {
                if (((BuiltinFuncType) callee).restrictMetacalls || (!isMetacall && parent == null))
                    return ((BuiltinFuncType) callee).run(this, args);
                else {
                    List<QType> metaArgs = new ArrayList<>(Collections.singletonList(parent));
                    metaArgs.addAll(args);
                    return ((BuiltinFuncType) callee).metaRun(this, metaArgs);
                }
            } else if (callee instanceof ContainerType) {
                ContainerType ct = (ContainerType) callee;
                if (ct.content.containsKey("__builder__")) {
                    if (!(ct.content.get("__builder__") instanceof FuncType))
                        throw new RuntimeStriker("run:function:cannot call " + callee);
                    List<QType> builderArgs = new ArrayList<>(Collections.singletonList(ct));
                    builderArgs.addAll(args);
                    return ((FuncType) ct.content.get("__builder__")).run(this, builderArgs);
                }
            } else {
                if (((FunctionCallNode) node).id instanceof FieldReferenceNode) {
                    QType v = run(((FieldReferenceNode) ((FunctionCallNode) node).id).lnode, scope);
                    String s = ((FieldReferenceNode) ((FunctionCallNode) node).id).rnode.toString();
                    return runFieldFunction(v, s, args, scope);
                } else throw new RuntimeStriker("run:function:cannot call " + callee);
            }
        } else if (node instanceof GroupNode) {
            return run(((GroupNode) node).node, scope);
        } else if (node instanceof IfBlockNode) {
            QType cond = run(((IfBlockNode) node).condition, scope);
            if (cond instanceof BoolType && ((BoolType) cond).value) {
                run(((IfBlockNode) node).nodes, scope);
            } else {
                for (Node branch : ((IfBlockNode) node).linkedNodes) {
                    if (branch instanceof ElseIfBlockNode) {
                        QType condElseIf = run(((ElseIfBlockNode) branch).condition, scope);
                        if (condElseIf instanceof BoolType && ((BoolType) condElseIf).value) {
                            run(((ElseIfBlockNode) branch).nodes, scope);
                            break;
                        }
                    } else if (branch instanceof ElseBlockNode) {
                        run(((ElseBlockNode) branch).nodes, scope);
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
                ContainerType ct = new ContainerType(new HashMap<>(), true);
                if (!((LiteralContainerNode) node).alike.equals("container")) {
                    ct.like = ((LiteralContainerNode) node).alike;
                    QType parent = scope.get(ct.like);
                    if (parent instanceof ContainerType) {
                        ct = inheritContainer((ContainerType) parent, ct);
                    }
                }
                for (Node init : ((LiteralContainerNode) node).initialize) {
                    if (init instanceof LiteralDefinitionNode) {
                        switch (((LiteralDefinitionNode) init).type.c) {
                            case "string": {
                                ct.content.put(((LiteralDefinitionNode) init).variable.c, new StringType(""));
                                break;
                            }
                            case "num": {
                                ct.content.put(((LiteralDefinitionNode) init).variable.c, new NumType(0));
                                break;
                            }
                            case "bool": {
                                ct.content.put(((LiteralDefinitionNode) init).variable.c, new BoolType(false));
                                break;
                            }
                        }
                    } else if (init instanceof BinaryOperatorNode &&
                            ((BinaryOperatorNode) init).operator.c.equals("=")) {
                        if (!(((BinaryOperatorNode) init).lnode instanceof VariableNode))
                            throw new RuntimeStriker("run:literal-container:init:cannot set value to non-variable");
                        ct.content.put(((VariableNode) ((BinaryOperatorNode) init).lnode).token.c,
                                run(((BinaryOperatorNode) init).rnode, scope));
                    } else if (init instanceof LiteralFunctionNode) {
                        List<String> args = new ArrayList<>();
                        for (Node n : ((MultiElementNode) ((LiteralFunctionNode) init).args).nodes) {
                            if (n instanceof VariableNode)
                                args.add(((VariableNode) n).token.c);
                            else throw new RuntimeStriker("run:literal-container:init:literal-function:invalid args");
                        }
                        FuncType f = new FuncType(((LiteralFunctionNode) init).name.c,
                                args, ((LiteralFunctionNode) init).code, ((LiteralFunctionNode) init).s);
                        ct.content.put(((LiteralFunctionNode) init).name.c, f);
                    }
                }
                if (!((LiteralContainerNode) node).builder.name.c.equals("no-builder")) {
                    List<String> args = new ArrayList<>();
                    for (Node n : ((MultiElementNode) ((LiteralContainerNode) node).builder.args).nodes) {
                        if (n instanceof VariableNode)
                            args.add(((VariableNode) n).token.c);
                        else throw new RuntimeStriker("run:literal-container:init:builder:invalid args");
                    }
                    ct.content.put("__builder__",
                            new FuncType("__builder__", args, ((LiteralContainerNode) node).builder.code));
                }
                ct.name = ((LiteralContainerNode) node).name;
                ct.like = ((LiteralContainerNode) node).alike;
                scope.set(((LiteralContainerNode) node).name, ct);
            } else {
                HashMap<String, QType> content = new HashMap<>();
                for (Node init : ((LiteralContainerNode) node).initialize) {
                    if (init instanceof LiteralDefinitionNode) {
                        switch (((LiteralDefinitionNode) init).type.c) {
                            case "string": {
                                content.put(((LiteralDefinitionNode) init).variable.c, new StringType(""));
                                break;
                            }
                            case "num": {
                                content.put(((LiteralDefinitionNode) init).variable.c, new NumType(0));
                                break;
                            }
                            case "bool": {
                                content.put(((LiteralDefinitionNode) init).variable.c, new BoolType(false));
                                break;
                            }
                        }
                    } else if (init instanceof BinaryOperatorNode &&
                            ((BinaryOperatorNode) init).operator.c.equals("=")) {
                        if (!(((BinaryOperatorNode) init).lnode instanceof VariableNode))
                            throw new RuntimeStriker("run:literal-container:init:cannot set value to non-variable");
                        content.put(((VariableNode) ((BinaryOperatorNode) init).lnode).token.c,
                                run(((BinaryOperatorNode) init).rnode, scope));
                    } else if (init instanceof LiteralFunctionNode) {
                        List<String> args = new ArrayList<>();
                        for (Node n : ((MultiElementNode) ((LiteralFunctionNode) init).args).nodes) {
                            if (n instanceof VariableNode)
                                args.add(((VariableNode) n).token.c);
                            else throw new RuntimeStriker("run:literal-container:init:literal-function:invalid args");
                        }
                        FuncType f = new FuncType(((LiteralFunctionNode) init).name.c,
                                args, ((LiteralFunctionNode) init).code, ((LiteralFunctionNode) init).s);
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
                    break;
                }
                case "num": {
                    scope.set(((LiteralDefinitionNode) node).variable.c, new NumType(0));
                    break;
                }
                case "bool": {
                    scope.set(((LiteralDefinitionNode) node).variable.c, new BoolType(false));
                    break;
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
                else throw new RuntimeStriker("run:literal-function:invalid args");
            }
            FuncType f = new FuncType(((LiteralFunctionNode) node).name.c,
                    args, ((LiteralFunctionNode) node).code);
            f.restrictMetacalls = ((LiteralFunctionNode) node).s;
            if (f.name.equals("that"))
                return f;
            else
                scope.set(f.name, f);
        } else if (node instanceof LiteralListNode) {
            ListType l = new ListType();
            for (Node n : ((LiteralListNode) node).nodes)
                l.values.add(run(n, scope));
            return l;
        } else if (node instanceof LiteralNullNode) {
            return Void;
        } else if (node instanceof LoopStopBlockNode) {
            while (true) {
                try {
                    run(((LoopStopBlockNode) node).nodes, scope);
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
                QType cond = run(((LoopStopBlockNode) node).condition, scope);
                if (!(cond instanceof BoolType && ((BoolType) cond).value))
                    return Void;
            }
        } else if (node instanceof ThroughBlockNode) {
            double iterator;
            double threshold;
            QType rangeStart = run(((ThroughBlockNode) node).range.lnode, scope);
            QType rangeEnd = run(((ThroughBlockNode) node).range.rnode, scope);
            if (!(QType.isNum(rangeStart) && QType.isNum(rangeEnd)))
                throw new RuntimeStriker("run:through:cannot iterate through non-num range");
            else {
                iterator = ((NumType) rangeStart).value;
                threshold = ((NumType) rangeEnd).value;
            }
            double step = ((NumType) rangeStart).value < threshold ? 1 : -1;
            if (!(((ThroughBlockNode) node).variable instanceof VariableNode))
                throw new RuntimeStriker("run:through:cannot iterate with non-variable iterator");
            while (iterator <= threshold) {
                try {
                    scope.set(((VariableNode) ((ThroughBlockNode) node).variable).token.c,
                            new NumType(iterator));
                    run(((ThroughBlockNode) node).nodes, scope);
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
                run(((TryCatchNode) node).tryNodes, scope);
            } catch (RuntimeStriker striker) {
                if (striker.type.equals(RuntimeStrikerTypes.EXCEPTION)) {
                    scope.set(((TryCatchNode) node).variable.token.c, new StringType(striker.msg));
                    run(((TryCatchNode) node).catchNodes, scope);
                    scope.remove(((TryCatchNode) node).variable.token.c);
                }
            }
        } else if (node instanceof UnaryOperatorNode) {
            switch (((UnaryOperatorNode) node).operator.c) {
                case "not":
                case "negate": {
                    QType v = run(((UnaryOperatorNode) node).operand, scope);
                    if (v instanceof BoolType)
                        return new BoolType(!((BoolType) v).value);
                    else if (v instanceof NumType)
                        return new NumType(-((NumType) v).value);
                    else throw new RuntimeStriker("run:unaryop:not-negate:unacceptable value");
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
                    QType v = run(((UnaryOperatorNode) node).operand, scope);
                    if (v instanceof BoolType && ((BoolType) v).value)
                        return new BoolType(true);
                    else throw new RuntimeStriker("assertion error");
                }
                case "notnull": {
                    QType v = run(((UnaryOperatorNode) node).operand, scope);
                    return new BoolType(v != null && !(v instanceof VoidType));
                }
                case "throw": {
                    throw new RuntimeStriker(run(((UnaryOperatorNode) node).operand, scope).toString());
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
                        String lib = QFileReader.loadLibrary(path + ".q");
                        if (lib == null) throw new RuntimeStriker("run:use:import failed:file not found");

                        Lexer l = new Lexer(lib, aal, codepath);
                        l.lex();

                        Parser p = new Parser(l.fixBooleans(), aal, codepath, code);

                        Runtime compiler = new Runtime(p.parseCode(), new RuntimeConfig(), aal, codepath, code);

                        QType compiled = compiler.runTree();

                        scope.set(path.split("/")[path.split("/").length - 1], compiled);
                    } else {
                        scope.set(path, getNativeLib(path));
                    }
                    return Void;
                }
                case "return": {
                    throw new RuntimeStriker(run(((UnaryOperatorNode) node).operand, scope));
                }
            }
        } else if (node instanceof VariableNode) {
            QType v = scope.get(((VariableNode) node).token.c);
            return v != null? v : Void;
        } else if (node instanceof WhileBlockNode) {
            while (true) {
                QType cond = run(((WhileBlockNode) node).condition, scope);
                if (!(cond instanceof BoolType && ((BoolType) cond).value))
                    return Void;
                try {
                    run(((WhileBlockNode) node).nodes, scope);
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
                run(n, scope);
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
            else {
                int[] lineNo = Utilities.getLine(current.pos, code);
                striker.poschar = lineNo[1];
                striker.posline = lineNo[0];
                throw striker;
            }
        }
    }
}