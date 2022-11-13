package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

public class QNumber extends QObject {

    public double value;

    public QNumber(double value) {
        table.putAll(Runtime.superObject.table);
        table.putAll(Runtime.numberPrototype.table);
        Runtime.numberPrototype.derivedObjects.add(this);
        setObjectMetadata(Runtime.numberPrototype);
        this.value = value;
    }

    @Override
    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value + ((QNumber) other).value);
        return super.sum(runtime, other);
    }

    @Override
    public QObject subtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value - ((QNumber) other).value);
        return super.subtract(runtime, other);
    }

    @Override
    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value * ((QNumber) other).value);
        return super.multiply(runtime, other);
    }

    @Override
    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value / ((QNumber) other).value);
        return super.divide(runtime, other);
    }

    @Override
    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val((long) (value + ((QNumber) other).value));
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject modulo(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value % ((QNumber) other).value);
        return super.modulo(runtime, other);
    }

    @Override
    public QObject power(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(Math.pow(value, ((QNumber) other).value));
        return super.power(runtime, other);
    }

    @Override
    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value - ((QNumber) other).value);
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value + ((QNumber) other).value);
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value == ((QNumber) other).value);
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value != ((QNumber) other).value);
        return super.notEqualsObject(runtime, other);
    }

    @Override
    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value > ((QNumber) other).value);
        return super.greater(runtime, other);
    }

    @Override
    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value >= ((QNumber) other).value);
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value < ((QNumber) other).value);
        return super.less(runtime, other);
    }

    @Override
    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value <= ((QNumber) other).value);
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject minus(Runtime runtime) throws RuntimeStriker {
        return QObject.Val(-value);
    }

    @Override
    public QObject typeString(Runtime runtime) throws RuntimeStriker {
        return QObject.Val(toString());
    }

    @Override
    public QObject typeBool(Runtime runtime) throws RuntimeStriker {
        return QObject.Val(value != 0);
    }

    @Override
    public QObject typeNumber(Runtime runtime) throws RuntimeStriker {
        return this;
    }

    @Override
    public QObject copy(Runtime runtime) throws RuntimeStriker {
        QObject copy = QObject.Val(value);
        copy.getTable().putAll(table);
        return copy;
    }

    @Override
    public QObject clone(Runtime runtime) throws RuntimeStriker {
        QObject cloned = QObject.Val(value);
        table.forEach((k, v) -> {
            try {
                cloned.getTable().put(
                        k,
                        v.clone(runtime),
                        table.getModifiersFor(k)
                );
            } catch (RuntimeStriker striker) {
                throw new RuntimeException(striker);
            }
        });
        return cloned;
    }

    public String toString() {
        return value % 1 == 0? Long.toString(Math.round(value)) : Double.toString(value);
    }

}
