package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.types.RuntimeStrikerType;
import me.tapeline.quailj.utils.Assert;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FuncConsole extends FuncType {

    public FuncConsole() {
        super("console", Collections.singletonList("code"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() > 0, "func console:invalid args size");
        try {
            Process p = java.lang.Runtime.getRuntime().exec(a.get(0).toString());
            return QType.V(p.waitFor());
        } catch (IOException | InterruptedException ignored) { }
        return QType.V();
    }

    @Override
    public QType copy() {
        return new FuncConsole();
    }
}
