package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

public class TypeModifier extends VariableModifier {

    public Type type = Type.MOD_LOCAL;
    public TokenType possibleType;
    public Node possibleClass;

    public TypeModifier(Node possibleClass) {
        this.possibleType = TokenType.TYPE_OBJECT;
        this.possibleClass = possibleClass;
    }

    public TypeModifier(TokenType possibleType) {
        this.possibleType = possibleType;
        this.possibleClass = null;
    }

    public boolean matches(Runtime r, QObject object) {
        switch (possibleType) {
            case TYPE_NUM: return object.isNum();
            case TYPE_BOOL: return object.isBool();
            case TYPE_CONTAINER:
            case TYPE_OBJECT: return true;
            case TYPE_FUNCTION:
            case TYPE_FUNC: return object.isFunc();
            case TYPE_LIST: return object.isList();
            case TYPE_STRING: return object.isStr();
            case TYPE_VOID: return object.isNull();
        }
        return false;
    }

    public String toString() {
        switch (possibleType) {
            case TYPE_NUM: return "num";
            case TYPE_BOOL: return "bool";
            case TYPE_CONTAINER:
            case TYPE_OBJECT: return "object";
            case TYPE_FUNCTION:
            case TYPE_FUNC: return "func";
            case TYPE_LIST: return "list";
            case TYPE_STRING: return "string";
            case TYPE_VOID: return "void";
        }
        return "unknown type";
    }
}
