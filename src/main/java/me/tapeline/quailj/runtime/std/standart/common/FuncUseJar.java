package me.tapeline.quailj.runtime.std.standart.common;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.libmanagement.Embed;
import me.tapeline.quailj.libmanagement.EmbedLoader;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FuncUseJar extends QBuiltinFunc {

    public FuncUseJar(Runtime runtime) {
        super(
                "useJar",
                Arrays.asList(
                        new FuncArgument(
                                "path",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "classPath",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "args",
                                new ArrayList<>(),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        Embed embed = null;
        try {
            embed = EmbedLoader.loadFromJar(
                    new File(args.get("path").toString()),
                    args.get("classPath").toString()
            );
        } catch (Exception e) {
            Runtime.error("Caught an exception while trying to load " + args.get("path") + "\n" +
                    "Exception details:\n" + e.toString());
            return QObject.Val();
        }
        if (embed == null) {
            Runtime.error("Embed load failed (probably no matching embed classes)");
            return QObject.Val();
        } else {
            try {
                return runtime.embedIntegrator.integrateEmbed(
                        embed,
                        !args.containsKey("args") ||args.get("args").isNull()?
                                null : args.get("args").getTable().getValues()
                );
            } catch (Exception e) {
                Runtime.error("Caught an exception while trying to integrate " + args.get("path") + "\n" +
                        "Exception details:\n" + e.toString());
                return QObject.Val();
            }
        }
    }

}
