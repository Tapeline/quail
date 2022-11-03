package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.utils.VariableTable;

import java.util.List;

public class QList extends QObject {

    public static VariableTable defaults = new VariableTable();
    private int iter = 0;

    public List<QObject> values;

    public QList(List<QObject> values) {
        setObjectMetadata("List");
        table.putAll(defaults);
        this.values = values;
    }

    public String toString() {
        return values.toString();
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
}
