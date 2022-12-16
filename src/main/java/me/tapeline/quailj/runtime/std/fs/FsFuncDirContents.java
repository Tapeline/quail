package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.io.File;
import java.util.*;

public class FsFuncDirContents extends QBuiltinFunc {

    public FsFuncDirContents(Runtime runtime) {
        super(
                "exists",
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
        String[] contents = new File(args.get("path").toString()).list();
        if (contents == null)
            return QObject.Val(new ArrayList<>());
        else {
            List<QObject> files = new ArrayList<>();
            for (String fileName : contents) files.add(QObject.Val(fileName));
            return QObject.Val(files);
        }
    }

}
