package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;

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

    public boolean matches(Runtime r, Object object) {
        return true;
    }

    public String toString() {
        return "type";
    }
}
