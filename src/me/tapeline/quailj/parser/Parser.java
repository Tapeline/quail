package me.tapeline.quailj.parser;

import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.lexer.TokenType;
import me.tapeline.quailj.parser.nodes.*;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.Arrays;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private final BlockNode root = new BlockNode(0);
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void error(String msg) throws RuntimeStriker {
        throw new RuntimeStriker("Parsing failed!\nAt " + tokens.get(pos).p + " pos in code '"
                + tokens.get(pos).c + "':\n" + msg);
    }

    public Token match(TokenType tokenType) {
        if (pos < tokens.size()) {
            Token current = tokens.get(pos);
            if (tokenType.equals(current.t)) {
                pos++;
                return current;
            }
        }
        return null;
    }

    public Token matchMultiple(TokenType[] tokenTypes) {
        if (pos < tokens.size()) {
            Token current = tokens.get(pos);
            for (TokenType tt : tokenTypes) {
                if (current.t.equals(tt)) {
                    pos++;
                    return current;
                }
            }
        }
        return null;
    }

    public Token match(TokenType tokenType, String[] acceptable) {
        if (pos < tokens.size()) {
            Token current = tokens.get(pos);
            if (tokenType.equals(current.t)) {
                for (String s : acceptable) {
                    if (current.c.equals(s)) {
                        pos++;
                        return current;
                    }
                }
            }
        }
        return null;
    }

    public Token match(String[] acceptable) {
        if (pos < tokens.size()) {
            Token current = tokens.get(pos);
            for (String s : acceptable) {
                if (current.c.equals(s)) {
                    pos++;
                    return current;
                }
            }
        }
        return null;
    }

    public Token getCurrent() {
        if (pos < tokens.size()) return tokens.get(pos);
        return null;
    }

    public Token previous() {
        if (pos > 0) return tokens.get(pos - 1);
        return null;
    }

    public Token require(TokenType tokenType, String customMessage) throws RuntimeStriker {
        Token token = match(tokenType);
        if (token == null) error(customMessage);
        return token;
    }

    public Token requireMultiple(TokenType[] tokenTypes, String customMessage) throws RuntimeStriker {
        Token token = matchMultiple(tokenTypes);
        if (token == null) error(customMessage);
        return token;
    }

    public Token requireString(TokenType tokenType, String[] acceptable, String customMessage) throws RuntimeStriker {
        Token token = require(tokenType, customMessage);
        for (String s : acceptable) if (token.c.equals(s)) return token;
        error(customMessage);
        return null;
    }

    public static BlockNode blockIfNeeded(Node node) {
        if (!(node instanceof BlockNode)) {
            BlockNode b = new BlockNode(node.codePos);
            b.addNode(node);
            return b;
        } else return (BlockNode) node;
    }

    public Token matchEnd() {
        if (Arrays.asList("}", "end").contains(getCurrent().c)) {
            pos++;
            return previous();
        } else return null;
    }

    public Token matchStart() {
        if (Arrays.asList("do", "does", "this", "with", "{", "has").contains(getCurrent().c)) {
            pos++;
            return previous();
        } else return null;
    }

    public static String getSystemField(String bh) {
        bh = bh.replaceAll("\\+", "_add");
        bh = bh.replaceAll("-", "_sub");
        bh = bh.replaceAll("//", "_divint");
        bh = bh.replaceAll("/", "_div");
        bh = bh.replaceAll("\\*", "_mul");
        bh = bh.replaceAll("\\^", "_pow");
        bh = bh.replaceAll("%", "_mod");
        bh = bh.replaceAll("==", "_cmpeq");
        bh = bh.replaceAll("!=", "_cmpuneq");
        bh = bh.replaceAll("<=", "_cmplet");
        bh = bh.replaceAll(">=", "_cmpget");
        bh = bh.replaceAll(">", "_cmpgt");
        bh = bh.replaceAll("<", "_cmplt");
        bh = bh.replaceAll("get", "_get");
        bh = bh.replaceAll("tostring", "_tostring_");
        bh = bh.replaceAll("tonumber", "_tonumber");
        bh = bh.replaceAll("tobool", "_tobool");
        bh = bh.replaceAll("not", "_not");
        bh = bh.replaceAll("!", "_not");
        bh = bh.replaceAll("set", "_set");
        return bh;
    }




    public Node parse() throws RuntimeStriker {
        while (pos < tokens.size()) {
            root.addNode(parseStatement());
        }
        return root;
    }



    public Node parseStatement() throws RuntimeStriker {
        Node node = null;

        Token ct = getCurrent();
        switch (ct.c) {
            case "do":
            case "does":
            case "then":
            case "with":
            case "has":
            case "{": {
                Token t = getCurrent();
                pos++;
                BlockNode blockNode = new BlockNode(t.p);
                while (matchEnd() == null) {
                    blockNode.addNode(parseStatement());
                }
                return blockNode;
            }
            case "if": {
                Token t = match(TokenType.KEYWORD);
                Node expr = parseExpression();
                if (expr == null) error("Null expression");
                BinaryOperatorNode ifCondition = new BinaryOperatorNode(
                        new Token(TokenType.BINARYOPERATOR, "==", t.p),
                        expr,
                        new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", t.p)));
                Node ifCode = null;
                if (matchStart() != null) {
                    ifCode = new BlockNode(previous().p);
                    while (matchEnd() == null && !getCurrent().c.equals("elseif")
                            && !getCurrent().c.equals("else")) {
                        ((BlockNode) ifCode).addNode(parseStatement());
                    }
                } else ifCode = parseStatement();
                IfBlockNode ifBlockNode = new IfBlockNode(ifCondition, blockIfNeeded(ifCode));

                while (getCurrent().c.equals("elseif")) {
                    Node expr2 = parseExpression();
                    if (expr2 == null) error("Null expression");
                    BinaryOperatorNode elseIfCondition = new BinaryOperatorNode(
                            new Token(TokenType.BINARYOPERATOR, "==", t.p),
                            expr2,
                            new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", t.p)));
                    Node elseIfCode = null;
                    if (matchStart() != null) {
                        elseIfCode = new BlockNode(previous().p);
                        while (matchEnd() == null && !getCurrent().c.equals("elseif")
                                && !getCurrent().c.equals("else")) {
                            ((BlockNode) elseIfCode).addNode(parseStatement());
                        }
                    } else elseIfCode = parseStatement();
                    ElseIfBlockNode elseIfBlockNode = new ElseIfBlockNode(elseIfCondition,
                            blockIfNeeded(elseIfCode));
                    ifBlockNode.linkedNodes.add(elseIfBlockNode);
                }

                if (getCurrent().c.equals("else")) {
                    Node elseCode = null;
                    if (matchStart() != null) {
                        elseCode = new BlockNode(previous().p);
                        while (matchEnd() == null) ((BlockNode) elseCode).addNode(parseStatement());
                    } else elseCode = parseStatement();
                    ElseBlockNode elseBlockNode = new ElseBlockNode(blockIfNeeded(elseCode));
                    ifBlockNode.linkedNodes.add(elseBlockNode);
                }
                return ifBlockNode;
            }

            case "while": {
                Token t = match(TokenType.KEYWORD);
                Node expr = parseExpression();
                if (expr == null) error("Null expression");
                BinaryOperatorNode condition = new BinaryOperatorNode(
                        new Token(TokenType.BINARYOPERATOR, "==", t.p),
                        expr,
                        new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", t.p)));
                return new WhileBlockNode(condition, blockIfNeeded(parseStatement()), t.p);
            }

            case "loop": {
                Token t = match(TokenType.KEYWORD);
                Node code = null;
                if (matchStart() != null) {
                    code = new BlockNode(previous().p);
                    while (matchEnd() == null && !getCurrent().c.equals("stop when")) {
                        ((BlockNode) code).addNode(parseStatement());
                    }
                } else code = parseStatement();
                Token tt = requireString(TokenType.KEYWORD, new String[] {"stop when"},
                        "Expected `stop when`");
                Node expr = parseExpression();
                if (expr == null) error("Null expression");
                BinaryOperatorNode condition = new BinaryOperatorNode(
                        new Token(TokenType.BINARYOPERATOR, "==", tt.p),
                        expr,
                        new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", tt.p)));
                return new LoopStopBlockNode(condition, blockIfNeeded(code), t.p);
            }

            case "through": {
                Token t = match(TokenType.KEYWORD);
                Node expr = parseExpression();
                if (expr == null) error("Null expression");
                if (!(expr instanceof BinaryOperatorNode) ||
                        !((BinaryOperatorNode) expr).operator.c.equals(":")) error("Expected range");
                requireString(TokenType.KEYWORD, new String[] {"as"},
                        "Expected `as`");
                VariableNode variableNode = new VariableNode(require(TokenType.ID,
                        "Expected variable"));
                assert expr instanceof BinaryOperatorNode;
                return new ThroughBlockNode((BinaryOperatorNode) expr,
                        variableNode, blockIfNeeded(parseStatement()), t.p);
            }

            case "every": {
                Token t = match(TokenType.KEYWORD);
                VariableNode variableNode = new VariableNode(require(TokenType.ID,
                        "Expected variable"));
                requireString(TokenType.KEYWORD, new String[] {"in"},
                        "Expected `as`");
                Node expr = parseExpression();
                if (expr == null) error("Null expression");
                return new EveryBlockNode(expr, variableNode, blockIfNeeded(parseStatement()), t.p);
            }

            case "try": {
                Token t = getCurrent();
                pos++;
                matchStart();
                BlockNode code;
                code = new BlockNode(previous().p);
                while (matchEnd() == null && !getCurrent().c.equals("catch")) {
                    code.addNode(parseStatement());
                }
                BlockNode cat = new BlockNode(t.p);
                VariableNode v = new VariableNode(new Token(TokenType.ID, "_", 0));
                if (match(TokenType.KEYWORD, new String[] {"catch"}) != null) {
                    requireString(TokenType.KEYWORD, new String[]{"as"}, "Expected `as`");
                    v = new VariableNode(require(TokenType.ID, "Expected variable"));
                    matchStart();
                    cat = new BlockNode(previous().p);
                    while (matchEnd() == null) {
                        cat.addNode(parseStatement());
                    }
                }
                return new TryCatchBlockNode(code, cat, v, t.p);
            }
        }

        if (getCurrent().c.equals("nothing")) {
            pos++;
            return new InstructionNode(previous());
        }

        Node n = parseEffect();
        if (n != null) return n;

        n = parseContainer();
        if (n != null) return n;

        n = parseFunction();
        if (n != null) return n;

        n = parseExpression();
        if (n != null) return n;

        error("Expected statement, but none found!");
        return null;
    }

    public Node parseEffect() throws RuntimeStriker {
        if (match(TokenType.EFFECT) != null) {
            Token t = previous();
            Node expr = parseExpression();
            if (expr == null) error("Null expression");
            return new EffectNode(t, expr);
        } else return null;
    }

    public Node parseContainer() throws RuntimeStriker {
        Token t = match(TokenType.TYPE, new String[] {"container", "metacontainer", "class"});
        if (t == null) return null;
        Token name = require(TokenType.ID, "Expected id");
        Token like = new Token(name.t, "container", name.p);
        if (getCurrent().c.equals("like")) {
            pos++;
            like = require(TokenType.ID, "Expected id");
        }
        LiteralContainerNode n = new LiteralContainerNode(
                name, !t.c.equals("container"), like.c
        );
        matchStart();
        while (matchEnd() == null) {
            n.initialize.add(parseStatement());
        }
        return n;
    }

    public Node parseFunction() throws RuntimeStriker {
        Token t = match(new String[] {
                "function", "func", "method", "staticmethod", "override", "object"
        });
        if (t == null) return null;
        switch (t.c) {
            case "function":
            case "func":
            case "method":
            case "staticmethod": {
                Token name = require(TokenType.ID, "Expected id");
                Node args = parseExpression();
                BlockNode b = blockIfNeeded(parseStatement());
                return new LiteralFunctionNode(name, args, b, t.c.equals("staticmethod"));
            }
            case "override": {
                Token override = getCurrent();
                pos++;
                Node args = parseExpression();
                BlockNode b = blockIfNeeded(parseStatement());
                return new LiteralFunctionNode(new Token(
                        override.t, getSystemField(override.c), override.p
                        ), args, b, false);
            }
            case "object": {
                pos++;
                Node args = parseExpression();
                BlockNode b = blockIfNeeded(parseStatement());
                return new LiteralFunctionNode(new Token(t.t, "_builder", t.p), args,
                        b, false);
            }
        }
        return null;
    }

    public Node parseExpression() throws RuntimeStriker {
        return EParseAssignment();
    }



    /*
        EXPRESSION PARSING
    */
    private Node EParseAssignment() throws RuntimeStriker {
        Node expr = EParseOr();

        if (match(new String[] {"="}) != null) {
            Token equals = previous();
            Node value = EParseAssignment();
            if (expr instanceof VariableNode) {
                return new BinaryOperatorNode(equals, expr, value);
            } else if (expr instanceof FieldReferenceNode) {
                FieldReferenceNode get = (FieldReferenceNode) expr;
                return new FieldSetNode(getCurrent(), get.lnode, get.rnode, value);
            } else {
                return new BinaryOperatorNode(equals, expr, value);
            }
        }
        return expr;
    }

    private Node EParseOr() throws RuntimeStriker {
        Node expr = EParseAnd();
        while (match(TokenType.BINARYOPERATOR, new String[] {"or"}) != null) {
            Token operator = previous();
            Node right = EParseAnd();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseAnd() throws RuntimeStriker {
        Node expr = EParseEquality();
        while (match(TokenType.BINARYOPERATOR, new String[] {"and", "step"}) != null) {
            Token operator = previous();
            Node right = EParseEquality();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseEquality() throws RuntimeStriker {
        Node expr = EParseComparison();
        while ( match(TokenType.BINARYOPERATOR, new String[] {
                "==", "!=", "is same as", "is", "in", "..."}) != null) {
            Token operator = previous();
            Node right = EParseComparison();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseComparison() throws RuntimeStriker {
        Node expr = EParseTerm();
        while ( match(TokenType.BINARYOPERATOR, new String[] {"<", ">", ">=", "<="}) != null) {
            Token operator = previous();
            Node right = EParseTerm();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseTerm() throws RuntimeStriker {
        Node expr = EParseFactor();
        while ( match(TokenType.BINARYOPERATOR, new String[] {"+", "-"}) != null) {
            Token operator = previous();
            Node right = EParseFactor();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseFactor() throws RuntimeStriker {
        Node expr = EParsePower();
        while ( match(TokenType.BINARYOPERATOR, new String[] {"/", "*"}) != null) {
            Token operator = previous();
            Node right = EParsePower();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParsePower() throws RuntimeStriker {
        Node expr = EParseUnary();
        while ( match(TokenType.BINARYOPERATOR, new String[] {"^"}) != null) {
            Token operator = previous();
            Node right = EParseUnary();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }


    private Node EParseUnary() throws RuntimeStriker {
        if (match(new String[] {
                "not",
                "exists",
                "notnull",
                "negate",
                "!",
                "-"}) != null) {
            Token operator = previous();
            Node right = EParseUnary();
            return new UnaryOperatorNode(operator, right);
        }
        return EParseCall();
    }

    private Node EFinishCall(Node callee) throws RuntimeStriker {
        MultiElementNode args = new MultiElementNode(getCurrent());
        if (!getCurrent().t.equals(TokenType.RPAR)) {
            do {
                args.addNode(parseExpression());
            } while (match(TokenType.COMMA) != null);
        }
        require(TokenType.RPAR, "Expected closing bracket");
        return new FunctionCallNode(callee, args, callee.codePos);
    }

    private Node EParseCall() throws RuntimeStriker {
        Node expr = EParsePrimary();
        while (true) {
            if (match(TokenType.LPAR) != null) {
                expr = EFinishCall(expr);
            } else if (match(TokenType.BINARYOPERATOR, new String[] {".", "'s", "'"}) != null) {
                Token name = requireMultiple(new TokenType[]{
                                TokenType.ID,
                                TokenType.LITERALBOOL,
                                TokenType.LITERALNUM,
                                TokenType.LITERALSTRING},
                        "Expected id");
                expr = new FieldReferenceNode(name, expr, new VariableNode(name));
            } else if (match(TokenType.BINARYOPERATOR, new String[] {"of", "at"}) != null) {
                Token name = requireMultiple(new TokenType[]{
                                TokenType.ID,
                                TokenType.LITERALBOOL,
                                TokenType.LITERALNUM,
                                TokenType.LITERALSTRING},
                        "Expected id");
                expr = new FieldReferenceNode(name, new VariableNode(name), expr);
            } else {
                break;
            }
        }
        return expr;
    }

    private Node EParsePrimary() throws RuntimeStriker {
        if (match(TokenType.LITERALBOOL  ) != null) return new LiteralBoolNode  (previous());
        if (match(TokenType.LITERALNULL  ) != null) return new LiteralNullNode  (previous());
        if (match(TokenType.LITERALNUM   ) != null) return new LiteralNumNode   (previous());
        if (match(TokenType.LITERALSTRING) != null) return new LiteralStringNode(previous());

        if (match(TokenType.ID) != null) return new VariableNode(previous());

        if (match(TokenType.LPAR) != null)  {
            Node expr = parseExpression();
            require(TokenType.RPAR, "Expected closing bracket");
            return expr;
        }

        if (match(TokenType.LSPAR) != null) {
            LiteralListNode list = new LiteralListNode(getCurrent());
            if (!getCurrent().t.equals(TokenType.RSPAR)) {
                do {
                    list.addNode(parseExpression());
                } while (match(TokenType.COMMA) != null);
            }
            require(TokenType.RSPAR, "Expected closing bracket");
            return list;
        }

        if (match(TokenType.LCPAR) != null) {
            Token t = previous();
            t.c = "_anonymous";
            LiteralContainerNode containerNode = new LiteralContainerNode(t, false,
                    "container");
            if (!getCurrent().t.equals(TokenType.RCPAR)) {
                do {
                    Node node = parseExpression();
                    if (!(node instanceof BinaryOperatorNode &&
                            ((BinaryOperatorNode) node).operator.c.equals("="))) {
                        error("Expected key=value pair");
                        return null;
                    }
                    containerNode.initialize.add(node);
                } while (match(TokenType.COMMA) != null);
            }
            require(TokenType.RCPAR, "Expected closing bracket");
            return containerNode;
        }

        if (getCurrent().c.equals("anonymous") || getCurrent().c.equals("function")) {
            Token t = getCurrent();
            t.c = "_anonymous";
            pos++;
            Node expr = parseExpression();
            if (expr == null) error("Null expression");
            BlockNode stmt = blockIfNeeded(parseStatement());
            return new LiteralFunctionNode(t, expr, stmt, false);
        }
        error("Expression parsing error");
        return null;
    }

}
