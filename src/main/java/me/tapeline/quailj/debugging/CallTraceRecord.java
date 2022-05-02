package me.tapeline.quailj.debugging;

import me.tapeline.quailj.parser.nodes.FunctionCallNode;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QValue;
import me.tapeline.quailj.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CallTraceRecord extends TracedAction {

    public String code;
    public List<String> args = new ArrayList<>();
    public List<TracedAction> innerActions = new ArrayList<>();

    public CallTraceRecord(FunctionCallNode node, List<QValue> casted) {
        casted.forEach((v) -> args.add(v.toString()));
        code = node.toString();
    }

    public CallTraceRecord() {
        code = "Runtime (main call)";
    }

    public void action(TracedAction a) {
        this.innerActions.add(a);
    }

    @Override
    public String toString() {
        StringBuilder ia = new StringBuilder(" {\n");
        for (TracedAction a : innerActions)
            ia.append(a.toString()).append("\n");
        return "Call for " + code + " w/args: " + args.toString() +
                (innerActions.size() > 0?
                        ia + "}"
                : "");
    }

    @Override
    public String toString(int tab) {
        StringBuilder ia = new StringBuilder(" {\n");
        for (TracedAction a : innerActions)
            ia.append(a.toString(tab + 2)).append("\n");
        return StringUtils.mult(" ", tab) + "Call for " + code + " w/args: " + args.toString() +
                (innerActions.size() > 0?
                        ia + StringUtils.mult(" ", tab) + "}"
                        : "");
    }

}
