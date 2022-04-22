package me.tapeline.quailj.runtime.builtins.library_storage;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class StorageFuncLoadyml extends FuncType {

    public StorageFuncLoadyml() {
        super("loadyml", Arrays.asList("path"), null);
    }

    private QType load(Object o) {
        if (o instanceof Integer)
            return new NumType(((Integer) o));
        else if (o instanceof Float)
            return new NumType(((Float) o));
        else if (o instanceof Double)
            return new NumType(((Double) o));
        else if (o instanceof String)
            return new StringType(((String) o));
        else if (o instanceof List) {
            ListType l = new ListType();
            for (Object obj : ((List<?>) o))
                l.values.add(load(obj));
            return l;
        } else if (o instanceof Map) {
            HashMap<String, QType> m = new HashMap<>();
            for (String s : ((Map<String, QType>) o).keySet()) {
                m.put(s, load(((Map<?, ?>) o).get(s)));
            }
            return new ContainerType(m);
        }
        return new VoidType();
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "storage loadyml:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "storage loadyml:invalid arg0 type");
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(IOManager.fileInput(((StringType) a.get(0)).value));
        return load(data);
    }

    @Override
    public QType copy() {
        return new StorageFuncLoadyml();
    }
}
