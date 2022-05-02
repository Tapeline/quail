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

    public static QValue Void = new QValue(new VoidType());
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
        scope.set("scripthome", new QValue((new File(path).getParent())));
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
        scope.set("out",        new QValue(new FuncOut()));
        scope.set("put",        new QValue(new FuncPut()));
        scope.set("input",      new QValue(new FuncInput()));
        scope.set("newevent",   new QValue(new FuncNewevent()));
        scope.set("char",       new QValue(new FuncChar()));
        scope.set("ord",        new QValue(new FuncOrd()));
        scope.set("exec",       new QValue(new FuncExec()));
        scope.set("table",      new QValue(new FuncTable()));
        scope.set("thread",     new QValue(new FuncThread()));

        scope.set("clock",  new QValue(new FuncClock()));
        scope.set("millis", new QValue(new FuncMillis()));

        scope.set("refreshtypes",   new QValue(new FuncRefreshtypes()));
        scope.set("tostring",       new QValue(new FuncTostring()));
        scope.set("tonum",          new QValue(new FuncTonum()));
        scope.set("tobool",         new QValue(new FuncTobool()));
        scope.set("copy",           new QValue(new FuncCopy()));
        scope.set("embed",          new QValue(new FuncEmbed()));
        scope.set("registerhandler",new QValue(new FuncRegisterhandler()));

        scope.set("filewrite",      new QValue(new FuncFilewrite()));
        scope.set("fileread",       new QValue(new FuncFileread()));
        scope.set("fileexists",     new QValue(new FuncFileexists()));

        scope.set("nothing",        new QValue(new VoidType()));
        scope.set("million",        new QValue(new NumType(1000000D)));
        scope.set("billion",        new QValue(new NumType(1000000000D)));
        scope.set("trillion",       new QValue(new NumType(1000000000000D)));

        scope.set("Number", new QValue(new ContainerType("Number", "container",
                new HashMap<>(), false)));
        NumType.tableToClone.put("floor",   new QValue(new NumFuncFloor()));
        NumType.tableToClone.put("ceil",    new QValue(new NumFuncCeil()));
        NumType.tableToClone.put("round",   new QValue(new NumFuncRound()));

        scope.set("Null", new QValue(new ContainerType("Null", "container",
                new HashMap<>(), false)));

        scope.set("String", new QValue(new ContainerType("String", "container",
                new HashMap<>(), false)));
        StringType.tableToClone.put("get",              new QValue(new StringFuncGet()));
        StringType.tableToClone.put("replace",          new QValue(new StringFuncReplace()));
        StringType.tableToClone.put("size",             new QValue(new StringFuncSize()));
        StringType.tableToClone.put("sub",              new QValue(new StringFuncSub()));
        StringType.tableToClone.put("upper",            new QValue(new StringFuncUpper()));
        StringType.tableToClone.put("lower",            new QValue(new StringFuncLower()));
        StringType.tableToClone.put("capitalize",       new QValue(new StringFuncCapitalize()));
        StringType.tableToClone.put("split",            new QValue(new StringFuncSplit()));
        StringType.tableToClone.put("find",             new QValue(new StringFuncFind()));
        StringType.tableToClone.put("reverse",          new QValue(new StringFuncReverse()));
        StringType.tableToClone.put("count",            new QValue(new StringFuncCount()));
        StringType.tableToClone.put("endswith",         new QValue(new StringFuncEndswith()));
        StringType.tableToClone.put("isalpha",          new QValue(new StringFuncIsalpha()));
        StringType.tableToClone.put("isalphanumeric",   new QValue(new StringFuncIsalphanumeric()));
        StringType.tableToClone.put("isnum",            new QValue(new StringFuncIsnum()));
        StringType.tableToClone.put("isuppercase",      new QValue(new StringFuncIsuppercase()));
        StringType.tableToClone.put("islowercase",      new QValue(new StringFuncIslowercase()));
        StringType.tableToClone.put("startswith",       new QValue(new StringFuncStartswith()));

        scope.set("Bool", new QValue(new ContainerType("Bool", "container",
                new HashMap<>(), false)));

        scope.set("List", new QValue(new ContainerType("List", "container",
                new HashMap<>(), false)));
        ListType.tableToClone.put("add",            new QValue(new ListFuncAdd()));
        ListType.tableToClone.put("find",           new QValue(new ListFuncFind()));
        ListType.tableToClone.put("get",            new QValue(new ListFuncGet()));
        ListType.tableToClone.put("set",            new QValue(new ListFuncSet()));
        ListType.tableToClone.put("remove",         new QValue(new ListFuncRemove()));
        ListType.tableToClone.put("removeitem",     new QValue(new ListFuncRemoveitem()));
        ListType.tableToClone.put("reverse",        new QValue(new ListFuncReverse()));
        ListType.tableToClone.put("size",           new QValue(new ListFuncSize()));
        ListType.tableToClone.put("clear",          new QValue(new ListFuncClear()));
        ListType.tableToClone.put("count",          new QValue(new ListFuncCount()));

        ContainerType.tableToClone.put("contains",   new QValue(new ContainerFuncContains()));
        ContainerType.tableToClone.put("keys",       new QValue(new ContainerFuncKeys()));
        ContainerType.tableToClone.put("get",        new QValue(new ContainerFuncGet()));
        ContainerType.tableToClone.put("remove",     new QValue(new ContainerFuncRemove()));
        ContainerType.tableToClone.put("set",        new QValue(new ContainerFuncSet()));
        ContainerType.tableToClone.put("allkeys",    new QValue(new ContainerFuncAllkeys()));
        ContainerType.tableToClone.put("pairs",      new QValue(new ContainerFuncPairs()));
        ContainerType.tableToClone.put("allpairs",   new QValue(new ContainerFuncAllpairs()));
        ContainerType.tableToClone.put("assemble",   new QValue(new ContainerFuncAssemble()));
        ContainerType.tableToClone.put("values",     new QValue(new ContainerFuncValues()));
        ContainerType.tableToClone.put("size",       new QValue(new ContainerFuncSize()));
        ContainerType.tableToClone.put("alltostring",new QValue(new ContainerFuncAlltostring()));
        scope.set("Container", new QValue(new ContainerType("Container", "container",
                new HashMap<>(), false)));

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
            QValue p = scope.get(parent.like());
            if (p.v instanceof ContainerType) {
                parent = inheritContainer((ContainerType) p.v, parent);
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
            QValue func = runtime.scope.get(handler);
            if (!(func.v instanceof FuncType))
                throw new RuntimeStriker("call event:event handler can only be a function");
            QValue result = ((FuncType) func.v).run(runtime, Collections.singletonList(new QValue(metadata)));
            if (QValue.isNull(result) || (QValue.isBool(result) && !((BoolType) result.v).value))
                break;
        }
    }

    public QValue run(Node node, Memory scope) throws RuntimeStriker {
        if (node == null) current.codePos = 0;
        else current.codePos = node.codePos;
        try {
            if (node instanceof BinaryOperatorNode) {
                QValue a = QValue.nullSafe(run(((BinaryOperatorNode) node).lnode, scope));
                QValue b = QValue.nullSafe(run(((BinaryOperatorNode) node).rnode, scope));
                QType av = a.v;
                QType bv = b.v;
                // String containerImpl = Utilities.transformOp(((BinaryOperatorNode) node).operator.c);
                String containerImpl = Utilities.opToString.get(((BinaryOperatorNode) node).operator.c);
                if (containerImpl != null && av.table != null &&
                        av.table.containsKey(containerImpl) && av.table.get(containerImpl).v instanceof FuncType) {
                    List<QValue> metaArgs = new ArrayList<>(Arrays.asList(a, b));
                    return ((FuncType) av.table.get(containerImpl).v).run(this, metaArgs);
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
                        if (QValue.isNum(a, b)) {
                            assert av instanceof NumType;
                            double avx = ((NumType) av).value;
                            double bvx = ((NumType) bv).value;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "+":
                                    return new QValue(new NumType(avx + bvx));
                                case "-":
                                    return new QValue(new NumType(avx - bvx));
                                case "*":
                                    return new QValue(new NumType(avx * bvx));
                                case "/":
                                    return new QValue(new NumType(avx / bvx));
                                case ">":
                                    return new QValue(new BoolType(avx > bvx));
                                case "<":
                                    return new QValue(new BoolType(avx < bvx));
                                case "<=":
                                    return new QValue(new BoolType(avx >= bvx));
                                case ">=":
                                    return new QValue(new BoolType(avx <= bvx));
                                case "//":
                                    return new QValue(new NumType(Math.floor(avx / bvx)));
                            }
                        } else if (QValue.isNum(b) && QValue.isStr(a)) {
                            assert av instanceof StringType;
                            String avx = ((StringType) av).value;
                            double bvx = ((NumType) bv).value;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "*":
                                    return new QValue(new StringType(StringUtils.mult(avx, bvx)));
                                case "/":
                                    return new QValue(ListUtils.newListType(StringUtils.div(avx, bvx)));
                                case ">":
                                    return new QValue(new BoolType(avx.length() > bvx));
                                case "<":
                                    return new QValue(new BoolType(avx.length() < bvx));
                                case "<=":
                                    return new QValue(new BoolType(avx.length() <= bvx));
                                case ">=":
                                    return new QValue(new BoolType(avx.length() >= bvx));
                                case "//":
                                    return new QValue(ListUtils.newListType(StringUtils.mod(avx, bvx)));
                            }
                        } else if (QValue.isNum(b) && QValue.isList(a)) {
                            assert av instanceof ListType;
                            List<QValue> avx = ((ListType) av).values;
                            double bvx = ((NumType) bv).value;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "*":
                                    return new QValue(new ListType(ListUtils.mult(avx, bvx)));
                                case "/":
                                    return new QValue(new ListType(ListUtils.div(avx, bvx)));
                                case ">":
                                    return new QValue(new BoolType(avx.size() > bvx));
                                case "<":
                                    return new QValue(new BoolType(avx.size() < bvx));
                                case "<=":
                                    return new QValue(new BoolType(avx.size() <= bvx));
                                case ">=":
                                    return new QValue(new BoolType(avx.size() >= bvx));
                                case "//":
                                    return new QValue(new ListType(ListUtils.mod(avx, bvx)));
                            }
                        } else if (QValue.isList(a, b)) {
                            assert av instanceof ListType;
                            List<QValue> avx = ((ListType) av).values;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "+":
                                    return new QValue(ListUtils.concat((ListType) av, (ListType) bv));
                                case "-":
                                    return new QValue(new ListType(ListUtils.removeAll(avx, ((ListType) bv).values)));
                            }
                        } else if (QValue.isStr(a, b)) {
                            assert av instanceof StringType;
                            String avx = ((StringType) av).value;
                            String bvx = ((StringType) bv).value;
                            switch (((BinaryOperatorNode) node).operator.c) {
                                case "+":
                                    return new QValue(new StringType(avx + bvx));
                                case "-":
                                    return new QValue(new StringType(avx.replaceAll(bvx, "")));
                            }
                        }
                        break;
                    }
                    case "==":
                    case "is": {
                        BoolType c = Utilities.compare(a, b);
                        if (c != null)
                            return new QValue(c);
                        break;
                    }
                    case "!=": {
                        BoolType c = Utilities.compare(a, b);
                        if (c != null)
                            return new QValue(new BoolType(!c.value));
                        else return new QValue(false);
                    }
                    case "^": {
                        if (QValue.isNum(a, b))
                            return new QValue(
                                    new NumType(Math.pow(((NumType) av).value, ((NumType) bv).value)));
                        break;
                    }
                    case "%": {
                        if (QValue.isNum(a, b))
                            return new QValue(
                                    new NumType(((NumType) av).value % ((NumType) bv).value));
                        break;
                    }
                    case "and": {
                        if (QValue.isBool(a, b))
                            return new QValue(
                                    new BoolType(((BoolType) av).value && ((BoolType) bv).value));
                        break;
                    }
                    case "or": {
                        if (QValue.isBool(a, b))
                            return new QValue(
                                    new BoolType(((BoolType) av).value || ((BoolType) bv).value));
                        else if (av instanceof VoidType && bv instanceof VoidType)
                            return Void;
                        else if (av instanceof VoidType)
                            return b;
                        else if (bv instanceof VoidType)
                            return a;
                        else
                            return a;
                    }
                    case "is type of":
                    case "instanceof": {
                        if (av instanceof VoidType)
                            return new QValue(new BoolType(false));
                        if (QValue.isStr(b)) {
                            String bvx = ((StringType) bv).value;
                            if ((QValue.isNum(a) && bvx.equals("num")) ||
                                    (QValue.isBool(a) && bvx.equals("bool")) ||

                                    (QValue.isStr(a) && bvx.equals("string")) ||
                                    (QValue.isList(a) && bvx.equals("list")) ||
                                    (QValue.isCont(a) && bvx.equals("container")) ||
                                    (QValue.isFunc(a) && bvx.equals("function")) ||
                                    (QValue.isJava(a) && bvx.equals("javatype")))
                                return new QValue(new BoolType(true));
                            if (QValue.isCont(a) && (((ContainerType) av).name().equals(bvx) ||
                                    ((ContainerType) av).like().equals(bvx)))
                                return new QValue(new BoolType(true));
                        }
                        break;
                    }
                    case "is same type as": {
                        if (QValue.isNum(a, b) ||
                                QValue.isList(a, b) ||
                                QValue.isCont(a, b) ||
                                QValue.isStr(a, b) ||
                                QValue.isBool(a, b) ||
                                QValue.isJava(a, b) ||
                                QValue.isFunc(a, b))
                            return new QValue(new BoolType(true));
                        break;
                    }
                    case "=": {
                        Node lnode = ((BinaryOperatorNode) node).lnode;
                        if (!(lnode instanceof VariableNode))
                            throw new RuntimeStriker("run:binaryop:set:cannot place value to non-variable type",
                                    current.codePos);
                        mainRecord.action(new AssignTraceRecord(lnode, run(lnode, scope), b));
                        scope.set(((VariableNode) lnode).token.c, b.copy());
                        return Void;
                    }
                    case "<-": {
                        if (!(a.v instanceof RefType))
                            throw new RuntimeStriker("run:binaryop:set:cannot place value by ref. with non-ref",
                                    current.codePos);
                        ((RefType) a.v).object.v = b.v;
                        return Void;
                    }
                    case ":":
                    case ":+": {
                        Assert.require(QValue.isNum(a, b), "run:binaryop:range:expected numbers", current.codePos);
                        ListType l = new ListType();
                        double avx = ((NumType) av).value;
                        double bvx = ((BinaryOperatorNode) node).operator.c.equals(":+")?
                                ((NumType) bv).value : ((NumType) bv).value - 1;
                        int step = avx < bvx ? 1 : -1;
                        double iterator = avx;
                        while (true) {
                            if (avx < bvx) if (iterator > bvx) break;
                            if (avx > bvx) if (iterator < bvx) break;
                            l.values.add(new QValue(iterator));
                            iterator += step;
                        }
                        return new QValue(l);
                    }
                    case "step": {
                        Assert.require(((BinaryOperatorNode) node).lnode instanceof BinaryOperatorNode,
                                "run:binaryop:step:expected range as lvalue", current.codePos);
                        Assert.require(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).operator.c.equals(":"),
                                "run:binaryop:step:expected range as lvalue", current.codePos);
                        QValue rangeStart = run(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).lnode, scope);
                        QValue rangeEnd = run(((BinaryOperatorNode) ((BinaryOperatorNode) node).lnode).rnode, scope);
                        QValue rangeStep = run(((BinaryOperatorNode) node).rnode, scope);
                        Assert.require(QValue.isNum(rangeStart, rangeEnd, rangeStep),
                                "run:binaryop:step:expected numbers", current.codePos);
                        ((NumType) rangeEnd.v).value = ((BinaryOperatorNode) node).operator.c.equals(":+")?
                                ((NumType) rangeEnd.v).value : ((NumType) rangeEnd.v).value - 1;
                        double iter = ((NumType) rangeStart.v).value;
                        boolean canRun = true;
                        ListType l = new ListType();
                        while (canRun) {
                            l.values.add(new QValue(iter));
                            iter += ((NumType) rangeStep.v).value;
                            if (((NumType) rangeStart.v).value < ((NumType) rangeEnd.v).value) {
                                if (((NumType) rangeEnd.v).value < iter)
                                    canRun = false;
                            } else {
                                if (((NumType) rangeEnd.v).value > iter)
                                    canRun = false;
                            }
                        }
                        return new QValue(l);
                    }
                    case "in": {
                        if (QValue.isStr(a, b)) {
                            return new QValue(new BoolType(
                                    ((StringType) av).value.contains(((StringType) bv).value)));
                        } else if (QValue.isList(b)) {
                            for (int i = 0; i < ((ListType) bv).values.size(); i++)
                                if (Utilities.compare(a, ((ListType) bv).values.get(i)).value)
                                    return new QValue(new BoolType(true));
                            return new QValue(new BoolType(false));
                        } else throw new RuntimeStriker("run:binaryop:in:b should be list or string");
                    }
                }
                throw new RuntimeStriker("run:binaryop:no valid case for " + node + " (" +
                        a + ", " + b + ")", current.codePos);

            } else if (node instanceof BlockNode) {
                for (Node n : ((BlockNode) node).nodes)
                    run(n, scope);

            } else if (node instanceof EveryBlockNode) {
                QValue range = run(((EveryBlockNode) node).expr, scope);
                List<QValue> iterable = new ArrayList<>();
                if (QValue.isStr(range))
                    for (char c : ((StringType) range.v).value.toCharArray())
                        iterable.add(new QValue(c + ""));
                else if (range.v instanceof ListType) iterable = ((ListType) range.v).values;
                else throw new RuntimeStriker("run:every loop:invalid range " + range +
                            ". Only list and string are supported");
                int doRethrow = 0;
                String var = ((EveryBlockNode) node).variable.token.c;
                for (QValue q : iterable) {
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
                return run(((FieldReferenceNode) node).lnode, scope).v.table.get(
                        ((VariableNode) ((FieldReferenceNode) node).rnode).token.c);

            } else if (node instanceof FieldSetNode) {
                QValue parent = run(((FieldSetNode) node).lnode, scope);
                if (!(((FieldSetNode) node).rnode instanceof VariableNode))
                    throw new RuntimeStriker("run:field set:cannot set value of non-variable type" + node,
                            current.codePos);
                parent.v.table.put(((VariableNode) ((FieldSetNode) node).rnode).token.c,
                        run(((FieldSetNode) node).value, scope));

            } else if (node instanceof FunctionCallNode) {
                QValue callee = run(((FunctionCallNode) node).id, scope);
                List<QValue> args = new ArrayList<>();
                for (Node a : Parser.multiElementIfNeeded(((FunctionCallNode) node).args).nodes)
                    args.add(run(a, scope));
                if (callee == null) throw new RuntimeStriker("run:call:cannot call null " + node);
                if (!(QValue.isCont(callee) || QValue.isFunc(callee)) &&
                    !QValue.isFunc(callee.nullSafeGet("_call"))) throw new RuntimeStriker(
                            "run:call:cannot call " + callee + " in " + node
                    );
                if (QValue.isCont(callee)) { // If constructor
                    QValue prototype = callee.copy();
                    if (QValue.isFunc(prototype.nullSafeGet("_builder"))) {
                        args.add(0, prototype);
                        ((FuncType) prototype.nullSafeGet("_builder").v).run(this, args);
                    }
                    return prototype;
                } else if (((FunctionCallNode) node).id instanceof FieldReferenceNode) {
                    QValue parent = run(((FieldReferenceNode) ((FunctionCallNode) node).id).lnode, scope);
                    if (QValue.isFunc(callee.nullSafeGet("_call"))) {
                        args.add(0, parent);
                        return ((FuncType) callee.nullSafeGet("_call").v).run(this, args);
                    }
                    if (!(QValue.isCont(parent) &&
                        !((ContainerType) parent.v).isMeta() &&
                        !ContainerType.tableToClone.containsKey(
                           ((FieldReferenceNode) ((FunctionCallNode) node).id).rnode.toString()))) {
                        args.add(0, parent);
                    }
                    return ((FuncType) parent.nullSafeGet(
                            ((FieldReferenceNode) ((FunctionCallNode) node).id).rnode.toString()).v).run(
                            this, args);
                } else return ((FuncType) callee.v).run(this, args);

            } else if (node instanceof EffectNode) {
                switch (((EffectNode) node).operator.c) {
                    case "assert": {
                        QValue v = run(((EffectNode) node).operand, scope);
                        if (v.v instanceof VoidType || (v.v instanceof BoolType && !((BoolType) v.v).value))
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
                        QValue loaded;
                        if (Runtime.nativeLibNames.contains(lib))
                            loaded = new QValue(getNative(lib));
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
                            QType.forEachNotBuiltIn(loaded.v, (k, v) -> {
                                if (!scope.hasParentalDefinition(k))
                                    scope.set(k, v);
                            });
                        } else {
                            if (((EffectNode) node).other.equals("_defaultname"))
                                scope.set(id, loaded);
                            else scope.set(((EffectNode) node).other, loaded);
                        }
                        if (loaded.nullSafeGet("_events").v instanceof ListType) {
                            for (QValue q : ((ListType) loaded.nullSafeGet("_events").v).values) {
                                if (q.v instanceof ContainerType) {
                                    Assert.require(q.nullSafeGet("consumer").v instanceof FuncType &&
                                            q.nullSafeGet("event").v instanceof StringType,
                                            "run:use:handler migrating is defined, but handler "
                                                    + q + " is invalid");
                                    String event = ((StringType) q.nullSafeGet("event").v).value;
                                    int i = 0;
                                    while (!(scope.get("_eventhandler_migrated_" + event + "_" + i).v
                                            instanceof VoidType)) i++;
                                    ((FuncType) q.v.table.get("consumer").v).name = "_eventhandler_migrated_" +
                                            event + "_" + i;
                                    scope.set(((FuncType) q.v.table.get("consumer").v).name, q.v.table.get("consumer"));
                                    if (eventHandlers.containsKey(event)) {
                                        List<String> e = eventHandlers.get(event);
                                        e.add(((FuncType) q.nullSafeGet("consumer").v).name);
                                        eventHandlers.put(event, e);
                                    }
                                    else eventHandlers.put(event, new ArrayList<>(Collections.singletonList(
                                            ((FuncType) q.nullSafeGet("consumer").v).name)));
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
                        QValue v = run(((EffectNode) node).operand, scope);
                        Assert.require(QValue.isNum(v), "run:effect:strike:specify a num value");
                        throw new RuntimeStriker(RuntimeStrikerType.BREAK, ((NumType) v.v).value);
                    }
                }

            } else if (node instanceof IfBlockNode) {
                QValue condition = run(((IfBlockNode) node).condition, scope);
                if (condition.v instanceof BoolType && ((BoolType) condition.v).value) {
                    run(((IfBlockNode) node).nodes, scope);
                } else {
                    for (Node linked : ((IfBlockNode) node).linkedNodes) {
                        if (linked instanceof ElseBlockNode) {
                            run(((IfBlockNode) node).nodes, scope);
                            break;
                        } else if (linked instanceof ElseIfBlockNode) {
                            QValue elseIfCondition = run(((ElseIfBlockNode) linked).condition, scope);
                            if (elseIfCondition.v instanceof BoolType && ((BoolType) elseIfCondition.v).value) {
                                run(((ElseIfBlockNode) linked).nodes, scope);
                                break;
                            }
                        }
                    }
                }

            } else if (node instanceof IndexReferenceNode) {
                QValue parent = run(((IndexReferenceNode) node).lnode, scope);
                QValue index = run(((IndexReferenceNode) node).rnode, scope);
                if (parent.v instanceof ListType) {
                    Assert.require(QValue.isNum(index), "run:index:list:index should be number");
                    Assert.require(((ListType) parent.v).values.size() >
                            (int) Math.round(((NumType) index.v).value), "run:index:list:out of bounds");
                    return ((ListType) parent.v).values.get((int) Math.round(((NumType) index.v).value));
                } else if (parent.nullSafeGet("_index").v instanceof FuncType) {
                    return ((FuncType) parent.nullSafeGet("_index").v).run(this,
                            Arrays.asList(parent, index));
                } else if (parent.v instanceof ContainerType) {
                    return parent.nullSafeGet(index.toString());
                } else throw new RuntimeStriker("run:index:" + ((IndexReferenceNode) node).lnode +
                        " is not indexable", current.codePos);

            } else if (node instanceof IndexSetNode) {
                QValue parent = run(((IndexSetNode) node).lnode, scope);
                QValue index = run(((IndexSetNode) node).rnode, scope);
                QValue value = run(((IndexSetNode) node).value, scope);
                if (parent.v instanceof ListType) {
                    Assert.require(QType.isNum(index.v), "run:index:list:index should be number");
                    Assert.require(((ListType) parent.v).values.size() >
                            (int) Math.round(((NumType) index.v).value), "run:index:list:out of bounds");
                    ((ListType) parent.v).values.set((int) Math.round(((NumType) index.v).value), value);
                    return Void;
                } else if (parent.nullSafeGet("_setindex").v instanceof FuncType) {
                    return ((FuncType) parent.nullSafeGet("_setindex").v).run(this,
                            Arrays.asList(parent, index, value));
                } else if (parent.v instanceof ContainerType) {
                    return parent.v.table.put(index.toString(), value);
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
                return new QValue(((LiteralBoolNode) node).token.c.equals("true"));

            } else if (node instanceof LiteralContainerNode) {
                ContainerType container = new ContainerType(
                        ((LiteralContainerNode) node).name,
                        ((LiteralContainerNode) node).alike,
                        new HashMap<>(),
                        ((LiteralContainerNode) node).isMeta
                );
                QValue parent = scope.get(((LiteralContainerNode) node).alike);
                if (!container.like().equals("container")) {
                    if (!(parent.v instanceof ContainerType))
                        throw new RuntimeStriker("run:container" + ((LiteralContainerNode) node).name +
                                ":Inheritance error, cannot inherit from a non-container type", current.codePos);
                    container = inheritContainer((ContainerType) parent.v, container);
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
                        container.table.put(((LiteralFunctionNode) n).name.c, new QValue(f));
                    }
                }
                scope.set(((LiteralContainerNode) node).name, new QValue(container));
                return new QValue(container);

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
                while (!(scope.get(f.name + id).v instanceof VoidType)) id++;
                f.name += id;
                scope.set(f.name, new QValue(f));
                if (eventHandlers.containsKey(event)) {
                    List<String> e = eventHandlers.get(event);
                    e.add(f.name);
                    eventHandlers.put(event, e);
                }
                else eventHandlers.put(event, new ArrayList<>(Collections.singletonList(f.name)));
                return new QValue();

            } else if (node instanceof LiteralFunctionNode) {
                List<VariableNode> args = new ArrayList<>();
                Parser.multiElementIfNeeded(((LiteralFunctionNode) node).args).nodes.forEach(
                        (v) -> args.add((VariableNode) v)
                );
                FuncType f = new FuncType(((LiteralFunctionNode) node).name.c,
                        args, ((LiteralFunctionNode) node).code, ((LiteralFunctionNode) node).isStatic);
                scope.set(((LiteralFunctionNode) node).name.c, new QValue(f));
                return new QValue(f);

            } else if (node instanceof LiteralListNode) {
                ListType list = new ListType();
                for (Node n : ((LiteralListNode) node).nodes)
                    list.values.add(run(n, scope));
                return new QValue(list);

            } else if (node instanceof LiteralNullNode) {
                return new QValue();

            } else if (node instanceof LiteralNumNode) {
                return new QValue(Double.parseDouble(((LiteralNumNode) node).token.c));

            } else if (node instanceof LiteralStringNode) {
                return new QValue(((LiteralStringNode) node).token.c);

            } else if (node instanceof LoopStopBlockNode) {
                int doRethrow = 0;
                while (true) {
                    try {
                        run(((LoopStopBlockNode) node).nodes, scope);
                        QType condition = run(((LoopStopBlockNode) node).condition, scope).v;
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
                return new QValue(list);

            } else if (node instanceof ThroughBlockNode) {
                QType baseV;
                QType ceilV;
                QType stepV = null;
                BinaryOperatorNode range = ((ThroughBlockNode) node).range;
                if (range.operator.c.equals("step")) {
                    stepV = run(range.rnode, scope).v;
                    baseV = run(((BinaryOperatorNode) range.lnode).lnode, scope).v;
                    ceilV = run(((BinaryOperatorNode) range.lnode).rnode, scope).v;
                } else {
                    baseV = run(range.lnode, scope).v;
                    ceilV = run(range.rnode, scope).v;
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
                    scope.set(var, new QValue(iterator));
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
                        QType v = run(((UnaryOperatorNode) node).operand, scope).v;
                        Assert.require(QType.isBool(v),
                                "run:unary:!:Non-bool operand", current.codePos);
                        return new QValue(!((BoolType) v).value);
                    }
                    case "negate":
                    case "-": {
                        QType v = run(((UnaryOperatorNode) node).operand, scope).v;
                        Assert.require(QType.isNum(v),
                                "run:unary:-:Non-num operand", current.codePos);
                        return new QValue(-((NumType) v).value);
                    }
                    case "notnull":
                    case "exists": {
                        QType v = run(((UnaryOperatorNode) node).operand, scope).v;
                        return new QValue(!(v instanceof VoidType));
                    }
                    case "&": {
                        return new QValue(new RefType(run(((UnaryOperatorNode) node).operand, scope)));
                    }
                    case "*": {
                        QType v = run(((UnaryOperatorNode) node).operand, scope).v;
                        Assert.require(v instanceof RefType,
                                "run:unary:*:cannot dereference non-ref. value " + v);
                        return ((RefType) v).object;
                    }
                }

            } else if (node instanceof VariableNode) {
                return scope.get(((VariableNode) node).token.c);

            } else if (node instanceof WhileBlockNode) {
                int doRethrow = 0;
                while (true) {
                    try {
                        QType condition = run(((WhileBlockNode) node).condition, scope).v;
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
