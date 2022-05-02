package me.tapeline.quailj.debugging;

import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QValue;
import me.tapeline.quailj.utils.StringUtils;

public class AssignTraceRecord extends TracedAction {

    public String subject;
    public String previousState;
    public String object;

    public AssignTraceRecord(Node subj, QValue old, QValue val) {
        subject = subj.toString();
        previousState = old != null? old.toString() : "java null";
        object = val != null? val.toString() : "java null";
    }

    @Override
    public String toString() {
        return subject + " (At the moment: " + previousState + ") = " + object;
    }

    public String toString(int tab) {
        return StringUtils.mult(" ", tab) + subject + " (At the moment: " + previousState + ") = " + object;
    }

}
