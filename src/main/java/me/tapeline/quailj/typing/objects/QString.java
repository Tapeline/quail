package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.utils.VariableTable;
import me.tapeline.quailj.utils.QStringUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

public class QString extends QObject {
    private int iterator;
    public String value;

    public QString(String value) {
        table.putAll(Runtime.superObject.table);
        table.putAll(Runtime.stringPrototype.table);
        Runtime.stringPrototype.derivedObjects.add(this);
        setObjectMetadata(Runtime.stringPrototype);
        this.value = value;
    }

    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        return Val(value + other.toString());
    }

    public QObject subtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.replaceAll(Pattern.quote(other.toString()), ""));
        return super.subtract(runtime, other);
    }

    @Override
    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(StringUtils.repeat(value, (int) other.numValue()));
        return super.multiply(runtime, other);
    }

    @Override
    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.divide(value, (int) other.numValue()));
        return super.divide(runtime, other);
    }

    @Override
    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.intDivide(value, (int) other.numValue()));
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.shift(value, (int) -other.numValue()));
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.shift(value, (int) other.numValue()));
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.equals(other.toString()));
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(!value.equals(other.toString()));
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() > other.toString().length());
        return super.greater(runtime, other);
    }

    @Override
    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() >= other.toString().length());
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() < other.toString().length());
        return super.less(runtime, other);
    }

    @Override
    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() <= other.toString().length());
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject typeString(Runtime runtime) throws RuntimeStriker {
        return this;
    }

    @Override
    public QObject typeBool(Runtime runtime) throws RuntimeStriker {
        if (value.equals("true"))
            return Val(true);
        else if (value.equals("false"))
            return Val(false);
        else runtime.error("Not a boolean: " + value);
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
        if (index.isNum())
            return Val("" + value.charAt((int) index.numValue()));
        return super.index(runtime, index);
    }

    @Override
    public QObject indexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        if (index.isNum()) {
            this.value =    this.value.substring(0, (int) index.numValue()) +
                            value.toString() +
                            this.value.substring((int) index.numValue() + 1);
            return Val();
        }
        return super.indexSet(runtime, index, value);
    }

    @Override
    public QObject subscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        if (start.isNum() && end.isNum())
            return Val(value.substring((int) start.numValue(), (int) end.numValue()));
        else if (start.isNum() && end.isNull())
            return Val(value.substring((int) start.numValue()));
        else if (start.isNull() && end.isNum())
            return Val(value.substring(0, (int) end.numValue()));
        return super.subscriptStartEnd(runtime, start, end);
    }

    @Override
    public QObject subscriptStartEndStep(Runtime runtime, QObject start, QObject end, QObject step)
            throws RuntimeStriker {
        return Val(QStringUtils.subscript(
                value,
                start.isNull()? null : (int) start.numValue(),
                end.isNull()? null : (int) end.numValue(),
                step.isNull()? null : (int) step.numValue()
        ));
    }

    @Override
    public QObject iterateStart(Runtime runtime) throws RuntimeStriker {
        iterator = 0;
        return Val();
    }

    @Override
    public QObject iterateNext(Runtime runtime) throws RuntimeStriker {
        if (iterator >= value.length())
            throw new RuntimeStriker(RuntimeStriker.Type.STOP_ITERATION);
        return Val("" + value.charAt(iterator++));
    }

    public String toString() {
        return value;
    }


}
