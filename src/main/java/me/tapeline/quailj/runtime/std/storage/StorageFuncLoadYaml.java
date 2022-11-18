package me.tapeline.quailj.runtime.std.storage;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.platforms.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class StorageFuncLoadYaml extends QBuiltinFunc {

    public StorageFuncLoadYaml(Runtime runtime) {
        super(
                "loadYaml",
                Arrays.asList(
                        new FuncArgument(
                                "path",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
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
        if (!IOManager.fileExists(args.get("path").toString()))
            Runtime.error("Failed to load YAML storage: " + args.get("path") + " does not exist");
        String yamlContents = IOManager.fileInput(args.get("path").toString());
        Yaml yaml = new Yaml();
        Object loadedObject = yaml.load(yamlContents);
        return represent(runtime, loadedObject);
    }

    public static QObject represent(Runtime runtime, Object obj) throws RuntimeStriker {
        if (obj instanceof String) {
            return QObject.Val((String) obj);
        } else if (obj instanceof Integer) {
            return QObject.Val((Integer) obj);
        } else if (obj instanceof Long) {
            return QObject.Val((Long) obj);
        } else if (obj instanceof Byte) {
            return QObject.Val((Byte) obj);
        } else if (obj instanceof Short) {
            return QObject.Val((Short) obj);
        } else if (obj instanceof Float) {
            return QObject.Val((Float) obj);
        } else if (obj instanceof Double) {
            return QObject.Val((Double) obj);
        } else if (obj instanceof Boolean) {
            return QObject.Val((Boolean) obj);
        } else if (obj instanceof List<?>) {
            List<QObject> list = new ArrayList<>();
            for (Object listObj : ((List<?>) obj))
                list.add(represent(runtime, listObj));
            return QObject.Val(list);
        } else if (obj instanceof Map<?, ?>) {
            QObject map = QObject.Val(new HashMap<>());
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet())
                map.set(entry.getKey().toString(), represent(runtime, entry.getValue()));
            return map;
        } else if (obj instanceof QFunc) {
            return (QFunc) obj;
        }
        Runtime.error("Invalid object in yaml " + obj.toString());
        return null;
    }

}
