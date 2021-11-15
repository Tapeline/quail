package me.tapeline.quarkj.parsingtools;

import me.tapeline.quarkj.parsingtools.nodes.*;
import me.tapeline.quarkj.tokenizetools.tokens.Token;
import me.tapeline.quarkj.tokenizetools.tokens.TokenType;

import java.util.Arrays;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private RootNode scope = new RootNode();
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void raiseException(String msg) {
        System.err.println("[QParser] (X) " + msg);
        System.exit(101);
    }

    public Token match(List<TokenType> tokenTypes) {
        if (pos < tokens.size()) {
            Token current = tokens.get(pos);
            if (tokenTypes.contains(current.t)) {
                pos++;
                return current;
            }
        }
        return null;
    }

    public Token getNext() {
        if (pos < tokens.size())
            return tokens.get(pos);
        return null;
    }

    public Token require(List<TokenType> tokenTypes) {
        Token token = match(tokenTypes);
        if (token == null) {
            raiseException("Expected one of `" + tokenTypes.toString() + "` types, but got `" +
                    getNext().toString() + "`");
        }
        return token;
    }

    public Node parseParentheses() {
        if (tokens.get(pos).t.equals(TokenType.LPAR) && tokens.get(pos+1).t.equals(TokenType.RPAR)) {
            pos += 2;
            return new MultiElementNode();
        }

        if (match(Arrays.asList(TokenType.LPAR)) != null) {
            Node node = parseFormula();
            if (getNext().t.equals(TokenType.COMMA)) {
                MultiElementNode multiElementNode = new MultiElementNode();
                multiElementNode.addNode(node);
                while (getNext().t.equals(TokenType.COMMA)) {
                    require(Arrays.asList(TokenType.COMMA));
                    multiElementNode.addNode(parseFormula());
                }
                require(Arrays.asList(TokenType.RPAR));
                return multiElementNode;
            } else {
                require(Arrays.asList(TokenType.RPAR));
                return node;
            }
        } else if (tokens.get(pos).t.equals(TokenType.LSPAR)) return parseSquaredParentheses();
        else return parseVarOrLiteral();
    }

    public Node parseFormula() {
        Node lNode = parseParentheses();
        Token op = match(Arrays.asList(TokenType.BINARYOPERATOR));
        while (op != null) {
            Node rNode = parseParentheses();
            if (op.c.equals("."))
                lNode = new FieldReferenceNode(op, lNode, rNode);
            else
                lNode = new BinaryOperatorNode(op, lNode, rNode);
            op = match(Arrays.asList(TokenType.BINARYOPERATOR));
        }
        return lNode;
    }

    public Node parseUnaryOperator() {
        Token token = match(Arrays.asList(TokenType.UNARYOPERATOR));
        if (token != null) return new UnaryOperatorNode(token, parseFormula());
        else {
            raiseException("Expected Unary Operator, but got null");
            return null;
        }
    }

    public Node parseVarOrLiteral() {
        Token string = match(Arrays.asList(TokenType.LITERALSTRING));
        if (string != null) return new LiteralStringNode(new Token(TokenType.LITERALSTRING,
                string.c.substring(1, string.c.length() - 1), string.p));
        Token num = match(Arrays.asList(TokenType.LITERALNUM));
        if (num != null) return new LiteralNumNode(num);
        Token bool = match(Arrays.asList(TokenType.LITERALBOOL));
        if (bool != null) return new LiteralBoolNode(bool);
        Token uo = match(Arrays.asList(TokenType.UNARYOPERATOR));
        if (uo != null) {
            pos--;
            return parseUnaryOperator();
        }
        Token var = match(Arrays.asList(TokenType.ID));
        if (var != null) {
            Node variable = new VariableNode(var);
            if (pos < tokens.size() && getNext().t.equals(TokenType.LPAR)) {
                pos++;
                return parseFunctionCall(variable);
            }
            return variable;
        }
        raiseException("Expected literal or variable contained value, but got " + getNext().toString());
        return null;
    }

    public Node parseIfBlock() {
        Node condraw = parseParentheses();
        BinaryOperatorNode condition = new BinaryOperatorNode(new Token(TokenType.BINARYOPERATOR, "==", 0),
                condraw, new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", 0)));
        BlockNode code = new BlockNode();
        Token t_ = require(Arrays.asList(TokenType.BLOCK));
        if (t_ == null || !t_.c.equals("do")) {
            raiseException("Expected do near if!");
            return null;
        }
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            code.addNode(node);
        }
        return new IfBlockNode(condition, code);
    }

    public Node parseElseIfBlock() {
        Node condraw = parseParentheses();
        BinaryOperatorNode condition = new BinaryOperatorNode(new Token(TokenType.BINARYOPERATOR, "==", 0),
                condraw, new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", 0)));
        BlockNode code = new BlockNode();
        Token t_ = require(Arrays.asList(TokenType.BLOCK));
        if (t_ == null || !t_.c.equals("do")) {
            raiseException("Expected do near `elseif`!");
            return null;
        }
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            code.addNode(node);
        }
        int ifBlockNodeIndex = scope.getLastIfNodeIndex();
        if (ifBlockNodeIndex > -1) {
            IfBlockNode ifBlockNode = (IfBlockNode) scope.nodes.get(ifBlockNodeIndex);
            ifBlockNode.linkNode(new ElseIfBlockNode(condition, code));
            scope.nodes.set(ifBlockNodeIndex, ifBlockNode);
        } else {
            raiseException("Expected `if` node before `elseif`.");
        }
        return null;
    }

    public Node parseElseBlock() {
        BlockNode code = new BlockNode();
        Token t_ = require(Arrays.asList(TokenType.BLOCK));
        if (t_ == null || !t_.c.equals("do")) {
            raiseException("Expected do near `else`!");
            return null;
        }
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            code.addNode(node);
        }
        int ifBlockNodeIndex = scope.getLastIfNodeIndex();
        if (ifBlockNodeIndex > -1) {
            IfBlockNode ifBlockNode = (IfBlockNode) scope.nodes.get(ifBlockNodeIndex);
            ifBlockNode.linkNode(new ElseBlockNode(code));
            scope.nodes.set(ifBlockNodeIndex, ifBlockNode);
        } else {
            raiseException("Expected `if` node before `elseif`.");
        }
        return null;
    }

    public Node parseThroughBlock() {
        BlockNode code = new BlockNode();
        Node rangeRaw = parseFormula();
        Token t_ = require(Arrays.asList(TokenType.KEYWORD));
        if (t_ == null || !t_.c.equals("as")) {
            raiseException("Expected `as` in `through` construction!");
            return null;
        }
        Node varRaw = parseVarOrLiteral();
        if (!(varRaw instanceof VariableNode)) {
            raiseException("`through` accepts only single variable (`VariableNode`), but not `"
                    + varRaw.getClass().toString() + "`");
            return null;
        }
        VariableNode variable = (VariableNode) varRaw;
        Token t__ = require(Arrays.asList(TokenType.BLOCK));
        if (t__ == null || !t__.c.equals("do")) {
            raiseException("Expected `do` at the end of `through` construction declaration!");
            return null;
        }
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            code.addNode(node);
        }
        return new ThroughBlockNode((BinaryOperatorNode) rangeRaw, variable, code);
    }

    public Node parseWhileBlock() {
        BlockNode code = new BlockNode();
        Node conditionRaw = parseParentheses();
        BinaryOperatorNode condition = new BinaryOperatorNode(new Token(TokenType.BINARYOPERATOR, "==", tokens.get(pos).p),
                conditionRaw, new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", tokens.get(pos).p)));
        Token t_ = require(Arrays.asList(TokenType.BLOCK));
        if (!t_.c.equals("do")) {
            raiseException("Expected `do` after `while` construction definition");
            return null;
        }
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            code.addNode(node);
        }
        return new WhileBlockNode(condition, code);
    }

    public Node parseLoopStopBlock() {
        BlockNode code = new BlockNode();
        while (true) {
            if (tokens.get(pos).t.equals(TokenType.KEYWORD) && tokens.get(pos).c.equals("stop when")) break;
            Node node = parseExpression();
            code.addNode(node);
        }
        require(Arrays.asList(TokenType.KEYWORD));
        Node conditionRaw = parseParentheses();
        BinaryOperatorNode condition = new BinaryOperatorNode(new Token(TokenType.BINARYOPERATOR, "==", tokens.get(pos).p),
                conditionRaw, new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", tokens.get(pos).p)));
        return new LoopStopBlockNode(condition, code);
    }

    public Node parseTryBlock() {
        BlockNode tryCode = new BlockNode();
        Token t___ = require(Arrays.asList(TokenType.BLOCK));
        if (t___ == null || !t___.c.equals("do")) {
            raiseException("Expected `do` at the end of `try` construction declaration!");
            return null;
        }
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            tryCode.addNode(node);
        }
        Token t_ = require(Arrays.asList(TokenType.KEYWORD));
        if (t_ == null || !t_.c.equals("catch")) {
            raiseException("Expected `catch` after `try` construction!");
            return null;
        }
        Token t__ = require(Arrays.asList(TokenType.KEYWORD));
        if (t__ == null || !t__.c.equals("as")) {
            raiseException("Expected `as` after exception definition in at `catch`!");
            return null;
        }
        Node varRaw = parseVarOrLiteral();
        if (!(varRaw instanceof VariableNode)) {
            raiseException("`catch` accepts only single variable (`VariableNode`), but not `"
                    + varRaw.getClass().toString() + "`");
            return null;
        }
        Token t____ = require(Arrays.asList(TokenType.BLOCK));
        if (t____ == null || !t____.c.equals("do")) {
            raiseException("Expected `do` at the end of `catch` construction declaration!");
            return null;
        }
        VariableNode variable = (VariableNode) varRaw;
        BlockNode catchCode = new BlockNode();
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            catchCode.addNode(node);
        }
        return new TryCatchNode(tryCode, catchCode, variable);
    }

    public Node parseFunctionCall(Node function) {
        pos--;
        Node arguments = parseParentheses();
        return new FunctionCallNode(((VariableNode) function).token, arguments);
    }

    public Node parseTypeStatement() {
        Token type = require(Arrays.asList(TokenType.TYPE));
        switch (type.c) {
            case "num":
            case "string":
            case "list":
            case "bool": {
                Token variable = require(Arrays.asList(TokenType.ID));
                return new LiteralDefinitionNode(variable, type);
            }
            case "func": {
                Token name = require(Arrays.asList(TokenType.ID));
                Node arguments = parseParentheses();
                Token t_ = require(Arrays.asList(TokenType.BLOCK));
                if (!t_.c.equals("does"))
                    raiseException("Expected `do` after function head definition");
                BlockNode node = new BlockNode();
                while (true) {
                    Node n = parseExpression();
                    if (n instanceof EndNode) break;
                    node.addNode(n);
                }
                return new LiteralFunctionNode(name, arguments, node);
            }
            default: {
                raiseException("Unknown type `" + type.c + "`");
                break;
            }
        }
        return null;
    }

    public Node parseSquaredParentheses() {
        if (tokens.get(pos).t.equals(TokenType.LSPAR) && tokens.get(pos+1).t.equals(TokenType.RSPAR)) {
            pos += 2;
            return new LiteralListNode();
        }

        if (match(Arrays.asList(TokenType.LSPAR)) != null) {
            Node node = parseFormula();
            if (getNext().t.equals(TokenType.COMMA)) {
                LiteralListNode multiElementNode = new LiteralListNode();
                multiElementNode.addNode(node);
                while (getNext().t.equals(TokenType.COMMA)) {
                    require(Arrays.asList(TokenType.COMMA));
                    multiElementNode.addNode(parseFormula());
                }
                require(Arrays.asList(TokenType.RSPAR));
                return multiElementNode;
            } else {
                require(Arrays.asList(TokenType.RSPAR));
                return node;
            }
        } else return parseVarOrLiteral();
    }

    public Node parseEveryBlock() {
        BlockNode code = new BlockNode();
        Node variable = parseVarOrLiteral();
        if (!(variable instanceof VariableNode))
            raiseException("`every` accepts only variable, but not " + variable.toString());
        Token t_ = require(Arrays.asList(TokenType.KEYWORD));
        if (!t_.c.equals("in"))
            raiseException("Expected `in` after variable in `every` construction header");
        Node list = parseVarOrLiteral();
        if (!(variable instanceof VariableNode || variable instanceof LiteralListNode))
            raiseException("`every` accepts only variable or list as iterable, but not " + list.toString());
        Token t__ = require(Arrays.asList(TokenType.BLOCK));
        if (!t__.c.equals("do"))
            raiseException("Expected `do` after `every` construction header");
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            code.addNode(node);
        }
        return new EveryBlockNode(list, ((VariableNode) variable), code);
    }


    public Node parseExpression() {
        if (match(Arrays.asList(TokenType.ID)) != null) {
            pos--;
            Node varNode = parseVarOrLiteral();
            if (varNode instanceof FunctionCallNode) return varNode;
            Token nextToken = getNext();
            pos++;
            if (nextToken.t.equals(TokenType.ASSIGNOPERATOR) || nextToken.t.equals(TokenType.BINARYOPERATOR)) {
                Node rfNode = parseFormula();
                if (nextToken.c.equals(".")) return new FieldReferenceNode(nextToken, varNode, rfNode);
                return new BinaryOperatorNode(nextToken, varNode, rfNode);
            } else if (nextToken.t.equals(TokenType.LPAR) && varNode instanceof VariableNode) {
                return parseFunctionCall(varNode);
            } else {
                raiseException("Unknown operation `" + nextToken + "` for `" + varNode.toString() + "`");
                return null;
            }
        } else if (match(Arrays.asList(TokenType.KEYWORD)) != null) {
            pos--;
            Token token = match(Arrays.asList(TokenType.KEYWORD));
            switch (token.c) {
                case "if": {
                    return parseIfBlock();
                }
                case "elseif": {
                    return parseElseIfBlock();
                }
                case "else": {
                    return parseElseBlock();
                }
                case "through": {
                    return parseThroughBlock();
                }
                case "try": {
                    return parseTryBlock();
                }
                case "loop": {
                    return parseLoopStopBlock();
                }
                case "while": {
                    return parseWhileBlock();
                }
                case "every": {
                    return parseEveryBlock();
                }
            }
        } else if (match(Arrays.asList(TokenType.BLOCK)) != null) {
            pos--;
            Token token = match(Arrays.asList(TokenType.BLOCK));
            if (token.c.equals("end")) {
                return new EndNode(token);
            }
        } else if (match(Arrays.asList(TokenType.INSTRUCTION)) != null) {
            pos--;
            Token token = match(Arrays.asList(TokenType.INSTRUCTION));
            return new InstructionNode(token);
        } else if (match(Arrays.asList(TokenType.WHITESPACE)) != null ||
                match(Arrays.asList(TokenType.COMMENT)) != null) {
            return null;
        } else if (match(Arrays.asList(TokenType.TYPE)) != null) {
            pos--;
            return parseTypeStatement();
        } else if (getNext().t.equals(TokenType.UNARYOPERATOR)) {
            return parseUnaryOperator();
        }
            raiseException("Unknown exception!");
        return null;
    }

    public Node parseCode() {
        while (pos < tokens.size()) {
            Node node = parseExpression();
            if (node != null) scope.addNode(node);
        }
        return scope;
    }

}
