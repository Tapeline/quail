package me.tapeline.quailj.runtime.builtins.library_storage;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.StringWriter;
import java.util.*;

public class StorageFuncSaveyml extends FuncType {

    public StorageFuncSaveyml() {
        super("saveyml", Arrays.asList("path", "data"), null);
    }

    private Object save(QType q) {
        if (q instanceof ContainerType) {
            Map<String, Object> m = new LinkedHashMap<>();
            QType stored = q.table.get("_storage_stored");
            List<String> whatToSave = new ArrayList<>();
            if (stored instanceof ListType)
                for (QType qs : ((ListType) stored).values)
                    whatToSave.add(qs.toString());
            q.table.forEach((k, v) -> {
                if (whatToSave.size() == 0)
                    m.put(k, save(v));
                else if (whatToSave.contains(k))
                    m.put(k, save(v));
            });
            return m;
        } else if (q instanceof ListType) {
            List<Object> l = new ArrayList<>();
            ((ListType) q).values.forEach(v -> {
                l.add(save(v));
            });
            return l;
        } else if (q instanceof StringType) {
            return ((StringType) q).value;
        } else if (q instanceof NumType) {
            return ((NumType) q).value;
        } else if (q instanceof BoolType) {
            return ((BoolType) q).value;
        } else return null;
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "storage saveyml:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "storage saveyml:invalid arg0 type");
        Assert.require(a.get(1) instanceof ContainerType, "storage saveyml:invalid arg1 type");
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(save(a.get(1)), writer);
        IOManager.fileSet(((StringType) a.get(0)).value, writer.toString());
        return new VoidType();
    }

    @Override
    public QType copy() {
        return new StorageFuncSaveyml();
    }
}
