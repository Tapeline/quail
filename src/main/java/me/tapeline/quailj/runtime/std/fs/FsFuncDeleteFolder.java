package me.tapeline.quailj.runtime.std.fs;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPDecoderStream;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class FsFuncDeleteFolder extends QBuiltinFunc {

    public FsFuncDeleteFolder(Runtime runtime) {
        super(
                "deleteFolder",
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
        try {
            FileUtils.deleteDirectory(new File(args.get("path").toString()));
            return QObject.Val(true);
        } catch (IOException e) {
            return QObject.Val(false);
        }
    }

}
