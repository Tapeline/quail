package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.utils.VariableTable;

import java.util.regex.Pattern;

public class QString extends QObject {

    public static VariableTable defaults = new VariableTable();

    public String value;

    public QString(String value) {
        table.putAll(defaults);
        setObjectMetadata("String");
        this.value = value;
    }

    public QObject sum(Runtime runtime, QObject other) {
        return QObject.Val(value + other.toString());
    }

    public QObject subtract(Runtime runtime, QObject other) {
        return QObject.Val(value.replaceAll(Pattern.quote(other.toString()), ""));
    }

    @Override
    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.multiply(runtime, other);
    }

    @Override
    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.divide(runtime, other);
    }

    @Override
    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.notEqualsObject(runtime, other);
    }

    @Override
    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.greater(runtime, other);
    }

    @Override
    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.less(runtime, other);
    }

    @Override
    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject typeString(Runtime runtime) throws RuntimeStriker {
        return super.typeString(runtime);
    }

    @Override
    public QObject typeBool(Runtime runtime) throws RuntimeStriker {
        return super.typeBool(runtime);
    }

    @Override
    public QObject typeNumber(Runtime runtime) throws RuntimeStriker {
        try {
            return QObject.Val(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            runtime.error("Not a number: " + value);
        }
        return super.typeNumber(runtime);
    }

    @Override
    public QObject index(Runtime runtime, QObject index) throws RuntimeStriker {
        return super.index(runtime, index);
    }

    @Override
    public QObject indexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        return super.indexSet(runtime, index, value);
    }

    @Override
    public QObject subscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        return super.subscriptStartEnd(runtime, start, end);
    }

    @Override
    public QObject subscriptStartEndStep(Runtime runtime, QObject start, QObject end, QObject step) throws RuntimeStriker {
        return super.subscriptStartEndStep(runtime, start, end, step);
    }

    public String toString() {
        return value;
    }


}
