package me.tapeline.quailj.runtime;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.debugging.*;
import me.tapeline.quailj.lexer.*;
import me.tapeline.quailj.libmanagement.*;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.*;
import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.builtins.intype_container.*;
import me.tapeline.quailj.runtime.builtins.intype_list.*;
import me.tapeline.quailj.runtime.builtins.intype_nums.*;
import me.tapeline.quailj.runtime.builtins.intype_string.*;
import me.tapeline.quailj.runtime.builtins.std.*;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.*;

import java.io.File;
import java.util.*;

public class Runtime {

    public static QType Void = new VoidType();
    public static List<String> nativeLibNames = new ArrayList<>(Arrays.asList(
            "random",
            "canvas"
    ));
    public static HashMap<String, ContainerType> nativeLibs = new HashMap<>();

    public final Node rootNode;
    public Memory scope;
    public final HashMap<String, List<String>> eventHandlers = new HashMap<>();
    public Node current = new Node();
    public IOManager io;
    public EmbedIntegrator embedIntegrator;
    public static CallTraceRecord mainRecord = new CallTraceRecord();
    public String path;

    public Runtime(Node rootNode, IOManager io, String path) {
        this.rootNode = rootNode;
        this.scope = new Memory();
        this.io = io;
        this.path = path;
        scope.set("scripthome", new StringType(new File(path).getParent()));
        this.embedIntegrator = new EmbedIntegrator(this);
        defineBuiltIns();
    }

    public static void registerLibrary(Library lib) {
        nativeLibNames.add(lib.getName());
        ContainerType containerType = new ContainerType("lib_" + lib.getName(), "container",
                lib.getContents(), false);
        nativeLibs.put(lib.getName(), containerType);
    }

    public void defineBuiltIns() {
        scope.set("sin",        new FuncSin());
        scope.set("cos",        new FuncCos());
        scope.set("tan",        new FuncTan());
        scope.set("asin",       new FuncAsin());
        scope.set("acos",       new FuncAcos());
        scope.set("atan",       new FuncAtan());
        scope.set("sinh",       new FuncSinh());
        scope.set("cosh",       new FuncCosh());
        scope.set("tanh",       new FuncTanh());
        scope.set("atan2",      new FuncAtan2());
        scope.set("min",        new FuncMin());
        scope.set("max",        new FuncMax());
        scope.set("abs",        new FuncAbs());

        scope.set("byte",       new FuncByte());
        scope.set("bit",        new FuncBit());
        scope.set("out",        new FuncOut());
        scope.set("put",        new FuncPut());
        scope.set("input",      new FuncInput());
        scope.set("newevent",   new FuncNewevent());
        scope.set("char",       new FuncChar());
        scope.set("ord",        new FuncOrd());
        scope.set("exec",       new FuncExec());
        scope.set("table",      new FuncTable());
        scope.set("thread",     new FuncThread());

        scope.set("clock",  new FuncClock());
        scope.set("millis", new FuncMillis());

        scope.set("refreshtypes",   new FuncRefreshtypes());
        scope.set("tostring",       new FuncTostring());
        scope.set("tonum",          new FuncTonum());
        scope.set("tobool",         new FuncTobool());
        scope.set("copy",           new FuncCopy());
        scope.set("embed",          new FuncEmbed());
        scope.set("registerhandler",new FuncRegisterhandler());

        scope.set("filewrite",      new FuncFilewrite());
        scope.set("fileread",       new FuncFileread());
        scope.set("binfileread",    new FuncBinfileread());
        scope.set("fileexists",     new FuncFileexists());

        scope.set("nothing",        new VoidType());
        scope.set("million",        new NumType(1000000D));
        scope.set("billion",        new NumType(1000000000D));
        scope.set("trillion",       new NumType(1000000000000D));

        scope.set("Number", new ContainerType("Number", "container",
                new HashMap<>(), false));
        NumType.tableToClone.put("floor",   new NumFuncFloor());
        NumType.tableToClone.put("ceil",    new NumFuncCeil());
        NumType.tableToClone.put("round",   new NumFuncRound());

        scope.set("Null", new ContainerType("Null", "container",
                new HashMap<>(), false));

        scope.set("String", new ContainerType("String", "container",
                new HashMap<>(), false));
        StringType.tableToClone.put("get",              new StringFuncGet());
        StringType.tableToClone.put("replace",          new StringFuncReplace());
        StringType.tableToClone.put("size",             new StringFuncSize());
        StringType.tableToClone.put("sub",              new StringFuncSub());
        StringType.tableToClone.put("upper",            new StringFuncUpper());
        StringType.tableToClone.put("lower",            new StringFuncLower());
        StringType.tableToClone.put("capitalize",       new StringFuncCapitalize());
        StringType.tableToClone.put("split",            new StringFuncSplit());
        StringType.tableToClone.put("find",             new StringFuncFind());
        StringType.tableToClone.put("reverse",          new StringFuncReverse());
        StringType.tableToClone.put("count",            new StringFuncCount());
        StringType.tableToClone.put("endswith",         new StringFuncEndswith());
        StringType.tableToClone.put("isalpha",          new StringFuncIsalpha());
        StringType.tableToClone.put("isalphanumeric",   new StringFuncIsalphanumeric());
        StringType.tableToClone.put("isnum",            new StringFuncIsnum());
        StringType.tableToClone.put("isuppercase",      new StringFuncIsuppercase());
        StringType.tableToClone.put("islowercase",      new StringFuncIslowercase());
        StringType.tableToClone.put("startswith",       new StringFuncStartswith());

        scope.set("Bool", new ContainerType("Bool", "container",
                new HashMap<>(), false));

        scope.set("List", new ContainerType("List", "container",
                new HashMap<>(), false));
        ListType.tableToClone.put("add",            new ListFuncAdd());
        ListType.tableToClone.put("find",           new ListFuncFind());
        ListType.tableToClone.put("get",            new ListFuncGet());
        ListType.tableToClone.put("set",            new ListFuncSet());
        ListType.tableToClone.put("remove",         new ListFuncRemove());
        ListType.tableToClone.put("removeitem",     new ListFuncRemoveitem());
        ListType.tableToClone.put("reverse",        new ListFuncReverse());
        ListType.tableToClone.put("size",           new ListFuncSize());
        ListType.tableToClone.put("clear",          new ListFuncClear());
        ListType.tableToClone.put("count",          new ListFuncCount());

        ContainerType.tableToClone.put("contains",   new ContainerFuncContains());
        ContainerType.tableToClone.put("keys",       new ContainerFuncKeys());
        ContainerType.tableToClone.put("get",        new ContainerFuncGet());
        ContainerType.tableToClone.put("remove",     new ContainerFuncRemove());
        ContainerType.tableToClone.put("set",        new ContainerFuncSet());
        ContainerType.tableToClone.put("allkeys",    new ContainerFuncAllkeys());
        ContainerType.tableToClone.put("pairs",      new ContainerFuncPairs());
        ContainerType.tableToClone.put("allpairs",   new ContainerFuncAllpairs());
        ContainerType.tableToClone.put("assemble",   new ContainerFuncAssemble());
        ContainerType.tableToClone.put("values",     new ContainerFuncValues());
        ContainerType.tableToClone.put("size",       new ContainerFuncSize());
        ContainerType.tableToClone.put("alltostring",new ContainerFuncAlltostring());
        scope.set("Container", new ContainerType("Container", "container",
                new HashMap<>(), false));

    }

    public ContainerType getNative(String id) throws RuntimeStriker {
        return nativeLibs.get(id);
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

    public static void callEvent(Runtime runtime, String event, ContainerType meta) throws RuntimeStriker {
        ContainerType metadata = new ContainerType("_anonymous", "container", new HashMap<>(), false);
        if (meta != null) metadata = meta;
        //if (runtime.eventHandlers.containsKey(event)) return;
        if (runtime.eventHandlers.get(event) == null) return;
        for (String handler : runtime.eventHandlers.get(event)) {
            QType func = runtime.scope.get(handler);
            if (!(func instanceof FuncType))
                throw new RuntimeStriker("call event:event handler can only be a function");
            QType result = ((FuncType) func).run(runtime, Collections.singletonList(metadata));
            if (QType.isNull(result) || (QType.isBool(result) && !((BoolType) result).value))
                break;
        }
    }

    public QType run(Node node, Memory scope) throws RuntimeStriker {
        if (node == null) current.codePos = 0;
        else current.codePos = node.codePos;
        try {
            if (node instanceof BinaryOperatorNode) {
                QType av = QType.nullSafe(run(((BinaryOperatorNode) node).lnode, scope));
                QType bv = QType.nullSafe(run(((BinaryOperatorNode) node).rnode, scope));
                // String containerImpl = Utilities.transformOp(((BinaryOperatorNode) node).operator.c);
                String containerImpl = Utilities.opToString.get(((BinaryOperatorNode) node).operator.c);
                if (containerImpl != null && av.table != null &&
                        av.table.containsKey(containerImpl) && av.table.get(containerImpl) instanceof FuncType) {
                    List<QType> metaArgs = new ArrayList<>(Arrays.asList(av, bv));
                    return ((FuncType) av.table.get(containerImpl)).run(this, metaArgs);
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
                        if (QType.isNum(av, bv)) {
                            assert av instanceof NumType;
                            double avx = ((NumType) av).value;
                            double bvx = ((NumType) bv).value;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "+":
                                    return new NumType(avx + bvx);
                                case "-":
                                    return new NumType(avx - bvx);
                                case "*":
                                    return new NumType(avx * bvx);
                                case "/":
                                    return new NumType(avx / bvx);
                                case ">":
                                    return new BoolType(avx > bvx);
                                case "<":
                                    return new BoolType(avx < bvx);
                                case "<=":
                                    return new BoolType(avx >= bvx);
                                case ">=":
                                    return new BoolType(avx <= bvx);
                                case "//":
                                    return new NumType(Math.floor(avx / bvx));
                            }
                        } else if (QType.isNum(bv) && QType.isStr(av)) {
                            assert av instanceof StringType;
                            String avx = ((StringType) av).value;
                            double bvx = ((NumType) bv).value;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "*":
                                    return new StringType(StringUtils.mult(avx, bvx));
                                case "/":
                                    return ListUtils.newListType(StringUtils.div(avx, bvx));
                                case ">":
                                    return new BoolType(avx.length() > bvx);
                                case "<":
                                    return new BoolType(avx.length() < bvx);
                                case "<=":
                                    return new BoolType(avx.length() <= bvx);
                                case ">=":
                                    return new BoolType(avx.length() >= bvx);
                                case "//":
                                    return ListUtils.newListType(StringUtils.mod(avx, bvx));
                            }
                        } else if (QType.isNum(bv) && QType.isList(av)) {
                            assert av instanceof ListType;
                            List<QType> avx = ((ListType) av).values;
                            double bvx = ((NumType) bv).value;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "*":
                                    return new ListType(ListUtils.mult(avx, bvx));
                                case "/":
                                    return new ListType(ListUtils.div(avx, bvx));
                                case ">":
                                    return new BoolType(avx.size() > bvx);
                                case "<":
                                    return new BoolType(avx.size() < bvx);
                                case "<=":
                                    return new BoolType(avx.size() <= bvx);
                                case ">=":
                                    return new BoolType(avx.size() >= bvx);
                                case "//":
                                    return new ListType(ListUtils.mod(avx, bvx));
                            }
                        } else if (QType.isList(av, bv)) {
                            assert av instanceof ListType;
                            List<QType> avx = ((ListType) av).values;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "+":
                                    return ListUtils.concat((ListType) av, (ListType) bv);
                                case "-":
                                    return new ListType(ListUtils.removeAll(avx, ((ListType) bv).values));
                            }
                        } else if (QType.isStr(av, bv)) {
                            assert av instanceof StringType;
                            String avx = ((StringType) av).value;
                            String bvx = ((StringType) bv).value;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "+":
                                    return new StringType(avx + bvx);
                                case "-":
                                    return new StringType(avx.replaceAll(bvx, ""));
                            }
                        }
                        break;
                    }
                    case "==":
                    case "is": {
                        BoolType c = Utilities.compare(av, bv);
                        if (c != null)
                            return c;
                        break;
                    }
                    case "!=": {
                        BoolType c = Utilities.compare(av, bv);
                        if (c != null)
                            return new BoolType(!c.value);
                        else return new BoolType(false);
                    }
                    case "^": {
                        if (QType.isNum(av, bv))
                            return new NumType(Math.pow(((NumType) av).value, ((NumType) bv).value));
                        break;
                    }
                    case "%": {
                        if (QType.isNum(av, bv))
                            return new NumType(((NumType) av).value % ((NumType) bv).value);
                        break;
                    }
                    case "and": {
                        if (QType.isBool(av, bv))
                            return new BoolType(((BoolType) av).value && ((BoolType) bv).value);
                        break;
                    }
                    case "or": {
                        if (QType.isBool(av, bv))
                            return new BoolType(((BoolType) av).value || ((BoolType) bv).value);
                        else if (av instanceof VoidType && bv instanceof VoidType) return Void;
                        else if (av instanceof VoidType) return bv;
                        else if (bv instanceof VoidType) return av;
                        else return av;
                    }
                    case "is type of":
                    case "instanceof": {
                        if (av instanceof VoidType)
                            return new BoolType(false);
                        if (QType.isStr(bv)) {
                            String bvx = ((StringType) bv).value;
                            if ((QType.isNum(av) && bvx.equals("num")) ||
                                    (QType.isBool(av) && bvx.equals("bool")) ||

                                    (QType.isStr(av) && bvx.equals("string")) ||
                                    (QType.isList(av) && bvx.equals("list")) ||
                                    (QType.isCont(av) && bvx.equals("container")) ||
                                    (QType.isFunc(av) && bvx.equals("function")) ||
                                    (QType.isJava(av) && bvx.equals("javatype")))
                                return new BoolType(true);
                            if (QType.isCont(av) && (((ContainerType) av).name().equals(bvx) ||
                                    ((ContainerType) av).like().equals(bvx)))
                                return new BoolType(true);
                        }
                        break;
                    }
                    case "is same type as": {
                        if (QType.isNum(av, bv) ||
                                QType.isList(av, bv) ||
                                QType.isCont(av, bv) ||
                                QType.isStr(av, bv) ||
                                QType.isBool(av, bv) ||
                                QType.isJava(av, bv) ||
                                QType.isFunc(av, bv))
                            return new BoolType(true);
                        break;
                    }
                    case "=": {
                        Node lnode = ((BinaryOperatorNode) node).lnode;
                        if (!(lnode instanceof VariableNode))
                            throw new RuntimeStriker("run:binaryop:set:cannot place value to non-variable type",
                                    current.codePos);
                        mainRecord.action(new AssignTraceRecord(lnode, run(lnode, scope), bv));
                        scope.set(((VariableNode) lnode).token.c, bv);
                        return Void;
                    }
                    case "<-": {
                        if (!(av instanceof RefType))
                            throw new RuntimeStriker("run:binaryop:set:cannot place value by ref. with non-ref",
                                    current.codePos);
                        ((RefType) av).object = bv;
                        return Void;
                    }
                    case ":":
                    case ":+": {
                        Assert.require(QType.isNum(av, bv), "run:binaryop:range:expected numbers", current.codePos);
                        ListType l = new ListType();
                        double avx = ((NumType) av).value;
                        double bvx = ((BinaryOperatorNode) node).operator.c.equals(":+")?
                                ((NumType) bv).value : ((NumType) bv).value - 1;
                        int step = avx < bvx ? 1 : -1;
                        double iterator = avx;
                        while (true) {
                            if (avx < bvx) if (iterator > bvx) break;
                            if (avx > bvx) if (iterator < bvx) break;
                            l.values.add(new NumType(iterator));
                            iterator += step;
                        }
                        return l;
                    }
                    case "step": {
                        Assert.require(((BinaryOperatorNode) node).lnode instanceof BinaryOperatorNode,
                                "run:binaryop:step:expected range as lvalue", current.codePos);
                        Assert.require(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).operator.c.equals(":"),
                                "run:binaryop:step:expected range as lvalue", current.codePos);
                        QType rangeStart = run(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).lnode, scope);
                        QType rangeEnd = run(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).rnode, scope);
                        QType rangeStep = run(((BinaryOperatorNode) node).rnode, scope);
                        Assert.require(QType.isNum(rangeStart, rangeEnd, rangeStep),
                                "run:binaryop:step:expected numbers", current.codePos);
                        ((NumType) rangeEnd).value = ((BinaryOperatorNode) node).operator.c.equals(":+")?
                                ((NumType) rangeEnd).value : ((NumType) rangeEnd).value - 1;
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
                    case "in": {
                        if (QType.isStr(av, bv)) {
                            return new BoolType(
                                    ((StringType) av).value.contains(((StringType) bv).value));
                        } else if (QType.isList(bv)) {
                            for (int i = 0; i < ((ListType) bv).values.size(); i++)
                                if (Utilities.compare(av, ((ListType) bv).values.get(i)).value)
                                    return new BoolType(true);
                            return new BoolType(false);
                        } else throw new RuntimeStriker("run:binaryop:in:b should be list or string");
                    }
                }
                throw new RuntimeStriker("run:binaryop:no valid case for " + node + " (" +
                        av + ", " + bv + ")", current.codePos);

            } else if (node instanceof BlockNode) {
                for (Node n : ((BlockNode) node).nodes)
                    run(n, scope);

            } else if (node instanceof EveryBlockNode) {
                QType range = run(((EveryBlockNode) node).expr, scope);
                List<QType> iterable = new ArrayList<>();
                if (QType.isStr(range))
                    for (char c : ((StringType) range).value.toCharArray())
                        iterable.add(new StringType(c + ""));
                else if (range instanceof ListType) iterable = ((ListType) range).values;
                else throw new RuntimeStriker("run:every loop:invalid range " + range +
                            ". Only list and string are supported");
                int doRethrow = 0;
                String var = ((EveryBlockNode) node).variable.token.c;
                for (QType q : iterable) {
                    scope.set(var, q);
                    try {
                        run(((EveryBlockNode) node).nodes, scope);
                    } catch (RuntimeStriker striker) {
                        if (striker.type.equals(RuntimeStrikerType.BREAK)) {
                            if (--striker.health > 0) doRethrow = striker.health;
                            break;
                        }
                        else if (striker.type.equals(RuntimeStrikerType.CONTINUE)) continue;
                        else throw striker;
                    }
                }
                if (doRethrow > 0) throw new RuntimeStriker(RuntimeStrikerType.BREAK, doRethrow);

            } else if (node instanceof FieldReferenceNode) {
                if (!(((FieldReferenceNode) node).rnode instanceof VariableNode))
                    throw new RuntimeStriker("run:field set:cannot get value of non-variable type " + node,
                            current.codePos);
                return run(((FieldReferenceNode) node).lnode, scope).table.get(
                        ((VariableNode) ((FieldReferenceNode) node).rnode).token.c);

            } else if (node instanceof FieldSetNode) {
                QType parent = run(((FieldSetNode) node).lnode, scope);
                if (!(((FieldSetNode) node).rnode instanceof VariableNode))
                    throw new RuntimeStriker("run:field set:cannot set value of non-variable type" + node,
                            current.codePos);
                parent.table.put(((VariableNode) ((FieldSetNode) node).rnode).token.c,
                        run(((FieldSetNode) node).value, scope));

            } else if (node instanceof FunctionCallNode) {
                QType callee = run(((FunctionCallNode) node).id, scope);
                List<QType> args = new ArrayList<>();
                for (Node a : Parser.multiElementIfNeeded(((FunctionCallNode) node).args).nodes)
                    args.add(run(a, scope));
                if (callee == null) throw new RuntimeStriker("run:call:cannot call null " + node);
                if (!(QType.isCont(callee) || QType.isFunc(callee)) &&
                    !QType.isFunc(callee.nullSafeGet("_call"))) throw new RuntimeStriker(
                            "run:call:cannot call " + callee + " in " + node
                    );
                if (QType.isCont(callee)) { // If constructor
                    QType prototype = callee.copy();
                    if (QType.isFunc(prototype.nullSafeGet("_builder"))) {
                        args.add(0, prototype);
                        ((FuncType) prototype.nullSafeGet("_builder")).run(this, args);
                    }
                    return prototype;
                } else if (((FunctionCallNode) node).id instanceof FieldReferenceNode) {
                    QType parent = run(((FieldReferenceNode) ((FunctionCallNode) node).id).lnode, scope);
                    if (QType.isFunc(callee.nullSafeGet("_call"))) {
                        args.add(0, parent);
                        return ((FuncType) callee.nullSafeGet("_call")).run(this, args);
                    }
                    if (!(QType.isCont(parent) &&
                        !((ContainerType) parent).isMeta() &&
                        !ContainerType.tableToClone.containsKey(
                           ((FieldReferenceNode) ((FunctionCallNode) node).id).rnode.toString()))) {
                        args.add(0, parent);
                    }
                    return ((FuncType) parent.nullSafeGet(
                            ((FieldReferenceNode) ((FunctionCallNode) node).id).rnode.toString())).run(
                            this, args);
                } else return ((FuncType) callee).run(this, args);

            } else if (node instanceof EffectNode) {
                switch (((EffectNode) node).operator.c) {
                    case "assert": {
                        QType v = run(((EffectNode) node).operand, scope);
                        if (v instanceof VoidType || (v instanceof BoolType && !((BoolType) v).value))
                            throw new RuntimeStriker("assert:" + ((EffectNode) node).operand.toString(),
                                    current.codePos);
                    }
                    case "use":
                    case "using":
                    case "deploy": {
                        String lib = ((EffectNode) node).operand instanceof LiteralStringNode ?
                                ((LiteralStringNode) ((EffectNode) node).operand).token.c :
                                (((EffectNode) node).operand instanceof VariableNode ?
                                        ((VariableNode) ((EffectNode) node).operand).token.c : null);
                        if (lib == null) throw new RuntimeStriker("run:use:invalid operand", current.codePos);
                        String id = lib.replaceAll("/", "_");
                        id = id.replaceAll(":", "_");
                        id = id.replaceAll("\\\\", "_");
                        id = id.replaceAll("\\.", "_");
                        QType loaded;
                        if (Runtime.nativeLibNames.contains(lib))
                            loaded = getNative(lib);
                        else {
                            String code = IOManager.loadLibrary(lib);
                            String path = IOManager.pathLibrary(lib);
                            Lexer lexer = new Lexer(code);
                            List<Token> tokens = lexer.lexAndFix();
                            Parser parser = new Parser(tokens);
                            Node node1 = parser.parse();
                            Runtime runtime = new Runtime(node1, io, path);
                            try {
                                loaded = runtime.run(node1, runtime.scope);
                            } catch (RuntimeStriker striker) {
                                loaded = striker.val;
                                if (striker.type == RuntimeStrikerType.EXCEPTION) {
                                    throw striker;
                                }
                            }
                        }
                        if (((EffectNode) node).operator.c.equals("deploy")) {
                            QType.forEachNotBuiltIn(loaded, (k, v) -> {
                                if (!scope.hasParentalDefinition(k))
                                    scope.set(k, v);
                            });
                        } else {
                            if (((EffectNode) node).other.equals("_defaultname"))
                                scope.set(id, loaded);
                            else scope.set(((EffectNode) node).other, loaded);
                        }
                        if (loaded.nullSafeGet("_events") instanceof ListType) {
                            for (QType q : ((ListType) loaded.nullSafeGet("_events")).values) {
                                if (q instanceof ContainerType) {
                                    Assert.require(q.nullSafeGet("consumer") instanceof FuncType &&
                                            q.nullSafeGet("event") instanceof StringType,
                                            "run:use:handler migrating is defined, but handler "
                                                    + q + " is invalid");
                                    String event = ((StringType) q.nullSafeGet("event")).value;
                                    int i = 0;
                                    while (!(scope.get("_eventhandler_migrated_" + event + "_" + i)
                                            instanceof VoidType)) i++;
                                    ((FuncType) q.table.get("consumer")).name = "_eventhandler_migrated_" +
                                            event + "_" + i;
                                    scope.set(((FuncType) q.table.get("consumer")).name, q.table.get("consumer"));
                                    if (eventHandlers.containsKey(event)) {
                                        List<String> e = eventHandlers.get(event);
                                        e.add(((FuncType) q.nullSafeGet("consumer")).name);
                                        eventHandlers.put(event, e);
                                    }
                                    else eventHandlers.put(event, new ArrayList<>(Collections.singletonList(
                                            ((FuncType) q.nullSafeGet("consumer")).name)));
                                }
                            }
                        }
                        break;
                    }
                    case "throw": {
                        throw new RuntimeStriker(run(((EffectNode) node).operand, scope).toString(), current.codePos);
                    }
                    case "return": {
                        throw new RuntimeStriker(run(((EffectNode) node).operand, scope));
                    }
                    case "strike": {
                        QType v = run(((EffectNode) node).operand, scope);
                        Assert.require(QType.isNum(v), "run:effect:strike:specify a num value");
                        throw new RuntimeStriker(RuntimeStrikerType.BREAK, ((NumType) v).value);
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
                            QType elseIfCondition = run(((ElseIfBlockNode) linked).condition, scope);
                            if (elseIfCondition instanceof BoolType && ((BoolType) elseIfCondition).value) {
                                run(((ElseIfBlockNode) linked).nodes, scope);
                                break;
                            }
                        }
                    }
                }

            } else if (node instanceof IndexReferenceNode) {
                QType parent = run(((IndexReferenceNode) node).lnode, scope);
                QType index = run(((IndexReferenceNode) node).rnode, scope);
                if (parent instanceof ListType) {
                    Assert.require(QType.isNum(index), "run:index:list:index should be number");
                    Assert.require(((ListType) parent).values.size() >
                            (int) Math.round(((NumType) index).value), "run:index:list:out of bounds");
                    return ((ListType) parent).values.get((int) Math.round(((NumType) index).value));
                } else if (parent.nullSafeGet("_index") instanceof FuncType) {
                    return ((FuncType) parent.nullSafeGet("_index")).run(this,
                            Arrays.asList(parent, index));
                } else if (parent instanceof ContainerType) {
                    return parent.nullSafeGet(index.toString());
                } else throw new RuntimeStriker("run:index:" + ((IndexReferenceNode) node).lnode +
                        " is not indexable", current.codePos);

            } else if (node instanceof IndexSetNode) {
                QType parent = run(((IndexSetNode) node).lnode, scope);
                QType index = run(((IndexSetNode) node).rnode, scope);
                QType value = run(((IndexSetNode) node).value, scope);
                if (parent instanceof ListType) {
                    Assert.require(QType.isNum(index), "run:index:list:index should be number");
                    Assert.require(((ListType) parent).values.size() >
                            (int) Math.round(((NumType) index).value), "run:index:list:out of bounds");
                    ((ListType) parent).values.set((int) Math.round(((NumType) index).value), value);
                    return Void;
                } else if (parent.nullSafeGet("_setindex") instanceof FuncType) {
                    return ((FuncType) parent.nullSafeGet("_setindex")).run(this,
                            Arrays.asList(parent, index, value));
                } else if (parent instanceof ContainerType) {
                    return parent.table.put(index.toString(), value);
                } else throw new RuntimeStriker("run:index:lvalue " + node +
                        " is not indexable", current.codePos);


            } else if (node instanceof InstructionNode) {
                switch (((InstructionNode) node).token.c) {
                    case "break":
                        throw new RuntimeStriker(RuntimeStrikerType.BREAK);
                    case "continue":
                        throw new RuntimeStriker(RuntimeStrikerType.CONTINUE);
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
                                ":Inheritance error, cannot inherit from a non-container type", current.codePos);
                    container = inheritContainer((ContainerType) parent, container);
                }
                for (Node n : ((LiteralContainerNode) node).initialize) {
                    if (n instanceof BinaryOperatorNode && ((BinaryOperatorNode) n).lnode instanceof VariableNode) {
                        container.table.put(((VariableNode) ((BinaryOperatorNode) n).lnode).token.c,
                                run(((BinaryOperatorNode) n).rnode, scope));
                    } else if (n instanceof BinaryOperatorNode) {
                        container.table.put(run(((BinaryOperatorNode) n).lnode, scope).toString(),
                                run(((BinaryOperatorNode) n).rnode, scope));
                    } else if (n instanceof LiteralFunctionNode) {
                        List<VariableNode> args = new ArrayList<>();
                        Parser.multiElementIfNeeded(((LiteralFunctionNode) n).args).nodes.forEach(
                                (v) -> args.add((VariableNode) v)
                        );
                        FuncType f = new FuncType(((LiteralFunctionNode) n).name.c, args,
                                ((LiteralFunctionNode) n).code, ((LiteralFunctionNode) n).isStatic);
                        container.table.put(((LiteralFunctionNode) n).name.c, f);
                    }
                }
                scope.set(((LiteralContainerNode) node).name, container);
                return container;

            } else if (node instanceof EventNode) {
                Node nodeArg = ((MultiElementNode) ((EventNode) node).event.args).nodes.get(0);
                if (!(nodeArg instanceof VariableNode))
                    throw new RuntimeStriker("run:init handler:cannot put event value to non-variable");
                String event = run(((EventNode) node).event.id, scope).toString();
                FuncType f = new FuncType("_eventhandler_" + event + "_",
                        Collections.singletonList(((VariableNode) nodeArg).token.c),
                        Parser.blockIfNeeded(((EventNode) node).code),
                        true);
                int id = 0;
                while (!(scope.get(f.name + id) instanceof VoidType)) id++;
                f.name += id;
                scope.set(f.name, f);
                if (eventHandlers.containsKey(event)) {
                    List<String> e = eventHandlers.get(event);
                    e.add(f.name);
                    eventHandlers.put(event, e);
                }
                else eventHandlers.put(event, new ArrayList<>(Collections.singletonList(f.name)));
                return new VoidType();

            } else if (node instanceof LiteralFunctionNode) {
                List<VariableNode> args = new ArrayList<>();
                Parser.multiElementIfNeeded(((LiteralFunctionNode) node).args).nodes.forEach(
                        (v) -> args.add((VariableNode) v)
                );
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
                int doRethrow = 0;
                while (true) {
                    try {
                        run(((LoopStopBlockNode) node).nodes, scope);
                        QType condition = run(((LoopStopBlockNode) node).condition, scope);
                        if (condition instanceof BoolType && ((BoolType) condition).value) break;
                    } catch (RuntimeStriker striker) {
                        if (striker.type.equals(RuntimeStrikerType.BREAK)) {
                            if (--striker.health > 0) doRethrow = striker.health;
                            break;
                        }
                        else if (striker.type.equals(RuntimeStrikerType.CONTINUE)) continue;
                        else throw striker;
                    }
                }
                if (doRethrow > 0) throw new RuntimeStriker(RuntimeStrikerType.BREAK, doRethrow);

            } else if (node instanceof MultiElementNode) {
                ListType list = new ListType();
                for (Node n : ((MultiElementNode) node).nodes)
                    list.values.add(run(n, scope));
                return list;

            } else if (node instanceof ThroughBlockNode) {
                QType baseV;
                QType ceilV;
                QType stepV = null;
                BinaryOperatorNode range = ((ThroughBlockNode) node).range;
                if (range.operator.c.equals("step")) {
                    stepV = run(range.rnode, scope);
                    baseV = run(((BinaryOperatorNode) range.lnode).lnode, scope);
                    ceilV = run(((BinaryOperatorNode) range.lnode).rnode, scope);
                } else {
                    baseV = run(range.lnode, scope);
                    ceilV = run(range.rnode, scope);
                }
                ((NumType) ceilV).value = range.operator.c.equals(":+")?
                        ((NumType) ceilV).value : ((NumType) ceilV).value - 1;
                if (!QType.isNum(baseV, ceilV))
                    throw new RuntimeStriker("run:through:There are some non-num values", current.codePos);
                if (!(stepV instanceof NumType) && stepV != null)
                    throw new RuntimeStriker("run:through:There are some non-num values", current.codePos);
                double iterator = ((NumType) baseV).value;
                double till = ((NumType) ceilV).value;
                double step = stepV == null ? (iterator > till ? -1 : 1) : ((NumType) stepV).value;
                Node toRun = ((ThroughBlockNode) node).nodes;
                String var = ((ThroughBlockNode) node).variable.token.c;
                int doRethrow = 0;
                while (true) {
                    if (((NumType) baseV).value < till && (iterator > till)) break;
                    if (((NumType) baseV).value > till && (iterator < till)) break;
                    scope.set(var, new NumType(iterator));
                    try {
                        run(toRun, scope);
                    } catch (RuntimeStriker striker) {
                        if (striker.type.equals(RuntimeStrikerType.BREAK)) {
                            if (--striker.health > 0) doRethrow = striker.health;
                            break;
                        }
                        else if (striker.type.equals(RuntimeStrikerType.CONTINUE)) {
                            iterator += step;
                            continue;
                        } else throw striker;
                    }
                    iterator += step;
                }
                if (doRethrow > 0) throw new RuntimeStriker(RuntimeStrikerType.BREAK, doRethrow);

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
                                "run:unary:!:Non-bool operand", current.codePos);
                        return new BoolType(!((BoolType) v).value);
                    }
                    case "negate":
                    case "-": {
                        QType v = run(((UnaryOperatorNode) node).operand, scope);
                        Assert.require(QType.isNum(v),
                                "run:unary:-:Non-num operand", current.codePos);
                        return new NumType(-((NumType) v).value);
                    }
                    case "notnull":
                    case "exists": {
                        QType v = run(((UnaryOperatorNode) node).operand, scope);
                        return new BoolType(!(v instanceof VoidType));
                    }
                    case "&": {
                        return new RefType(run(((UnaryOperatorNode) node).operand, scope));
                    }
                    case "*": {
                        QType v = run(((UnaryOperatorNode) node).operand, scope);
                        Assert.require(v instanceof RefType,
                                "run:unary:*:cannot dereference non-ref. value " + v);
                        return ((RefType) v).object;
                    }
                    case "##": {
                        return new NumType(run(((UnaryOperatorNode) node).operand, scope).hashCode());
                    }
                }

            } else if (node instanceof VariableNode) {
                return scope.get(((VariableNode) node).token.c);

            } else if (node instanceof WhileBlockNode) {
                int doRethrow = 0;
                while (true) {
                    try {
                        QType condition = run(((WhileBlockNode) node).condition, scope);
                        if (condition instanceof BoolType && !((BoolType) condition).value)
                            break;
                        run(((WhileBlockNode) node).nodes, scope);
                    } catch (RuntimeStriker striker) {
                        if (striker.type.equals(RuntimeStrikerType.BREAK)) {
                            if (--striker.health > 0) doRethrow = striker.health;
                            break;
                        }
                        else if (striker.type.equals(RuntimeStrikerType.CONTINUE))
                            continue;
                        else if (striker.type.equals(RuntimeStrikerType.RETURN))
                            throw striker;
                        else if (striker.type.equals(RuntimeStrikerType.EXCEPTION))
                            throw striker;
                    }
                }
                if (doRethrow > 0) throw new RuntimeStriker(RuntimeStrikerType.BREAK, doRethrow);
            }
        } catch (RuntimeStriker striker) {
            if (striker.type.equals(RuntimeStrikerType.EXCEPTION)) {
                if (striker.posChar <= 0)
                    striker.posChar = current.codePos;
            }
            throw striker;
        }

        return Void;
    }
}
