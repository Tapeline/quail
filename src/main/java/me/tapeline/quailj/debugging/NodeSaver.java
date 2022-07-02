package me.tapeline.quailj.debugging;

import me.tapeline.quailj.parser.nodes.*;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.VariableTable;

public class NodeSaver {

    public static String save(Node n) {
        String s = "{\"type\": \"" + n.getClass().getSimpleName() + "\", " +
                "\"pos\": " + n.codePos + ", " +
                "\"startTime\": " + n.executionStart + ", \"elapsedTime\": " + n.executionTime + ", ";
        if (n instanceof BinaryOperatorNode) {
            s += "\"left\": " + save(((BinaryOperatorNode) n).lnode) +
                    ", \"right\": " + save(((BinaryOperatorNode) n).rnode) +
                    ", \"operator\": \"" + ((BinaryOperatorNode) n).operator.c + "\"";
        } else if (n instanceof BlockNode) {
            s += "\"nodes\": [";
            if (((BlockNode) n).nodes.size() == 0) s += "]";
            else for (int i = 0; i < ((BlockNode) n).nodes.size(); i++)
                if (i + 1 == ((BlockNode) n).nodes.size()) s += save(((BlockNode) n).nodes.get(i)) + "]";
                else s += save(((BlockNode) n).nodes.get(i)) + ", ";
        } else if (n instanceof EffectNode) {
            s += "\"token\": \"" + ((EffectNode) n).operator.c + "\", \"operand\": " + save(((EffectNode) n).operand);
        } else if (n instanceof ElseBlockNode) {
            s += "\"code\": " + save(((ElseBlockNode) n).nodes);
        } else if (n instanceof ElseIfBlockNode) {
            s += "\"condition\": " + save(((ElseIfBlockNode) n).condition) + "\", \"code\": " +
                    save(((ElseIfBlockNode) n).nodes);
        } else if (n instanceof EventNode) {
            s += "\"event\": " + save(((EventNode) n).event) + ", \"code\": " + save(((EventNode) n).code);
        } else if (n instanceof EveryBlockNode) {
            s += "\"expr\": " + save(((EveryBlockNode) n).expr) +
                    ", \"var\": " + save(((EveryBlockNode) n).variable) +
                    ", \"code\": " + save(((EveryBlockNode) n).nodes);
        } else if (n instanceof FieldReferenceNode) {
            s += "\"left\": " + save(((FieldReferenceNode) n).lnode) +
                    ", \"right\": " + save(((FieldReferenceNode) n).rnode);
        } else if (n instanceof FieldSetNode) {
            s += "\"left\": " + save(((FieldSetNode) n).lnode) +
                    ", \"right\": " + save(((FieldSetNode) n).rnode) +
                    ", \"value\": " + save(((FieldSetNode) n).value);
        } else if (n instanceof FunctionCallNode) {
            s += "\"args\": " + save(((FunctionCallNode) n).args) +
                    ", \"id\": " + save(((FunctionCallNode) n).id);
        } else if (n instanceof IfBlockNode) {
            s += "\"condition\": " + save(((IfBlockNode) n).condition) + "\", \"code\": " +
                    save(((IfBlockNode) n).nodes);
        } else if (n instanceof IndexReferenceNode) {
            s += "\"id\": " + save(((IndexReferenceNode) n).lnode) +
                    ", \"index\": " + save(((IndexReferenceNode) n).rnode);
        } else if (n instanceof IndexSetNode) {
            s += "\"id\": " + save(((IndexSetNode) n).lnode) +
                    ", \"index\": " + save(((IndexSetNode) n).rnode) +
                    ", \"value\": " + save(((IndexSetNode) n).value);
        } else if (n instanceof InstructionNode) {
            s += "\"token\": \"" + ((InstructionNode) n).token.c + "\"";
        } else if (n instanceof LiteralBoolNode) {
            s += "\"value\": \"" + ((LiteralBoolNode) n).token + "\n";
        } else if (n instanceof LiteralContainerNode) {
            s += "\"name\": \"" + ((LiteralContainerNode) n).name + "\", " +
                    "\"alike\": \"" + ((LiteralContainerNode) n).alike + "\", " +
                    "\"isMeta\": \"" + ((LiteralContainerNode) n).isMeta + "\", " +
                    "\"contents\": [";
            if (((LiteralContainerNode) n).initialize.size() == 0) s += "]";
            else for (int i = 0; i < ((LiteralContainerNode) n).initialize.size(); i++)
                if (i + 1 == ((LiteralContainerNode) n).initialize.size())
                    s += save(((LiteralContainerNode) n).initialize.get(i)) + "]";
                else
                    s += save(((LiteralContainerNode) n).initialize.get(i)) + ", ";
        } else if (n instanceof LiteralFunctionNode) {
            s += "\"name\": \"" + ((LiteralFunctionNode) n).name + "\", " +
                    "\"args\": " + save(((LiteralFunctionNode) n).args) + ", " +
                    "\"isStatic\": \"" + ((LiteralFunctionNode) n).isStatic + "\", " +
                    "\"code\": " + save(((LiteralFunctionNode) n).code);
        } else if (n instanceof LiteralListNode) {
            s += "\"contents\": [";
            if (((LiteralListNode) n).nodes.size() == 0) s += "]";
            else for (int i = 0; i < ((LiteralListNode) n).nodes.size(); i++)
                if (i + 1 == ((LiteralListNode) n).nodes.size())
                    s += save(((LiteralListNode) n).nodes.get(i)) + "]";
                else
                    s += save(((LiteralListNode) n).nodes.get(i)) + ", ";
        } else if (n instanceof LiteralNullNode) {
            s += "\"token\": \"null\"";
        } else if (n instanceof LiteralNumNode) {
            s += "\"token\": \"" + ((LiteralNumNode) n).token + "\"";
        } else if (n instanceof LiteralStringNode) {
            s += "\"token\": \"" + ((LiteralStringNode) n).token + "\"";
        } else if (n instanceof MultiElementNode) {
            s += "\"contents\": [";
            if (((MultiElementNode) n).nodes.size() == 0) s += "]";
            else for (int i = 0; i < ((MultiElementNode) n).nodes.size(); i++)
                if (i + 1 == ((MultiElementNode) n).nodes.size())
                    s += save(((MultiElementNode) n).nodes.get(i)) + "]";
                else
                    s += save(((MultiElementNode) n).nodes.get(i)) + ", ";
        } else if (n instanceof ThroughBlockNode) {
            s += "\"expr\": " + save(((ThroughBlockNode) n).range) + ", " +
                    "\"var\": " + save(((ThroughBlockNode) n).variable) + ", " +
                    "\"code\": " + save(((ThroughBlockNode) n).nodes);
        } else if (n instanceof TryCatchBlockNode) {
            s += "\"var\": " + save(((TryCatchBlockNode) n).variable) + ", " +
                    "\"tryCode\": " + save(((TryCatchBlockNode) n).tryNodes) + ", " +
                    "\"catchCode\": " + save(((TryCatchBlockNode) n).catchNodes);
        } else if (n instanceof UnaryOperatorNode) {
            s += "\"token\": \"" + ((UnaryOperatorNode) n).operator.c +
                    "\", \"operand\": " + save(((UnaryOperatorNode) n).operand);
        } else if (n instanceof VariableNode) {
            s += "\"token\": \"" + n + "\"";
        } else if (n instanceof WhileBlockNode) {
            s += "\"condition\": " + save(((WhileBlockNode) n).condition) + "\", \"code\": " +
                    save(((WhileBlockNode) n).nodes);
        } else {
            return "{}";
        }
        return (s.endsWith(", ")? s.substring(0, s.length() - 2) : s) + "}";
    }

    public static String saveMemoryActions() {
        String s = "";
        for (int i = 0; i < VariableTable.assigns.size(); i++) {
            AssignTraceRecord a = VariableTable.assigns.get(i);
            s += "{\"scope\": \"" + a.scope         + "\", " +
                    "\"var\": \"" + a.subject       + "\", " +
                    "\"old\": \"" + a.previousState + "\", " +
                    "\"new\": \"" + a.object        + "\"}";
            if (i + 1 < VariableTable.assigns.size()) s += ", ";
        }
        return "[" + s + "]";
    }

}
