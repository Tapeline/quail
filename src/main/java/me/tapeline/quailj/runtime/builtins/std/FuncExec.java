package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.parser.Parser;
import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class FuncExec extends FuncType {

    public FuncExec() {
        super("exec", Collections.singletonList("code"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.require(a.size() > 0, "func exec:invalid args size");
        Lexer lexer = new Lexer(a.get(0).toString());
        Parser parser = new Parser(lexer.lexAndFix());
        Node node = parser.parse();
        try {
            return runtime.run(node, runtime.scope);
        } catch (RuntimeStriker striker) {
            if (striker.type.equals(RuntimeStrikerType.RETURN)) return striker.val;
            if (striker.type.equals(RuntimeStrikerType.EXCEPTION)) throw striker;
        }
        return new QValue();
    }

    @Override
    public QType copy() {
        return new FuncExec();
    }
}
