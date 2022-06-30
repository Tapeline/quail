package me.tapeline.quailj.parser;

import me.tapeline.quailj.lexer.Lexer;
import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.lexer.TokenType;
import me.tapeline.quailj.parser.nodes.*;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                    if (current.c.equals(s) && !current.t.equals(TokenType.LITERALSTRING)) {
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
                if (current.c.equals(s) && !current.t.equals(TokenType.LITERALSTRING)) {
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
        for (String s : acceptable) if (token.c.equals(s) && !token.t.equals(TokenType.LITERALSTRING)) return token;
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
        return Utilities.opToString.get(bh);
    }

    public static MultiElementNode multiElementIfNeeded(Node node) {
        if (!(node instanceof MultiElementNode)) {
            MultiElementNode b = new MultiElementNode(new Token(TokenType.ID, "", node.codePos));
            b.addNode(node);
            return b;
        } else return (MultiElementNode) node;
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

                while (getCurrent() != null && getCurrent().c.equals("elseif")) {
                    pos++;
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

                if (getCurrent() != null && getCurrent().c.equals("else")) {
                    pos++;
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
                if (!(expr instanceof BinaryOperatorNode)) error("Expected range");
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
                requireString(TokenType.BINARYOPERATOR, new String[] {"in"},
                        "Expected `in`");
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

        if (match(new String[] {"break", "continue", "breakpoint", "memory"}) != null) {
            return new InstructionNode(previous());
        }

        Node n = parseEffect();
        if (n != null) return n;

        n = parseEvent();
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

    public Node parseEvent() throws RuntimeStriker {
        if (match(new String[] {"on", "when"}) != null) {
            Token t = previous();
            Node name = parseExpression();
            if (!getCurrent().t.equals(TokenType.ID))
                error("Expected 'event' 'variable' syntax");
            FunctionCallNode f = new FunctionCallNode(name, new MultiElementNode(Collections.singletonList(
                    new VariableNode(match(TokenType.ID))), previous().p),previous().p);
            Node code = parseStatement();
            return new EventNode(f, code, t.p);
        } else return null;
    }

    public Node parseEffect() throws RuntimeStriker {
        if (match(TokenType.EFFECT) != null) {
            Token t = previous();
            Node expr = parseExpression();
            if (expr == null) error("Null expression");
            if (t.c.equals("use") || t.c.equals("using")) {
                if (getCurrent().c.equals("as")) {
                    pos++;
                    return new EffectNode(t, expr, require(TokenType.ID,
                            "Expected ID after as in use").c);
                }
            }
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
                MultiElementNode args = new MultiElementNode(getCurrent());
                if (getCurrent().c.equals("(")) args = multiElementIfNeeded(parseExpression());
                BlockNode b = blockIfNeeded(parseStatement());
                return new LiteralFunctionNode(name, args, b, t.c.equals("staticmethod"));
            }
            case "override": {
                Token override = getCurrent();
                pos++;
                MultiElementNode args = new MultiElementNode(getCurrent());
                if (getCurrent().c.equals("(")) args = multiElementIfNeeded(parseExpression());
                BlockNode b = blockIfNeeded(parseStatement());
                return new LiteralFunctionNode(new Token(
                        override.t, getSystemField(override.c), override.p
                ), args, b, false);
            }
            case "object": {
                pos++;
                MultiElementNode args = new MultiElementNode(getCurrent());
                if (getCurrent().c.equals("(")) args = multiElementIfNeeded(parseExpression());
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

        if (match(new String[] {"=", "<-"}) != null) {
            Token equals = previous();
            Node value = EParseAssignment();
            if (expr instanceof VariableNode) {
                return new BinaryOperatorNode(equals, expr, value);
            } else if (expr instanceof FieldReferenceNode) {
                FieldReferenceNode get = (FieldReferenceNode) expr;
                return new FieldSetNode(getCurrent(), get.lnode, get.rnode, value);
            } else if (expr instanceof IndexReferenceNode) {
                IndexReferenceNode get = (IndexReferenceNode) expr;
                return new IndexSetNode(getCurrent(), get.lnode, get.rnode, value);
            } else {
                return new BinaryOperatorNode(equals, expr, value);
            }
        } else if (match(TokenType.SHORTBINARYOPERATOR) != null) {
            Token equals = previous();
            Node value = EParseAssignment();
            if (expr instanceof VariableNode) {
                return new BinaryOperatorNode(new Token(TokenType.BINARYOPERATOR,
                        "=", equals.p), expr, new BinaryOperatorNode(
                        new Token(TokenType.BINARYOPERATOR,
                                equals.c.substring(0, equals.c.length() - 1),
                                equals.p), expr, value));
            } else if (expr instanceof FieldReferenceNode) {
                FieldReferenceNode get = (FieldReferenceNode) expr;
                return new FieldSetNode(getCurrent(), get.lnode, get.rnode,
                        new BinaryOperatorNode(
                                new Token(TokenType.BINARYOPERATOR,
                                        equals.c.substring(0, equals.c.length() - 1),
                                        equals.p), get, value));
            } else if (expr instanceof IndexReferenceNode) {
                IndexReferenceNode get = (IndexReferenceNode) expr;
                return new IndexSetNode(getCurrent(), get.lnode, get.rnode,
                        new BinaryOperatorNode(
                                new Token(TokenType.BINARYOPERATOR,
                                        equals.c.substring(0, equals.c.length() - 1),
                                        equals.p), get, value));
            } else {
                return new BinaryOperatorNode(equals, expr, value);
            }
        }
        return expr;
    }

    private Node EParseOr() throws RuntimeStriker {
        Node expr = EParseAnd();
        while (match(TokenType.BINARYOPERATOR, new String[] {"or", "filter"}) != null) {
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
                "==", "!=", "is same type as", "is", "in", ":", ":+", "is type of", "instanceof"}) != null) {
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
        while ( match(TokenType.BINARYOPERATOR, new String[] {"/", "*", "//", "%"}) != null) {
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
                "-",
                "&",
                "*",
                "##"}) != null) {
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
        if (match(new String[] {"["}) != null) {
            Token t = previous();
            Node index = EParseOr();
            require(TokenType.RSPAR, "Expected ] to close indexation");
            return new IndexReferenceNode(t, new FunctionCallNode(callee, args, callee.codePos), index);
        }
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

        while (match(TokenType.LSPAR) != null) {
            Token t = previous();
            Node index = EParseOr();
            require(TokenType.RSPAR, "Expected ] to close indexing");
            expr = new IndexReferenceNode(previous(), expr, index);
        }
        return expr;
    }

    private Node EParsePrimary() throws RuntimeStriker {
        if (match(TokenType.LITERALBOOL  ) != null) return new LiteralBoolNode  (previous());
        if (match(TokenType.LITERALNULL  ) != null) return new LiteralNullNode  (previous());
        if (match(TokenType.LITERALNUM   ) != null) return new LiteralNumNode   (previous());
        if (match(TokenType.LITERALSTRING) != null) return new LiteralStringNode(previous());

        if (match(TokenType.TYPE, new String[] {
                "num", "string", "bool", "void", "anyof",
                "list", "container", "object", "require", "local"
        }) != null) {
            pos--;
            List<VariableModifier> modifiers = new ArrayList<>();
            while (match(TokenType.TYPE, new String[] {
                    "num", "string", "bool", "void", "anyof", "object<",
                    "list", "container", "object", "require", "local"
            }) != null) {
                Token m = previous();
                if (m.c.equals("require")) {
                    modifiers.add(new RequireModifier());
                } else if (m.c.equals("local")) {
                    modifiers.add(new LocalModifier());
                } else if (m.c.startsWith("object")) {
                    if (m.c.endsWith("<")) {
                        Node objClass = EParseCall();
                        requireString(TokenType.BINARYOPERATOR, new String[] {">"},
                                "Expected > to close class-clarification");
                        TypeModifier mod = new TypeModifier(ContainerType.class);
                        mod.objectClass = objClass;
                        modifiers.add(mod);
                        continue;
                    } else modifiers.add(new TypeModifier(ContainerType.class));
                } else if (m.c.equals("anyof")) {
                    List<TypeModifier> types = new ArrayList<>();
                    do {
                        Token m_ = match(TokenType.TYPE, new String[] {
                                "num", "string", "bool", "void", "object<",
                                "list", "container", "object", "require"
                        });
                        if (m_.c.startsWith("object")) {
                            if (m_.c.endsWith("<")) {
                                Node objClass = EParseCall();
                                requireString(TokenType.BINARYOPERATOR, new String[] {">"},
                                        "Expected > to close class-clarification");
                                TypeModifier mod = new TypeModifier(ContainerType.class);
                                mod.objectClass = objClass;
                                types.add(mod);
                            }
                        } else {
                            switch (m_.c) {
                                case "num": types.add(new TypeModifier(NumType.class));
                                    break;
                                case "void": types.add(new TypeModifier(VoidType.class));
                                    break;
                                case "string": types.add(new TypeModifier(
                                        StringType.class));
                                    break;
                                case "list": types.add(new TypeModifier(ListType.class));
                                    break;
                                case "container": types.add(new TypeModifier(
                                        ContainerType.class));
                                    break;
                                case "bool": types.add(new TypeModifier(BoolType.class));
                                    break;
                            }
                        }
                    } while (match(TokenType.PILLAR) != null);
                    modifiers.add(new AnyofModifier(types));
                } else {
                    switch (m.c) {
                        case "num": modifiers.add(new TypeModifier(NumType.class));
                            break;
                        case "void": modifiers.add(new TypeModifier(VoidType.class));
                            break;
                        case "string": modifiers.add(new TypeModifier(
                                StringType.class));
                            break;
                        case "list": modifiers.add(new TypeModifier(ListType.class));
                            break;
                        case "container": modifiers.add(new TypeModifier(
                                ContainerType.class));
                            break;
                        case "bool": modifiers.add(new TypeModifier(BoolType.class));
                            break;
                    }
                }
            }
            require(TokenType.ID, "Expected ID after clarification");
            VariableNode v = new VariableNode(previous());
            v.modifiers = modifiers;
            if (match(TokenType.CONSUME) != null) v.isConsumer = true;
            return v;
        }

        if (match(TokenType.ID) != null) {
            VariableNode v = new VariableNode(previous());
            if (match(TokenType.CONSUME) != null) v.isConsumer = true;
            return v;
        }

        if (match(TokenType.LPAR) != null)  {
            if (getCurrent().t.equals(TokenType.RPAR))
                return new MultiElementNode(match(TokenType.RPAR));
            Node expr = parseExpression();
            if (match(TokenType.COMMA) != null) {
                MultiElementNode list = new MultiElementNode(previous());
                list.addNode(expr);
                do {
                    list.addNode(parseExpression());
                } while (match(TokenType.COMMA) != null);
                require(TokenType.RPAR, "Expected closing bracket");
                if (match(TokenType.LAMBDAARROW) != null) {
                    Token arrow = previous();
                    BlockNode stmt = blockIfNeeded(parseStatement());
                    return new LiteralFunctionNode(arrow, list, stmt, false);
                }
                return list;
            }
            require(TokenType.RPAR, "Expected closing bracket");
            if (match(TokenType.LAMBDAARROW) != null) {
                Token arrow = previous();
                BlockNode stmt = blockIfNeeded(parseStatement());
                return new LiteralFunctionNode(arrow, expr, stmt, false);
            }
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
