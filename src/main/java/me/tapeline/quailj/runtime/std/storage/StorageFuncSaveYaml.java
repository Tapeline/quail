package me.tapeline.quailj.runtime.std.storage;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.platforms.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.*;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class StorageFuncSaveYaml extends QBuiltinFunc {

    public StorageFuncSaveYaml(Runtime runtime) {
        super(
                "saveYaml",
                Arrays.asList(
                        new FuncArgument(
                                "path",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "obj",
                                new ArrayList<>(),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        Yaml yaml = new Yaml();
        IOManager.fileSet(args.get("path").toString(), yaml.dump(represent(runtime, args.get("obj"))));
        return QObject.Val();
    }

    public static Object represent(Runtime runtime, QObject obj) {
        if (obj instanceof QString) {
            return obj.toString();
        } else if (obj instanceof QNumber) {
            return obj.numValue();
        } else if (obj instanceof QBool) {
            return obj.isTrue();
        } else if (obj instanceof QList) {
            List<Object> list = new ArrayList<>();
            for (QObject listObj : obj.listValue())
                list.add(represent(runtime, listObj));
            return list;
        } else if (obj instanceof QFunc) {
            return obj;
        } else {
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, QObject> entry : obj.getNonDefaultFields().entrySet())
                map.put(entry.getKey(), represent(runtime, entry.getValue()));
            return map;
        }
    }

}
