package me.tapeline.quailj.parsingtools;

import me.tapeline.quailj.Language;
import me.tapeline.quailj.debugtools.*;
import me.tapeline.quailj.parsingtools.nodes.*;
import me.tapeline.quailj.tokenizetools.tokens.*;

import java.util.*;

public class Parser {

    private final List<Token> tokens;
    private final RootNode scope = new RootNode();
    private int pos = 0;
    public AdvancedActionLogger aal;

    public Parser(List<Token> tokens, AdvancedActionLogger aal) {
        this.tokens = tokens;
        this.aal = aal;
    }

    public void error(String msg) {
        System.err.println("[QParser] (X) At " + pos + " symbol in code: " + msg);
        aal.err("QParser", "At " + pos + " symbol in code: " + msg);
        new AALFrame(aal);
        Scanner sc = new Scanner(System.in);
        sc.next();
        System.exit(101);
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

    public Token getCurrent() {
        if (pos < tokens.size()) return tokens.get(pos);
        return null;
    }

    public Token previous() {
        if (pos > 0) return tokens.get(pos - 1);
        return null;
    }

    public Token require(TokenType tokenType, String customMessage) {
        Token token = match(tokenType);
        if (token == null) error(customMessage);
        return token;
    }

    public Token requireMultiple(TokenType[] tokenTypes, String customMessage) {
        Token token = matchMultiple(tokenTypes);
        if (token == null)  error(customMessage);
        return token;
    }

    public Token requireString(TokenType tokenType, String[] acceptable, String customMessage) {
        Token token = require(tokenType, customMessage);
        for (String s : acceptable) if (token.c.equals(s)) return token;
        error(customMessage);
        return null;
    }

    public static BlockNode blockIfNeeded(Node node) {
        if (!(node instanceof BlockNode)) {
            BlockNode b = new BlockNode();
            b.addNode(node);
            return b;
        } else return (BlockNode) node;
    }

    public Token requireOpening() {
        Token t = tokens.get(pos++);
        if (t.t.equals(TokenType.LCPAR)
         || t.c.equals("do") || t.c.equals("does") || t.c.equals("has")  || t.c.equals("then")) {
            return t;
        } else {
            error("Expected opening keyword or left curve bracket, but got " + t.c);
        }
        return null;
    }

    public Token requireClosing() {
        Token t = tokens.get(pos++);
        if (t.t.equals(TokenType.RCPAR) || t.c.equals("end")) {
            return t;
        } else {
            error("Expected closing keyword or right curve bracket, but got " + t.c);
        }
        return null;
    }



    /*
        PARSING FUNCTIONS
     */

    public Node parseFunction(boolean soft) {
        aal.log("QParser", "Parsing function... Soft?", soft);
        if (soft && !Arrays.asList("func", "function").contains(getCurrent().c)) {
            aal.log("QParser", "Function not found.");
            return null;
        }
        requireString(TokenType.TYPE, new String[] {"func", "function"},
                Language.get("p.expected-func-event-container"));
        boolean isAnonymous = getCurrent().t.equals(TokenType.LPAR);
        Token id = new Token(TokenType.ID, "__anon__", getCurrent().p);
        if (!isAnonymous) {
            id = require(TokenType.ID, Language.get("p.func.no-id"));
        }
        requireString(TokenType.LPAR, new String[] {"("},
                Language.get("p.common.no-par"));
        MultiElementNode multiElementNode = new MultiElementNode();
        do {
            if (!getCurrent().t.equals(TokenType.ID)) break;
            multiElementNode.addNode(new VariableNode(match(TokenType.ID)));
            if (getCurrent().t.equals(TokenType.COMMA))
                match(TokenType.COMMA);
            else
                break;
        } while (getCurrent().t.equals(TokenType.ID));
        requireString(TokenType.RPAR, new String[] {")"},
                Language.get("p.common.no-par"));

        BlockNode statement = blockIfNeeded(parseStatement(false));
        if (isAnonymous) {
            aal.log("QParser", "Anonymous Function parsed");
        } else {
            aal.log("QParser", "Function", id.c, "parsed");
        }
        return new LiteralFunctionNode(id, multiElementNode, statement);
    }

    public Node parseAnonymousFunction() {
        aal.log("QParser", "Parsing anonymous function...");
        requireString(TokenType.LPAR, new String[] {"("},
                Language.get("p.common.no-par"));
        MultiElementNode multiElementNode = new MultiElementNode();
        do {
            if (!getCurrent().t.equals(TokenType.ID)) break;
            multiElementNode.addNode(new VariableNode(match(TokenType.ID)));
            if (getCurrent().t.equals(TokenType.COMMA))
                match(TokenType.COMMA);
            else
                break;
        } while (getCurrent().t.equals(TokenType.ID));
        requireString(TokenType.RPAR, new String[] {")"},
                Language.get("p.common.no-par"));

        BlockNode statement = blockIfNeeded(parseStatement(false));
        aal.log("QParser", "Anonymous function parsed");
        return new LiteralFunctionNode(new Token(TokenType.ID, "__anon__", 0), multiElementNode, statement);
    }

    public Node parseBuilder(boolean soft) {
        aal.log("QParser", "Parsing function... Soft?", soft);
        if (soft &&
                !Objects.equals("object", getCurrent().c)) {
            aal.log("QParser", "Builder not found.");
            return null;
        }
        Token t = requireString(TokenType.TYPE, new String[] {"object"},
                Language.get("p.expected-func-event-container"));
        requireString(TokenType.ID, new String[] {"builder"},
                Language.get("p.expected-func-event-container"));
        t.c = "__builder__";

        requireString(TokenType.LPAR, new String[] {"("},
                Language.get("p.common.no-par"));
        MultiElementNode multiElementNode = new MultiElementNode();
        do {
            if (!getCurrent().t.equals(TokenType.ID)) break;
            multiElementNode.addNode(new VariableNode(match(TokenType.ID)));
            if (getCurrent().t.equals(TokenType.COMMA))
                match(TokenType.COMMA);
            else
                break;
        } while (getCurrent().t.equals(TokenType.ID));
        requireString(TokenType.RPAR, new String[] {")"},
                Language.get("p.common.no-par"));

        BlockNode statement = blockIfNeeded(parseStatement(false));
        aal.log("QParser", "Builder parsed");
        return new LiteralFunctionNode(t, multiElementNode, statement);
    }

    public Node parseEvent(boolean soft) {
        aal.log("QParser", "Parsing event handler... Soft?", soft);
        if (soft && !Arrays.asList("on", "when").contains(getCurrent().c)) {
            aal.log("QParser", "Event handler not found.");
            return null;
        }
        requireString(TokenType.KEYWORD, new String[] {"on", "when"},
                Language.get("p.expected-func-event-container"));
        Token eventId = require(TokenType.ID, Language.get("p.event.no-id"));
        Token var = require(TokenType.ID, Language.get("p.event.no-var"));

        Token as = null;
        if (getCurrent().c.equals("as")) {
            requireString(TokenType.KEYWORD, new String[]{"as"},
                    Language.get("p.event.no-as"));
            as = require(TokenType.ID, Language.get("p.event.no-var"));
        }

        BlockNode statement = blockIfNeeded(parseStatement(false));
        aal.log("QParser", "Event handler", eventId.c, "parsed");
        return new LiteralEventNode(eventId, var, statement, as);
    }

    public Node parseContainer(boolean soft) {
        aal.log("QParser", "Parsing container... Soft?", soft);
        if (soft &&
                !Arrays.asList("metacontainer", "container").contains(getCurrent().c)) {
            aal.log("QParser", "Container not found.");
            return null;
        }
        boolean isMeta = requireString(TokenType.TYPE, new String[] {"metacontainer", "container"},
                Language.get("p.expected-func-event-container")).c.equals("metacontainer");
        Token id = require(TokenType.ID, Language.get("p.event.no-id"));
        String like = "container";
        if (getCurrent().c.equals("like")) {
            pos++;
            like = require(TokenType.ID, "p.container.no-like-id").c;
        }
        requireOpening();
        LiteralContainerNode literalContainerNode = new LiteralContainerNode(id.c, isMeta, like);
        aal.log("QParser", "Begin parsing contents of container", literalContainerNode);

        while (!getCurrent().c.equals("end") && !getCurrent().c.equals("}")) {
            Node builder = parseBuilder(true);
            if (builder != null) {
                literalContainerNode.builder = (LiteralFunctionNode) builder;
                aal.log("QParser", "Found custom builder. Replaced.");
            }

            Node func = parseFunction(true);
            if (func != null) {
                literalContainerNode.initialize.add(func);
                aal.log("QParser", "Added function");
                continue;
            }

            Node expr = parseExpression();
            if (expr != null) {
                literalContainerNode.initialize.add(expr);
                aal.log("QParser", "Added expression");
                continue;
            }

            error(Language.get("p.container.unknown-content"));
        }

        requireClosing();
        aal.log("QParser", "Container", literalContainerNode.name, "parsed");
        return literalContainerNode;
    }

    public Node parseStatement(boolean soft) {
        aal.log("QParser", "Parsing statement... Soft?" + soft);
        Token ct = getCurrent();
        switch (ct.c) {
            case "do":
            case "does":
            case "then":
            case "{":
                aal.log("QParser", "Found block statement. Parsing...");
                match(TokenType.BLOCK);
                BlockNode block = new BlockNode();
                while (!getCurrent().c.equals("end") && !getCurrent().c.equals("}"))
                    block.addNode(parseStatement(false));
                pos++;
                aal.log("QParser", "Statement parsed.");
                return block;
            case "if": {
                aal.log("QParser", "Found if statement. Parsing...");
                Token t = match(TokenType.KEYWORD);
                BinaryOperatorNode ifCondition = new BinaryOperatorNode(
                        new Token(TokenType.BINARYOPERATOR, "==", t.p),
                        parseExpression(),
                        new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", t.p)));
                Node ifCode = parseStatement(false);
                IfBlockNode ifBlockNode = new IfBlockNode(ifCondition, blockIfNeeded(ifCode));

                aal.log("QParser", "Trying to found linked elseifs...");
                while (getCurrent() != null && getCurrent().c.equals("elseif")) {
                    aal.log("QParser", "Found elseif statement. Parsing...");
                    t = match(TokenType.KEYWORD);
                    BinaryOperatorNode elseIfCondition = new BinaryOperatorNode(
                            new Token(TokenType.BINARYOPERATOR, "==", t.p),
                            parseExpression(),
                            new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", t.p)));
                    Node elseIfCode = parseStatement(false);
                    ifBlockNode.linkNode(new ElseIfBlockNode(elseIfCondition, blockIfNeeded(elseIfCode)));
                    aal.log("QParser", "Statement parsed and added to parent if.");
                }

                aal.log("QParser", "Trying to found linked else...");
                if (getCurrent() != null && getCurrent().c.equals("else")) {
                    pos++;
                    ifBlockNode.linkNode(new ElseBlockNode(blockIfNeeded(parseStatement(false))));
                }

                aal.log("QParser", "Statement parsed.");
                return ifBlockNode;
            }
            case "while": {
                Token t = match(TokenType.KEYWORD);
                BinaryOperatorNode condition = new BinaryOperatorNode(
                        new Token(TokenType.BINARYOPERATOR, "==", t.p),
                        parseExpression(),
                        new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", t.p)));
                Node code = parseStatement(false);
                aal.log("QParser", "Statement parsed.");
                return new WhileBlockNode(condition, blockIfNeeded(code));
            }
            case "through": {
                match(TokenType.KEYWORD);
                Node expression = parseExpression();
                if (!(expression instanceof BinaryOperatorNode)) {
                    error(Language.get("p.loop.invalid-expression"));
                    return null;
                }
                requireString(TokenType.KEYWORD, new String[] {"as"}, Language.get("p.loop.no-as-in"));
                Node var = parseExpression();
                Node code = parseStatement(false);
                aal.log("QParser", "Statement parsed.");
                return new ThroughBlockNode((BinaryOperatorNode) expression, var, blockIfNeeded(code));
            }
            case "every": {
                match(TokenType.KEYWORD);
                Node var = parseExpression();
                requireString(TokenType.KEYWORD, new String[] {"in"}, Language.get("p.loop.no-as-in"));
                Node expression = parseExpression();
                Node code = parseStatement(false);
                aal.log("QParser", "Statement parsed.");
                return new EveryBlockNode(expression, var, blockIfNeeded(code));
            }
            case "loop": {
                match(TokenType.KEYWORD);
                Node code = parseStatement(false);
                Token t = requireString(TokenType.KEYWORD, new String[] {"stop when"}, Language.get("p.loop.no-stop-when"));
                BinaryOperatorNode condition = new BinaryOperatorNode(
                        new Token(TokenType.BINARYOPERATOR, "==", t.p),
                        parseExpression(),
                        new LiteralBoolNode(new Token(TokenType.LITERALBOOL, "true", t.p)));
                aal.log("QParser", "Statement parsed.");
                return new LoopStopBlockNode(condition, blockIfNeeded(code));
            }
            case "try": {
                match(TokenType.KEYWORD);
                Node tryCode = parseStatement(false);
                requireString(TokenType.KEYWORD, new String[] {"catch"}, Language.get("p.try.no-catch"));
                requireString(TokenType.KEYWORD, new String[] {"as"}, Language.get("p.try.no-catch"));
                Token var = require(TokenType.ID, Language.get("p.common.expected-variable"));
                Node catchCode = parseStatement(false);
                aal.log("QParser", "Statement parsed.");
                return new TryCatchNode(blockIfNeeded(tryCode), blockIfNeeded(catchCode), new VariableNode(var));
            }
        }
        if (ct.t.equals(TokenType.INSTRUCTION)) {
            aal.log("QParser", "Statement parsed.");
            return new InstructionNode(match(TokenType.INSTRUCTION));
        }
        Node container = parseContainer(true);
        if (container != null) {
            aal.log("QParser", "Statement parsed.");
            return container;
        }
        if (ct.t.equals(TokenType.TYPE)) {
            pos++;
            switch (ct.c) {
                case "string":
                case "bool":
                case "num": {
                    return new LiteralDefinitionNode(require(TokenType.ID, "Expected id!"), ct);
                }
            }
        }
        Node expression = parseExpression();
        if (expression != null) {
            aal.log("QParser", "Statement parsed.");
            return expression;
        }

        if (!soft) error(Language.get("p.statement.no-valid-case"));
        else aal.log("QParser", "Statement not found.");
        return null;
    }

    public Node parseExpression() {
        return EParseAssignment();
    }



    /*
        EXPRESSION PARSING
    */
    private Node EParseAssignment() {
        Node expr = EParseOr();

        if (match(TokenType.ASSIGNOPERATOR) != null) {
            Token equals = previous();
            Node value = EParseAssignment();
            if (expr instanceof VariableNode) {
                return new BinaryOperatorNode(equals, expr, value);
            } else if (expr instanceof FieldReferenceNode) {
                FieldReferenceNode get = (FieldReferenceNode) expr;
                return new FieldSetNode(get.lnode, get.rnode, value);
            } else {
                return new BinaryOperatorNode(equals, expr, value);
            }
            //error(Language.get("p.expr.invalid-assign-target"));
        }
        return expr;
    }

    private Node EParseOr() {
        Node expr = EParseAnd();
        while (match(TokenType.BINARYOPERATOR, new String[] {"or"}) != null) {
            Token operator = previous();
            Node right = EParseAnd();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseAnd() {
        Node expr = EParseEquality();
        while (match(TokenType.BINARYOPERATOR, new String[] {"and"}) != null) {
            Token operator = previous();
            Node right = EParseEquality();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseEquality() {
        Node expr = EParseComparison();
        while ( match(TokenType.BINARYOPERATOR, new String[] {"==", "!=", "is same as", "is"}) != null) {
            Token operator = previous();
            Node right = EParseComparison();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseComparison() {
        Node expr = EParseTerm();
        while ( match(TokenType.BINARYOPERATOR, new String[] {"<", ">", ">=", "<="}) != null) {
            Token operator = previous();
            Node right = EParseTerm();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseTerm() {
        Node expr = EParseFactor();
        while ( match(TokenType.BINARYOPERATOR, new String[] {"+", "-"}) != null) {
            Token operator = previous();
            Node right = EParseFactor();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseFactor() {
        Node expr = EParseUnary();
        while ( match(TokenType.BINARYOPERATOR, new String[] {"/", "*"}) != null) {
            Token operator = previous();
            Node right = EParseUnary();
            expr = new BinaryOperatorNode(operator, expr, right);
        }
        return expr;
    }

    private Node EParseUnary() {
        if (match(TokenType.UNARYOPERATOR, new String[] {
                "not",
                "reference to",
                "out",
                "input",
                "put",
                "exists",
                "notnull",
                "destroy",
                "assert",
                "use",
                "using",
                "block",
                "unlock",
                "deploy",
                "return",
                "throw",
                "var",
                "my",
                "negate"}) != null) {
            Token operator = previous();
            Node right = EParseUnary();
            return new UnaryOperatorNode(operator, right);
        }
        return EParseCall();
    }

    private Node EFinishCall(Node callee) {
        MultiElementNode args = new MultiElementNode();
        if (!getCurrent().t.equals(TokenType.RPAR)) {
            do {
                if (args.nodes.size() >= 255) {
                    error(Language.get("p.expr.argument-len-exceeded"));
                }
                args.addNode(parseExpression());
            } while (match(TokenType.COMMA) != null);
        }
        require(TokenType.RPAR, Language.get("p.common.no-par"));
        return new FunctionCallNode(callee, args);
    }

    private Node EParseCall() {
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
                        Language.get("p.expr.expected-id-after-dot"));
                expr = new FieldReferenceNode(name, expr, new VariableNode(name));
            } else if (match(TokenType.BINARYOPERATOR, new String[] {"of"}) != null) {
                Token name = requireMultiple(new TokenType[]{
                                TokenType.ID,
                                TokenType.LITERALBOOL,
                                TokenType.LITERALNUM,
                                TokenType.LITERALSTRING},
                        Language.get("p.expr.expected-id-after-dot"));
                expr = new FieldReferenceNode(name, new VariableNode(name), expr);
            } else {
                break;
            }
        }
        return expr;
    }

    private Node EParsePrimary() {
        if (match(TokenType.LITERALBOOL  ) != null) return new LiteralBoolNode  (previous());
        if (match(TokenType.LITERALNULL  ) != null) return new LiteralNullNode  (previous());
        if (match(TokenType.LITERALNUM   ) != null) return new LiteralNumNode   (previous());
        if (match(TokenType.LITERALSTRING) != null) return new LiteralStringNode(previous());

        if (match(TokenType.ID) != null) return new VariableNode(previous());

        if (match(TokenType.LPAR) != null)  {
            Node expr = parseExpression();
            require(TokenType.RPAR, Language.get("p.common.no-par"));
            return new GroupNode(expr);
        }

        if (match(TokenType.LSPAR) != null) {
            LiteralListNode list = new LiteralListNode();
            if (!getCurrent().t.equals(TokenType.RSPAR)) {
                do {
                    list.addNode(parseExpression());
                } while (match(TokenType.COMMA) != null);
            }
            require(TokenType.RSPAR, Language.get("p.common.no-par"));
            return list;
        }

        if (match(TokenType.LCPAR) != null) {
            LiteralContainerNode containerNode = new LiteralContainerNode("_anonymous", false,
                    "container");
            if (!getCurrent().t.equals(TokenType.RCPAR)) {
                do {
                    Node node = parseExpression();
                    if (!(node instanceof BinaryOperatorNode &&
                            ((BinaryOperatorNode) node).operator.c.equals("="))) {
                        error(Language.get("p.expr.expected-key-value"));
                        return null;
                    }
                    containerNode.initialize.add(node);
                } while (match(TokenType.COMMA) != null);
            }
            require(TokenType.RCPAR, Language.get("p.common.no-par"));
            return containerNode;
        }

        if (getCurrent().c.equals("anonymous")) {
            pos++;
            return parseAnonymousFunction();
        }
        error(Language.get("p.expr.no-valid-case"));
        return null;
    }


    public Node parseCode() {
        while (pos < tokens.size()) {
            Node node = parseFunction(true);
            if (node != null) {
                scope.addNode(node);
                continue;
            }
            node = parseEvent(true);
            if (node != null) {
                scope.addNode(node);
                continue;
            }
            node = parseContainer(true);
            if (node != null) {
                scope.addNode(node);
                continue;
            }
            node = parseStatement(true);
            if (node != null) {
                scope.addNode(node);
                continue;
            }
            node = parseExpression();
            if (node != null) {
                scope.addNode(node);
            }
        }
        return scope;
    }
}