package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

public class FsFuncDelete extends QBuiltinFunc {

    public FsFuncDelete(Runtime runtime) {
        super(
                "delete",
                Collections.singletonList(
                        new FuncArgument(
                                "path",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        File file = new File(args.get("path").toString());
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
                return QObject.Val(true);
            } catch (IOException e) {
                return QObject.Val(false);
            }
        } else return QObject.Val(file.delete());
    }

}
