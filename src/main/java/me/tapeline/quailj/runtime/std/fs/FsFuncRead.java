package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.platforms.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class FsFuncRead extends QBuiltinFunc {

    public FsFuncRead(Runtime runtime) {
        super(
                "read",
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
        return QObject.Val(IOManager.fileInput(new File(args.get("path").toString()).getAbsolutePath()));
    }

}
