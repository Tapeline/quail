package me.tapeline.quailj.runtime.std.qml.typography;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QMLTypographyFuncMetrics extends QBuiltinFunc {

    public QMLTypographyFuncMetrics(Runtime runtime) {
        super(
                "metrics",
                Arrays.asList(
                        new FuncArgument(
                                "font",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "str",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
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
        if (!(args.get("font") instanceof QJavaAdapter) ||
                !(((QJavaAdapter<?>) args.get("font")).object instanceof Font))
            Runtime.error("Not a font");
        List<QObject> l = new ArrayList<>();
        Font font = ((Font) ((QJavaAdapter<?>) args.get("font")).object);
        String text = args.get("str").toString();
        AffineTransform transform = new AffineTransform();
        FontRenderContext context = new FontRenderContext(transform, true, true);
        int w = (int) font.getStringBounds(text, context).getWidth();
        int h = (int) font.getStringBounds(text, context).getHeight();
        l.add(QObject.Val(w));
        l.add(QObject.Val(h));
        return QObject.Val(l);
    }

}
