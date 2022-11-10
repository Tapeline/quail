package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.utils.VariableTable;
import me.tapeline.quailj.utils.QListUtils;
import me.tapeline.quailj.utils.QStringUtils;
import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class QList extends QObject {
    private int iter = 0;

    public List<QObject> values;

    public QList(List<QObject> values) {
        table.putAll(Runtime.superObject.table);
        table.putAll(Runtime.listPrototype.table);
        Runtime.listPrototype.derivedObjects.add(this);
        setObjectMetadata(Runtime.listPrototype);
        this.values = values;
    }

    public String toString() {
        return values.toString();
    }

    @Override
    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(ListUtils.union(values, other.listValue()));
        return super.sum(runtime, other);
    }

    @Override
    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.multiply(values, (int) other.numValue()));
        return super.multiply(runtime, other);
    }

    @Override
    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.divide(values, (int) other.numValue()));
        return super.divide(runtime, other);
    }

    @Override
    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.intDivide(values, (int) other.numValue()));
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.shift(values, (int) -other.numValue()));
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.shift(values, (int) other.numValue()));
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.equals(other.listValue()));
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(!values.equals(other.listValue()));
        return super.notEqualsObject(runtime, other);
    }

    @Override
    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() > other.listValue().size());
        return super.greater(runtime, other);
    }

    @Override
    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() >= other.listValue().size());
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() < other.listValue().size());
        return super.less(runtime, other);
    }

    @Override
    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() <= other.listValue().size());
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject index(Runtime runtime, QObject index) throws RuntimeStriker {
        if (index.isNum())
            return values.get((int) index.numValue());
        return super.index(runtime, index);
    }

    @Override
    public QObject indexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        if (index.isNum()) {
            values.set((int) index.numValue(), value);
            return Val();
        }
        return super.indexSet(runtime, index, value);
    }

    @Override
    public QObject subscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        if (start.isNum() && end.isNum())
            return Val(values.subList((int) start.numValue(), (int) end.numValue()));
        else if (start.isNum() && end.isNull())
            return Val(values.subList((int) start.numValue(), values.size()));
        else if (start.isNull() && end.isNum())
            return Val(values.subList(0, (int) end.numValue()));
        return super.subscriptStartEnd(runtime, start, end);
    }

    @Override
    public QObject subscriptStartEndStep(Runtime runtime, QObject start, QObject end, QObject step) throws RuntimeStriker {
        return Val(QListUtils.subscript(
                values,
                start.isNull()? null : (int) start.numValue(),
                end.isNull()? null : (int) end.numValue(),
                step.isNull()? null : (int) step.numValue()
        ));
    }

    @Override
    public QObject iterateStart(Runtime runtime) throws RuntimeStriker {
        this.iter = 0;
        return QObject.Val();
    }

    @Override
    public QObject iterateNext(Runtime runtime) throws RuntimeStriker {
        if (iter == values.size())
            throw new RuntimeStriker(RuntimeStriker.Type.STOP_ITERATION);
        return values.get(iter++);
    }

    @Override
    public QObject copy(Runtime runtime) {
        QObject copy = QObject.Val(values);
        copy.getTable().putAll(table);
        return copy;
    }

    @Override
    public QObject clone(Runtime runtime) {
        List<QObject> clonedValues = new ArrayList<>();
        int size = values.size();
        for (int i = 0; i < size; i++) clonedValues.add(values.get(i).clone(runtime));
        QObject cloned = QObject.Val(clonedValues);
        table.forEach((k, v) -> cloned.getTable().put(
                k,
                v.clone(runtime),
                table.getModifiersFor(k)
        ));
        return cloned;
    }

}
