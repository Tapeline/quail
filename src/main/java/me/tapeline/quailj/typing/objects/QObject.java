package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.*;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.modifiers.VariableModifier;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.utils.ContainerPreRuntimeContents;
import me.tapeline.quailj.typing.utils.VariableTable;
import me.tapeline.quailj.utils.Utilities;

import java.util.*;

public class QObject {

    public static QObject getDefaultFor(List<VariableModifier> modifiers) {
        for (VariableModifier m : modifiers) {
            if (m instanceof TypeModifier) {
                if (((TypeModifier) m).possibleType == TokenType.TYPE_BOOL)
                    return QObject.Val(false);
                else if (((TypeModifier) m).possibleType == TokenType.TYPE_LIST)
                    return QObject.Val(new ArrayList<>());
                else if (((TypeModifier) m).possibleType == TokenType.TYPE_NUM)
                    return QObject.Val(0);
                else if (((TypeModifier) m).possibleType == TokenType.TYPE_STRING)
                    return QObject.Val("");
                else if (((TypeModifier) m).possibleType == TokenType.TYPE_CONTAINER)
                    return QObject.Val(new HashMap<>());
            }
        }
        return QObject.Val();
    }

    public static Node getDefaultNodeFor(List<VariableModifier> modifiers) {
        for (VariableModifier m : modifiers) {
            if (m instanceof TypeModifier) {
                if (((TypeModifier) m).possibleType == TokenType.TYPE_BOOL)
                    return new LiteralBool(Token.UNDEFINED, false);
                else if (((TypeModifier) m).possibleType == TokenType.TYPE_LIST)
                    return new LiteralList(Token.UNDEFINED, new ArrayList<>());
                else if (((TypeModifier) m).possibleType == TokenType.TYPE_NUM)
                    return new LiteralNum(Token.UNDEFINED, 0);
                else if (((TypeModifier) m).possibleType == TokenType.TYPE_STRING)
                    return new LiteralString(Token.UNDEFINED, "");
                else if (((TypeModifier) m).possibleType == TokenType.TYPE_CONTAINER)
                    return new LiteralContainer(Token.UNDEFINED,
                            new ContainerPreRuntimeContents(new ArrayList<>(), new HashMap<>()));
            }
        }
        return new LiteralNull(Token.UNDEFINED);
    }
    
    public VariableTable table = new VariableTable();
    protected String className;
    protected QObject superClass;
    protected QObject prototype;
    protected boolean isPrototype;
    private Set<String> iter;

    public Set<QObject> derivedObjects = new HashSet<>();
    public Set<QObject> childPrototypes = new HashSet<>();
    public boolean isDict = false;
    public boolean isInheritable = true;

    public QObject() {}

    public QObject(VariableTable content) {
        this.table = new VariableTable();
        if (Runtime.superObject != null)
            table.putAll(Runtime.superObject.table);
        this.table.putAll(content);
        setObjectMetadata("Object");
        if (this != Runtime.superObject)
            Runtime.superObject.derivedObjects.add(this);
    }

    public QObject(HashMap<String, QObject> content) {
        this.table = new VariableTable();
        if (Runtime.superObject != null)
            table.putAll(Runtime.superObject.table);
        this.table.putAll(content);
        setObjectMetadata("Object");
        if (this != Runtime.superObject)
            Runtime.superObject.derivedObjects.add(this);
    }

    public QObject(String name, QObject like, VariableTable content) throws RuntimeStriker {
        this.table = new VariableTable();
        if (Runtime.superObject != null)
            table.putAll(Runtime.superObject.table);
        this.table.putAll(content);
        setObjectMetadata(name, like);
        if (this != Runtime.superObject)
            Runtime.superObject.derivedObjects.add(this);
    }

    public QObject(String name, QObject like, HashMap<String, QObject> content) {
        this.table = new VariableTable();
        if (Runtime.superObject != null)
            table.putAll(Runtime.superObject.table);
        this.table.putAll(content);
        try {
            setObjectMetadata(name, like);
        } catch (RuntimeStriker ignored) {}
        if (this != Runtime.superObject)
            Runtime.superObject.derivedObjects.add(this);
    }

    public final HashMap<String, QObject> getNonDefaultFields() {
        HashMap<String, QObject> fields = new HashMap<>();
        for (Map.Entry<String, QObject> entry : table.getValues().entrySet())
            if (prototype != null && !prototype.table.getValues().containsKey(entry.getKey()))
                fields.put(entry.getKey(), entry.getValue());
        return fields;
    }

    public static QObject constructSuperObject() {
        QObject superObject = new QObject();
        superObject.table = new VariableTable();
        try {
            superObject.setObjectMetadata("Object", null);
        } catch (RuntimeStriker ignored) {}
        return superObject;
    }

    public final void updateDerivations() {
        for (QObject derived : derivedObjects)
            for (Map.Entry<String, QObject> entry : table.getValues().entrySet()) {
                derived.table.getValues().putIfAbsent(entry.getKey(), entry.getValue());
                derived.table.modifiers.putIfAbsent(entry.getKey(), table.getModifiersFor(entry.getKey()));
            }
    }

    public final void updateInheritanceChain() {
        for (QObject child : childPrototypes) {
            for (Map.Entry<String, QObject> entry : table.getValues().entrySet()) {
                child.table.getValues().putIfAbsent(entry.getKey(), entry.getValue());
                child.table.modifiers.putIfAbsent(entry.getKey(), table.getModifiersFor(entry.getKey()));
            }
            child.updateInheritanceChain();
        }
    }

    public final void updateInheritanceAndDerivations() {
        updateDerivations();
        for (QObject child : childPrototypes) {
            for (Map.Entry<String, QObject> entry : table.getValues().entrySet()) {
                child.table.getValues().putIfAbsent(entry.getKey(), entry.getValue());
                child.table.modifiers.putIfAbsent(entry.getKey(), table.getModifiersFor(entry.getKey()));
            }
            child.updateInheritanceChain();
            child.updateDerivations();
        }
    }

    public final void registerInheritance(QObject child) throws RuntimeStriker {
        if (isInheritable)
            childPrototypes.add(child);
        else
            Runtime.error("Cannot inherit from " + className);
    }

    public final void setSuperClass(QObject superClass) throws RuntimeStriker  {
        this.superClass = superClass;
        if (superClass != null)
            superClass.registerInheritance(this);
    }

    public final void setSafeSuperClass(QObject superClass)  {
        try {
            setSuperClass(superClass);
        } catch (RuntimeStriker ignored) {}
    }

    public final void setObjectMetadata(String className) {
        this.className = className;
        //this.table.put("_name", new QString(className));
    }

    public final void setObjectMetadata(String className, QObject superClass) throws RuntimeStriker {
        this.className = className;
        this.superClass = superClass;
        if (superClass != null)
            superClass.registerInheritance(this);
        /*this.table.put("_name", new QString(className));
        this.table.put("super", superClass);*/
    }

    public final void setObjectMetadata(QObject klass) {
        this.prototype = klass;
        this.className = klass.getClassName();
        //this.table.put("_name", new QString(className));
    }

    public final void setObjectMetadata(QObject klass, QObject superClass) throws RuntimeStriker {
        this.prototype = klass;
        this.className = klass.getClassName();
        this.superClass = superClass;
        if (superClass != null)
            superClass.registerInheritance(klass);
        /*this.table.put("_name", new QString(className));
        this.table.put("super", superClass);*/
    }

    public final void setPrototypeFlag(boolean isPrototype) {
        this.isPrototype = isPrototype;
        //this.table.put("_isPrototype", new QBool(isPrototype));
    }

    public final String getClassName() {
        if (!isPrototype() && prototype != null)
            return prototype.getClassName();
        else
            return className;
        //return className;
        //return get("_name").toString();
    }

    public final QObject getPrototype() {
        return prototype;
    }

    public final QObject getSuper() {
        return superClass;
        //return table.get("super");
    }

    public final boolean isPrototype() {
        return isPrototype;
        //return get("_isPrototype").toString().equals("true");
    }

    public final boolean isNum() {
        return this instanceof QNumber;
    }

    public final boolean isBool() {
        return this instanceof QBool;
    }

    public final boolean isNull() {
        return this instanceof QNull;
    }

    public final boolean isStr() {
        return this instanceof QString;
    }

    public final boolean isList() {
        return this instanceof QList;
    }

    public final boolean isFunc() {
        return this instanceof QFunc;
    }

    public final double numValue() {
        if (this instanceof QNumber)
            return ((QNumber) this).value;
        return 0;
    }

    public final boolean boolValue() {
        if (this instanceof QBool)
            return ((QBool) this).value;
        return false;
    }

    public final List<QObject> listValue() {
        if (this instanceof QList)
            return ((QList) this).values;
        return new ArrayList<>();
    }

    public static QObject Val(double d) {
        return new QNumber(d);
    }

    public static QObject Val(boolean d) {
        return new QBool(d);
    }

    public static QObject Val(String d) {
        return new QString(d);
    }

    public static QObject Val(List<QObject> d) {
        return new QList(d);
    }

    public static QObject Val(HashMap<String, QObject> d) {
        return new QObject(d);
    }

    public static QObject Val() {
        return new QNull();
    }

    public static QObject nullSafe(QObject obj) {
        return obj == null? Val() : obj;
    }

    public final QObject get(String id) {
        if (id.equals("_name"))
            return QObject.Val(className);
        else if (id.equals("_super"))
            return nullSafe(superClass);
        else if (id.equals("_prototype"))
            return QObject.Val(isPrototype);
        return nullSafe(table.get(id));
    }

    public final void set(Runtime runtime, String id, QObject value) throws RuntimeStriker {
        table.put(runtime, id, value);
    }

    public final QObject getOverridable(Runtime runtime, String id) throws RuntimeStriker {
        if (table.containsKey("_get"))
            return callFromThis(runtime, "_get", Arrays.asList(QObject.Val(id)));
        if (table.containsKey("_get_" + id))
            return callFromThis(runtime, "_get_" + id, new ArrayList<>());
        return get(id);
    }

    public final void setOverridable(Runtime runtime, String id, QObject value) throws RuntimeStriker {
        if (table.containsKey("_set"))
            callFromThis(runtime, "_set", Utilities.asList(QObject.Val(id), value));
        else if (table.containsKey("_set_" + id))
            callFromThis(runtime, "_set_" + id, Utilities.asList(value));
        else set(runtime, id, value);
    }

    public final void set(String id, QObject value, List<VariableModifier> modifiers) {
        table.put(id, value, modifiers);
    }

    public final void set(String id, QObject value) {
        table.put(id, value);
    }

    public final boolean isTrue() {
        if (this instanceof QBool) {
            return ((QBool) this).value;
        } else return !(this instanceof QNull);
    }

    public final boolean instanceOf(QObject parent) {
        // If parent == superObject -> true
        if (parent == Runtime.superObject) return true;

        // if parent == prototype -> true
        if (parent == prototype || parent == this) return true;

        // if super.instanceof parent -> true
        if (prototype != null && prototype.superClass != null && prototype.superClass.instanceOf(parent))
            return true;

        if (superClass != null && superClass.instanceOf(parent))
            return true;

        // -> false
        return false;
    }

    public final VariableTable getTable() {
        return table;
    }

    public final QObject callFromThis(Runtime runtime, String funcId, List<QObject> args) throws RuntimeStriker {
        if (!isPrototype())
            args.add(0, this);
        return get(funcId).call(runtime, args);
    }

    public final QObject callFromThis(Runtime runtime, QObject func, List<QObject> args) throws RuntimeStriker {
        if (!isPrototype())
            args.add(0, this);
        return func.call(runtime, args);
    }

    public final QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype())
            Runtime.error("Attempt to derive from non-prototype value");
        QObject newObject = Val(new HashMap<>());
        newObject.table.putAll(table);
        newObject.setObjectMetadata(this, getSuper());
        return newObject;
    }

    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_add"))
            return callFromThis(
                    runtime,
                    "_add",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " + " + other.getClassName());
        return Val();
    }

    public QObject subtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_sub"))
            return callFromThis(
                    runtime,
                    "_sub",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " - " + other.getClassName());
        return Val();
    }

    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_mul"))
            return callFromThis(
                    runtime,
                    "_mul",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " * " + other.getClassName());
        return Val();
    }

    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_div"))
            return callFromThis(
                    runtime,
                    "_div",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " / " + other.getClassName());
        return Val();
    }

    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_intdiv"))
            return callFromThis(
                    runtime,
                    "_intdiv",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " // " + other.getClassName());
        return Val();
    }

    public QObject modulo(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_mod"))
            return callFromThis(
                    runtime,
                    "_mod",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " % " + other.getClassName());
        return Val();
    }

    public QObject power(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_pow"))
            return callFromThis(
                    runtime,
                    "_pow",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " ^ " + other.getClassName());
        return Val();
    }

    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_shl"))
            return callFromThis(
                    runtime,
                    "_shl",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " << " + other.getClassName());
        return Val();
    }

    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_shr"))
            return callFromThis(
                    runtime,
                    "_shr",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " >> " + other.getClassName());
        return Val();
    }

    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_eq"))
            return callFromThis(
                    runtime,
                    "_eq",
                    Arrays.asList(other)
            );
        return Val(table.getValues().equals(other.table.getValues()));
    }

    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_neq"))
            return callFromThis(
                    runtime,
                    "_neq",
                    Arrays.asList(other)
            );
        return Val(!table.getValues().equals(other.table.getValues()));
    }

    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_cmpg"))
            return callFromThis(
                    runtime,
                    "_cmpg",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " > " + other.getClassName());
        return Val();
    }

    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_cmpge"))
            return callFromThis(
                    runtime,
                    "_cmpge",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " >= " + other.getClassName());
        return Val();
    }

    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_cmpl"))
            return callFromThis(
                    runtime,
                    "_cmpl",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " < " + other.getClassName());
        return Val();
    }

    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_cmple"))
            return callFromThis(
                    runtime,
                    "_cmple",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " <= " + other.getClassName());
        return Val();
    }

    public QObject not(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_not"))
            return callFromThis(
                    runtime,
                    "_not",
                    new ArrayList<>()
            );
        Runtime.error("Unsupported operation ! on " + getClassName());
        return Val();
    }

    public QObject minus(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_neg"))
            return callFromThis(
                    runtime,
                    "_neg",
                    new ArrayList<>()
            );
        Runtime.error("Unsupported operation - on " + getClassName());
        return Val();
    }

    public QObject typeString(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_tostring"))
            return callFromThis(
                    runtime,
                    "_tostring",
                    new ArrayList<>()
            );
        Runtime.error("Unsupported typecast string <- " + getClassName());
        return Val();
    }

    public QObject typeBool(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_tobool"))
            return callFromThis(
                    runtime,
                    "_tobool",
                    new ArrayList<>()
            );
        Runtime.error("Unsupported typecast bool <- " + getClassName());
        return Val();
    }

    public QObject typeNumber(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_tonum"))
            return callFromThis(
                    runtime,
                    "_tonum",
                    new ArrayList<>()
            );
        Runtime.error("Unsupported typecast num <- " + getClassName());
        return Val();
    }

    public QObject and(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_and"))
            return callFromThis(
                    runtime,
                    "_and",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " && " + other.getClassName());
        return Val();
    }

    public QObject or(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_or"))
            return callFromThis(
                    runtime,
                    "_or",
                    Arrays.asList(other)
            );
        Runtime.error("Unsupported operation between " +
                getClassName() + " || " + other.getClassName());
        return Val();
    }

    public QObject index(Runtime runtime, QObject index) throws RuntimeStriker {
        if (table.containsKey("_index"))
            return callFromThis(
                    runtime,
                    "_index",
                    Arrays.asList(index)
            );
        return get(index.toString());
    }

    public QObject indexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        if (table.containsKey("_indexSet"))
            return callFromThis(
                    runtime,
                    "_indexSet",
                    Arrays.asList(index, value)
            );
        set(runtime, index.toString(), value);
        return Val();
    }

    public QObject subscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        if (table.containsKey("_subscriptStartEnd"))
            return callFromThis(
                    runtime,
                    "_subscriptStartEnd",
                    Arrays.asList(start, end)
            );
        Runtime.error(getClassName() + " is not subscriptable");
        return Val();
    }

    public QObject subscriptStartEndStep(Runtime runtime, QObject start, QObject end, QObject step)
            throws RuntimeStriker {
        if (table.containsKey("_subscriptStartEndStep"))
            return callFromThis(
                    runtime,
                    "_subscriptStartEndStep",
                    Arrays.asList(start, end, step)
            );
        Runtime.error(getClassName() + " is not subscriptable with step");
        return Val();
    }

    public QObject call(Runtime runtime, List<QObject> arguments) throws RuntimeStriker {
        if (isPrototype()) {
            QObject newObject = derive(runtime);
            return newObject.callFromThis(runtime, "_constructor", arguments);
        }
        if (table.containsKey("_call"))
            return callFromThis(
                    runtime,
                    "_call",
                    Arrays.asList(QObject.Val(arguments))
            );
        Runtime.error(getClassName() + " is not callable");
        // TODO: WTF
        return Val();
    }

    public QObject iterateStart(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_iterate"))
            return callFromThis(
                    runtime,
                    "_iterate",
                    new ArrayList<>()
            );
        iter = new HashSet<>();
        return Val();
    }

    public QObject iterateNext(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_next"))
            return callFromThis(
                    runtime,
                    "_next",
                    new ArrayList<>()
            );
        for (String key : table.keySet())
            if (!iter.contains(key)) {
                iter.add(key);
                return Val(Arrays.asList(
                        Val(key),
                        table.get(key)
                ));
            }
        if (iter.size() == table.keySet().size())
            throw new RuntimeStriker(RuntimeStriker.Type.STOP_ITERATION);
        return Val();
    }

    public QObject copy(Runtime runtime) throws RuntimeStriker {
        QObject copy = new QObject(getClassName(), getSuper(), new VariableTable());
        copy.getTable().putAll(table);
        copy.setPrototypeFlag(isPrototype());
        return copy;
    }

    public QObject clone(Runtime runtime) throws RuntimeStriker {
        QObject copy = new QObject(getClassName(), getSuper(), new VariableTable());
        table.forEach((k, v) -> {
            try {
                copy.getTable().put(
                        k,
                        v.clone(runtime),
                        table.getModifiersFor(k)
                );
            } catch (RuntimeStriker striker) {
                throw new RuntimeException(striker);
            }
        });
        copy.setPrototypeFlag(isPrototype());
        return copy;
    }

    public String toString() {
        return getClassName() + "@" + Integer.toHexString(hashCode());
    }

}
