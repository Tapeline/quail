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
    private boolean stop = false;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void raiseException(String msg) {
        stop = true;
        System.err.println("[QParser] (X) " + msg);
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
            stop = true;
            raiseException("Expected one of `" + tokenTypes.toString() + "` types, but got `" +
                    getNext().toString() + "`");
            return null;
        }
        return token;
    }

    public Node parseParentheses() {
        if (match(Arrays.asList(TokenType.LPAR)) != null) {
            Node node = parseFormula();
            require(Arrays.asList(TokenType.RPAR));
            return node;
        } else return parseVarOrLiteral();
    }

    public Node parseFormula() {
        Node lNode = parseParentheses();
        Token op = match(Arrays.asList(TokenType.BINARYOPERATOR));
        while (op != null) {
            Node rNode = parseParentheses();
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
        if (var != null) return new VariableNode(var);
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
            if (stop) return null;
            code.addNode(node);
        }
        return new IfBlockNode(condition, code);
    }

    public Node parseElseIfBlock() {
        Node condraw = parseParentheses();
        BinaryOperatorNode condition = new BinaryOperatorNode(new Token(TokenType.BINARYOPERATOR, "==", 0),
                condraw, new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", 0)));
        BlockNode code = new BlockNode();
        if (stop) return null;
        Token t_ = require(Arrays.asList(TokenType.BLOCK));
        if (t_ == null || !t_.c.equals("do")) {
            raiseException("Expected do near `elseif`!");
            return null;
        }
        while (true) {
            Node node = parseExpression();
            if (node instanceof EndNode) break;
            if (stop) return null;
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
            if (stop) return null;
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
        if (stop) return null;
        Token t_ = require(Arrays.asList(TokenType.KEYWORD));
        if (t_ == null || !t_.c.equals("as")) {
            raiseException("Expected `as` in `through` construction!");
            return null;
        }
        Node varRaw = parseVarOrLiteral();
        if (stop) return null;
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
            if (stop) return null;
            code.addNode(node);
        }
        return new ThroughBlockNode((BinaryOperatorNode) rangeRaw, variable, code);
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
            if (stop) return null;
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
        if (stop) return null;
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
            if (stop) return null;
            catchCode.addNode(node);
        }
        return new TryCatchNode(tryCode, catchCode, variable);
    }


    public Node parseExpression() {
        if (match(Arrays.asList(TokenType.ID)) != null) {
            pos--;
            Node varNode = parseVarOrLiteral();
            if (stop) return null;
            Token nextToken = getNext();
            pos++;
            if (nextToken.t.equals(TokenType.ASSIGNOPERATOR)) {
                Node rfNode = parseFormula();
                if (stop) return null;
                return new BinaryOperatorNode(nextToken, varNode, rfNode);
            /*} else if (nextToken.t.equals(TokenType.FIELDREFERENCE)) {
                Node field = parseVarOrLiteral();
                if (getNext().t.equals(TokenType.FIELDREFERENCE))
                return new ObjectFieldReferenceNode(nextToken, varNode, field);
            */} else {
                raiseException("Unknown operation `" + nextToken.toString() + "` for `" + varNode.toString() + "`");
                return null;
            }
        } else if (match(Arrays.asList(TokenType.KEYWORD)) != null) {
            pos--;
            Token token = match(Arrays.asList(TokenType.KEYWORD));
            if (token.c.equals("if")) {
                Node block = parseIfBlock();
                if (stop) return null;
                return block;
            } else if (token.c.equals("elseif")) {
                Node block = parseElseIfBlock();
                if (stop) return null;
                return block;
            } else if (token.c.equals("else")) {
                Node block = parseElseBlock();
                if (stop) return null;
                return block;
            } else if (token.c.equals("through")) {
                Node block = parseThroughBlock();
                if (stop) return null;
                return block;
            } else if (token.c.equals("try")) {
                Node block = parseTryBlock();
                if (stop) return null;
                return block;
            }
        } else if (match(Arrays.asList(TokenType.BLOCK)) != null) {
            pos--;
            Token token = match(Arrays.asList(TokenType.BLOCK));
            if (token.c.equals("end")) {
                Node end = new EndNode(token);
                if (stop) return null;
                return end;
            }
        } else if (match(Arrays.asList(TokenType.INSTRUCTION)) != null) {
            pos--;
            Token token = match(Arrays.asList(TokenType.INSTRUCTION));
            if (token.c.equals("milestone") || token.c.equals("breakpoint")) {
                Node node = new InstructionNode(token);
                if (stop) return null;
                return node;
            }
        } else if (match(Arrays.asList(TokenType.WHITESPACE)) != null ||
                match(Arrays.asList(TokenType.COMMENT)) != null) {
            return null;
        } else if (getNext().t.equals(TokenType.UNARYOPERATOR)) {
            Node node = parseUnaryOperator();
            if (stop) return null;
            return node;
        }
        raiseException("Unknown exception!");
        return null;
    }

    public Node parseCode() {
        while (pos < tokens.size()) {
            Node node = parseExpression();
            if (stop) return null;
            if (node != null) scope.addNode(node);
        }
        return scope;
    }

}
