package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.*;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.modifiers.VariableModifier;
import me.tapeline.quailj.typing.utils.ContainerPreRuntimeContents;
import me.tapeline.quailj.typing.utils.VariableTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QObject {
    
    public static VariableTable defaults = new VariableTable();

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
    
    protected VariableTable table;

    public QObject() {}

    public QObject(VariableTable content) {
        this.table = new VariableTable();
        this.table.putAll(defaults);
        this.table.putAll(content);
        setObjectMetadata("Object");
    }

    public QObject(HashMap<String, QObject> content) {
        this.table = new VariableTable();
        this.table.putAll(defaults);
        this.table.putAll(content);
        setObjectMetadata("Object");
    }

    public QObject(String name, QObject like, VariableTable content) {
        this.table = new VariableTable();
        this.table.putAll(defaults);
        this.table.putAll(content);
        setObjectMetadata(name, like);
    }

    public QObject(String name, QObject like, HashMap<String, QObject> content) {
        this.table = new VariableTable();
        this.table.putAll(defaults);
        this.table.putAll(content);
        setObjectMetadata(name, like);
    }

    public void setObjectMetadata(String className) {
        this.table.put("_name", Val(className));
    }

    public void setObjectMetadata(String className, QObject superClass) {
        this.table.put("_name", Val(className));
        this.table.put("super", superClass);
    }

    public void setPrototypeFlag(boolean isPrototype) {
        this.table.put("_isPrototype", Val(isPrototype));
    }

    public String getClassName() {
        return get("_name").toString();
    }

    public QObject getSuper() {
        return table.get("super");
    }

    public boolean isPrototype() {
        return get("_isPrototype").toString().equals("true");
    }

    public boolean isNum() {
        return this instanceof QNumber;
    }

    public boolean isBool() {
        return this instanceof QBool;
    }

    public boolean isNull() {
        return this instanceof QNull;
    }

    public boolean isStr() {
        return this instanceof QString;
    }

    public boolean isList() {
        return this instanceof QList;
    }

    public boolean isFunc() {
        return this instanceof QFunc;
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

    public QObject get(String id) {
        return nullSafe(table.get(id));
    }

    public void set(Runtime runtime, String id, QObject value) throws RuntimeStriker {
        table.put(runtime, id, value);
    }

    public void set(String id, QObject value, List<VariableModifier> modifiers) {
        table.put(id, value, modifiers);
    }

    public void set(String id, QObject value) {
        table.put(id, value);
    }

    public boolean isTrue() {
        return (this instanceof QBool && ((QBool) this).value) || !(this instanceof QNull);
    }

    public boolean instanceOf(QObject parent) {
        if (getClassName().equals(parent.getClassName()))
            return true;
        else if (getSuper() != null)
            return false;
        else
            return getSuper().instanceOf(parent);
    }

    public VariableTable getTable() {
        return table;
    }

    public QObject callFromThis(Runtime runtime, String funcId,
                                List<QObject> args, HashMap<String, QObject> kwargs)
                                        throws RuntimeStriker {
        if (!isPrototype())
            args.add(0, this);
        return get(funcId).call(runtime, args, kwargs);
    }

    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " + " + other.getClassName());
        return Val();
    }

    public QObject subtract(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " - " + other.getClassName());
        return Val();
    }

    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " * " + other.getClassName());
        return Val();
    }

    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " / " + other.getClassName());
        return Val();
    }

    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " // " + other.getClassName());
        return Val();
    }

    public QObject modulo(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " % " + other.getClassName());
        return Val();
    }

    public QObject power(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " ^ " + other.getClassName());
        return Val();
    }

    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " << " + other.getClassName());
        return Val();
    }

    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " >> " + other.getClassName());
        return Val();
    }

    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " == " + other.getClassName());
        return Val();
    }

    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " != " + other.getClassName());
        return Val();
    }

    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " > " + other.getClassName());
        return Val();
    }

    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " >= " + other.getClassName());
        return Val();
    }

    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " < " + other.getClassName());
        return Val();
    }

    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " <= " + other.getClassName());
        return Val();
    }

    public QObject not(Runtime runtime) throws RuntimeStriker {
        runtime.error("Unsupported operation ! on " + getClassName());
        return Val();
    }

    public QObject minus(Runtime runtime) throws RuntimeStriker {
        runtime.error("Unsupported operation - on " + getClassName());
        return Val();
    }

    public QObject typeString(Runtime runtime) throws RuntimeStriker {
        runtime.error("Unsupported typecast string <- " + getClassName());
        return Val();
    }

    public QObject typeBool(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported typecast bool <- " + getClassName());
        return Val();
    }

    public QObject typeNumber(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported typecast num <- " + getClassName());
        return Val();
    }

    public QObject and(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " && " + other.getClassName());
        return Val();
    }

    public QObject or(Runtime runtime, QObject other) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " || " + other.getClassName());
        return Val();
    }

    public QObject index(Runtime runtime, QObject index) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " [" + index.getClassName() + "]");
        return Val();
    }

    public QObject indexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        runtime.error("Unsupported operation between " +
                getClassName() + " [" + index.getClassName() + "] = " + value.getClassName());
        return Val();
    }

    public QObject subscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        runtime.error(getClassName() + " is not subscriptable");
        return Val();
    }

    public QObject subscriptStartEndStep(Runtime runtime, QObject start, QObject end, QObject step)
            throws RuntimeStriker {
        runtime.error(getClassName() + " is not subscriptable with step");
        return Val();
    }

    public QObject call(Runtime runtime, List<QObject> arguments,
                        HashMap<String, QObject> kwargs) throws RuntimeStriker {
        runtime.error(getClassName() + " is not callable");
        return Val();
    }

    public QObject iterateNext(Runtime runtime) throws RuntimeStriker {
        // TODO: container iteration
        runtime.error(getClassName() + " is not iterable");
        return Val();
    }

    public QObject copy(Runtime runtime) throws RuntimeStriker {
        QObject copy = new QObject(getClassName(), getSuper(), new VariableTable());
        copy.getTable().putAll(table);
        copy.setPrototypeFlag(isPrototype());
        return copy;
    }

}
