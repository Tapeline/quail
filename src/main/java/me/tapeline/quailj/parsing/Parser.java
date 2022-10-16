package me.tapeline.quailj.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.block.BlockNode;
import me.tapeline.quailj.parsing.nodes.branching.CatchClause;
import me.tapeline.quailj.parsing.nodes.branching.EventNode;
import me.tapeline.quailj.parsing.nodes.branching.IfNode;
import me.tapeline.quailj.parsing.nodes.branching.TryCatchNode;
import me.tapeline.quailj.parsing.nodes.effect.EffectNode;
import me.tapeline.quailj.parsing.nodes.expression.CallNode;
import me.tapeline.quailj.parsing.nodes.generators.ContainerGeneratorNode;
import me.tapeline.quailj.parsing.nodes.generators.ListGeneratorNode;
import me.tapeline.quailj.parsing.nodes.literals.*;
import me.tapeline.quailj.parsing.nodes.loops.ForNode;
import me.tapeline.quailj.parsing.nodes.loops.LoopNode;
import me.tapeline.quailj.parsing.nodes.loops.ThroughNode;
import me.tapeline.quailj.parsing.nodes.loops.WhileNode;
import me.tapeline.quailj.parsing.nodes.modifiers.ModifierSequence;
import me.tapeline.quailj.parsing.nodes.modifiers.TypeCastNode;
import me.tapeline.quailj.parsing.nodes.operators.*;
import me.tapeline.quailj.parsing.nodes.sequence.TupleNode;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.typing.modifiers.*;
import me.tapeline.quailj.typing.utils.ContainerPreRuntimeContents;
import me.tapeline.quailj.typing.utils.Utilities;
import me.tapeline.quailj.utils.ErrorFormatter;
import static me.tapeline.quailj.lexing.TokenType.*;

public class Parser {

    private List<Token> tokens;
    private String sourceCode;
    private int pos = 0;

    public Parser(String code, List<Token> tokens) {
        this.tokens = tokens;
        this.sourceCode = code;
    }

    private void error(String message) throws Exception {
        throw new Exception(
                ErrorFormatter.formatError(
                        sourceCode,
                        tokens.get(pos).getLine() - 1,
                        tokens.get(pos).getCharacter(),
                        tokens.get(pos).getLength(),
                        message
                )
        );
    }

    private void error(Token token, String message) throws Exception {
        if (token == null)
            throw new Exception(
                    ErrorFormatter.formatError(
                            sourceCode,
                            1,
                           0,
                            1,
                            message
                    )
            );
        else
            throw new Exception(
                    ErrorFormatter.formatError(
                            sourceCode,
                            token.getLine() - 1,
                            token.getCharacter(),
                            token.getLength(),
                            message
                    )
            );
    }

    private boolean reachedEnd() {
        return pos >= tokens.size();
    }

    private Token match(TokenType type) {
        if (!reachedEnd()) {
            int increasePos = 0;
            while (tokens.get(pos + increasePos).getType() == EOL)
                increasePos++;
            Token current = tokens.get(pos + increasePos);
            if (type.equals(current.getType())) {
                pos += increasePos + 1;
                return current;
            }
        }
        return null;
    }

    private Token matchMultiple(TokenType... types) {
        if (!reachedEnd()) {
            int increasePos = 0;
            while (tokens.get(pos + increasePos).getType() == EOL)
                increasePos++;
            Token current = tokens.get(pos + increasePos);
            for (TokenType acceptable : types) {
                if (acceptable.equals(current.getType())) {
                    pos += increasePos;
                    return current;
                }
            }
        }
        return null;
    }

    private Token matchSameLine(TokenType type) {
        if (!reachedEnd()) {
            Token current = tokens.get(pos);
            if (type.equals(current.getType())) {
                pos++;
                return current;
            }
        }
        return null;
    }

    private Token consume() {
        if (!reachedEnd())
            return tokens.get(pos++);
        return null;
    }

    private Token getCurrent() {
        if (!reachedEnd())
            return tokens.get(pos);
        return null;
    }

    private Token getNext() {
        if (pos + 1 < tokens.size())
            return tokens.get(pos + 1);
        return null;
    }

    private Token getPrevious() {
        if (pos - 1 >= 0)
            return tokens.get(pos - 1);
        return null;
    }

    private BlockNode parseBlockUntil(TokenType... until) throws Exception {
        BlockNode block = new BlockNode(getCurrent(), new ArrayList<>());
        match(LCPAR);
        while (true) {
            for (TokenType end : until)
                if (match(end) != null)
                    return block;
            block.nodes.add(parseStatement());
        }
    }

    private Token require(TokenType type, String message) throws Exception {
        Token token = match(type);
        if (token == null)
            error(getCurrent(), message == null?
                    "Expected " + type.toString() + " but none found" :
                    message);
        return token;
    }

    private TupleNode tupleIfNeeded(Node node) {
        if (node instanceof TupleNode)
            return (TupleNode) node;
        TupleNode tupleNode = new TupleNode(getCurrent(), new ArrayList<>());
        tupleNode.character = node.character;
        tupleNode.line = node.line;
        tupleNode.length = node.length;
        return tupleNode;
    }

    /**
     * Returns BlockNode with parsed code
     * or throws an exception if there is
     * a syntax error.
     *
     * @return BlockNode
     */
    public BlockNode parse() throws Exception {
        BlockNode statements = new BlockNode(getCurrent(), new ArrayList<>());
        while (!reachedEnd())
            statements.nodes.add(parseStatement());
        return statements;
    }

    private Node parseStatement() throws Exception {
        Token current = getCurrent();
        if (current == null) return new Node(tokens.get(0));
        TokenType currentType = current.getType();

        if (currentType == LCPAR)
            return parseBlockUntil(RCPAR);

        if (currentType == CONTROL_IF) {
            Token ifToken = consume();
            Node ifBranchCondition = parseExpression(new ExpressionParsingRule());
            BlockNode ifBranchCode = parseBlockUntil(CONTROL_ELSEIF, CONTROL_ELSE, RSPAR);
            IfNode ifNode = new IfNode(ifToken, ifBranchCondition, ifBranchCode);
            if (getPrevious().getType() == RSPAR)
                return ifNode;
            pos--;
            while (getCurrent() != null && getCurrent().getType() == CONTROL_ELSEIF) {
                consume();
                Node elseIfBranchCondition = parseExpression(new ExpressionParsingRule());
                if (elseIfBranchCondition == null) error("Null elseif condition");
                BlockNode elseIfCode = parseBlockUntil(CONTROL_ELSEIF, CONTROL_ELSE, RSPAR);
                ifNode.conditions.add(elseIfBranchCondition);
                ifNode.branches.add(elseIfCode);
                if (getPrevious().getType() == RSPAR)
                    return ifNode;
                pos--;
            }
            if (match(CONTROL_ELSE) != null)
                ifNode.elseBranch = parseBlockUntil(RSPAR);
            return ifNode;
        } else if (match(CONTROL_WHILE) != null) {
            Token token = getPrevious();
            Node condition = parseExpression(new ExpressionParsingRule());
            return new WhileNode(token, condition, parseStatement());
        } else if (match(CONTROL_LOOP) != null) {
            Token token = getPrevious();
            match(LCPAR);
            Node code = parseBlockUntil(RCPAR, CONTROL_STOP_WHEN);
            match(CONTROL_STOP_WHEN);
            Node condition = parseExpression(new ExpressionParsingRule());
            return new LoopNode(token, condition, code);
        } else if (match(CONTROL_THROUGH) != null) {
            Token token = getPrevious();
            Node expr = parseExpression(new ExpressionParsingRule());
            if (!(expr instanceof RangeNode))
                error(token, "Expected range in through loop");
            require(AS, "Expected as after range");
            Token iterator = require(ID, "Expected iterator");
            return new ThroughNode(
                    token,
                    ((RangeNode) expr).start,
                    ((RangeNode) expr).end,
                    ((RangeNode) expr).step,
                    iterator.getLexeme(),
                    parseStatement()
            );
        } else if (match(CONTROL_TRY) != null) {
            Token token = getPrevious();
            Node tryCode = parseBlockUntil(CONTROL_CATCH, RCPAR);
            pos--;
            match(RCPAR);
            List<CatchClause> catchClauses = new ArrayList<>();
            while (match(CONTROL_CATCH) != null) {
                Node exception = null;
                if (match(AS) == null)
                    exception = parseExpression(new ExpressionParsingRule());
                match(AS);
                Token var = require(ID, "Expected id in catch");
                Node catchCode = parseBlockUntil(CONTROL_CATCH, RCPAR);
                pos--;
                match(RCPAR);
                catchClauses.add(new CatchClause(exception, var.getLexeme(), catchCode));
            }
            return new TryCatchNode(token, tryCode, catchClauses);
        } else if (matchMultiple(CONTROL_FOR, CONTROL_EVERY) != null) {
            Token token = getPrevious();
            List<String> iterators = new ArrayList<>();
            do {
                iterators.add(require(ID, "Expected iterator after for/every").getLexeme());
            } while (match(COMMA) != null);
            require(IN, "Expected in in for/every");
            Node iterable = parseExpression(new ExpressionParsingRule());
            return new ForNode(token, iterable, iterators, parseStatement());
        }

        Node n = parseEffect();
        if (n != null) return n;

        n = parseEvent();
        if (n != null) return n;

        n = parseClass();
        if (n != null) return n;

        n = parseFunction();
        if (n != null) return n;

        n = parseExpression(new ExpressionParsingRule());
        if (n != null) return n;

        error("Expected statement, but none found");
        return null;
    }

    private Node parseEffect() throws Exception {
        if (match(EFFECT_RETURN) != null) {
            Token token = getPrevious();
            if (getCurrent() == null || getCurrent().getType() == EOL) 
                return new EffectNode(token, EFFECT_RETURN, new LiteralNull(token));
            return new EffectNode(token, EFFECT_RETURN, parseExpression(new ExpressionParsingRule()));
        } else if (matchMultiple(EFFECT_ASSERT, EFFECT_IMPORT, EFFECT_STRIKE, EFFECT_THROW) != null) {
            Token token = getPrevious();
            if (getCurrent() == null || getCurrent().getType() == EOL)
                error(token, "Expected expression after effect, but got EOL");
            return new EffectNode(token, token.getType(), parseExpression(new ExpressionParsingRule()));
        }
        return null;
    }

    private Node parseEvent() throws Exception {
        if (matchMultiple(CONTROL_WHEN, CONTROL_ON) != null) {
            Token token = getPrevious();
            Node event = parseExpression(new ExpressionParsingRule());
            Token variable = require(ID, "Expected id after listener declaration");
            return new EventNode(token, event, variable.getLexeme());
        }
        return null;
    }

    private Node parseClass() throws Exception {
        if (match(TYPE_CLASS) != null) {
            Token classToken = getPrevious();
            Token className = require(ID, "Expected class name after class keyword");
            Node like = new VariableNode(
                    classToken,
                    "Container",
                    false,
                    false,
                    new ArrayList<>()
            );
            if (match(LIKE) != null)
                like = parseOr(new ExpressionParsingRule());
            match(LCPAR);
            HashMap<String, Node> contents = new HashMap<>();
            HashMap<String, LiteralFunction> methods = new HashMap<>();
            List<Node> initialize = new ArrayList<>();
            while (match(RCPAR) == null) {
                Node expr = parseStatement();
                if (expr instanceof AssignNode)
                    contents.put(((AssignNode) expr).variable, ((AssignNode) expr).value);
                else if (expr instanceof LiteralFunction)
                    methods.put(((LiteralFunction) expr).name, ((LiteralFunction) expr));
                else
                    initialize.add(expr);
            }
            return new LiteralClass(classToken, className.getLexeme(),
                    like, contents, methods, initialize);
        }
        return null;
    }

    private Node parseFunction() throws Exception {

        Token token = matchMultiple(OVERRIDE, TYPE_FUNCTION, TYPE_METHOD, SETS, GETS, CONSTRUCTOR);
        if (token == null) return null;
        switch (token.getType()) {
            case TYPE_FUNCTION: {
                Token name = require(ID, "Expected id");
                Node args = parsePrimary(new ExpressionParsingRule());
                return new LiteralFunction(name, name.getLexeme(),
                        tupleIfNeeded(args), parseStatement(), false);
            }
            case OVERRIDE: {
                Token name = consume();
                if (name == null) error("Unexpected end");
                String functionName = Utilities.opToString.get(name.getType());
                if (name.getLexeme().equals("index"))
                    functionName = "_index";
                else if (name.getLexeme().equals("index set"))
                    functionName = "_indexset";
                else if (name.getLexeme().equals("call"))
                    functionName = "_call";

                Node args = parsePrimary(new ExpressionParsingRule());
                return new LiteralFunction(name, functionName,
                        tupleIfNeeded(args), parseStatement(), false);
            }
            case GETS: {
                Token var = consume();
                Node args = parsePrimary(new ExpressionParsingRule());
                return new LiteralFunction(var, "_get_" + var.getLexeme(),
                        tupleIfNeeded(args), parseStatement(), false);
            }
            case SETS: {
                Token var = consume();
                Node args = parsePrimary(new ExpressionParsingRule());
                return new LiteralFunction(var, "_set_" + var.getLexeme(),
                        tupleIfNeeded(args), parseStatement(), false);
            }
            case CONSTRUCTOR: {
                Node args = parsePrimary(new ExpressionParsingRule());
                return new LiteralFunction(token, "_constructor",
                        tupleIfNeeded(args), parseStatement(), false);
            }
        }
        return null;
    }

    private Node parseExpression(ExpressionParsingRule rule) throws Exception {
        return parseAssignment(rule);
    }

    private Node parseAssignment(ExpressionParsingRule rule) throws Exception {
        Node left = parseOr(rule);

        if (match(ASSIGN) != null) {
            Token equals = getPrevious();
            Node value = parseAssignment(rule);
            if (left instanceof VariableNode) {
                return new AssignNode(equals, ((VariableNode) left).id, value);
            } else if (left instanceof FieldReferenceNode) {
                return new FieldSetNode(
                        equals,
                        ((FieldReferenceNode) left).object,
                        ((FieldReferenceNode) left).field,
                        value
                );
            } else if (left instanceof IndexingNode) {
                return new IndexSetNode(
                        getCurrent(),
                        ((IndexingNode) left).object,
                        ((IndexingNode) left).index,
                        value
                );
            } else {
                error(equals, "Unknown assign operation");
            }

        } else if (matchMultiple(
                SHORT_DIVIDE,
                SHORT_MODULO,
                SHORT_INTDIV,
                SHORT_POWER,
                SHORT_MINUS,
                SHORT_MULTIPLY,
                SHORT_PLUS,
                SHORT_SHIFT_LEFT,
                SHORT_SHIFT_RIGHT
        ) != null) {
            Token shortOp = getPrevious();
            Node value = parseOr(rule);
            if (left instanceof VariableNode) {
                return new AssignNode(shortOp, ((VariableNode) left).id,
                         new BinaryOperatorNode(
                                 getPrevious(),
                                 shortToNormal.get(getPrevious().getType()),
                                 left,
                                 value
                         ));
            } else if (left instanceof FieldReferenceNode) {
                return new FieldSetNode(
                        shortOp,
                        ((FieldReferenceNode) left).object,
                        ((FieldReferenceNode) left).field,
                        new BinaryOperatorNode(
                                getPrevious(),
                                shortToNormal.get(getPrevious().getType()),
                                left,
                                value
                        )
                );
            } else if (left instanceof IndexingNode) {
                return new IndexSetNode(
                        getCurrent(),
                        ((IndexingNode) left).object,
                        ((IndexingNode) left).index,
                        new BinaryOperatorNode(
                                getPrevious(),
                                shortToNormal.get(getPrevious().getType()),
                                left,
                                value
                        )
                );
            } else {
                error(shortOp, "Unknown short assign operation");
            }
        }

        return left;
    }

    private Node parseOr(ExpressionParsingRule rule) throws Exception {
        Node left = parseAnd(rule);
        while (match(OR) != null) {
            left = new BinaryOperatorNode(getPrevious(), OR, left, parseAnd(rule));
        }
        return left;
    }

    private Node parseAnd(ExpressionParsingRule rule) throws Exception {
        Node left = parseEquality(rule);
        while (match(AND) != null) {
            left = new BinaryOperatorNode(getPrevious(), AND, left, parseEquality(rule));
        }
        return left;
    }

    private Node parseEquality(ExpressionParsingRule rule) throws Exception {
        Node left = parseComparison(rule);
        while (matchMultiple(EQUALS, NOT_EQUALS, INSTANCEOF, IN) != null) {
            left = new BinaryOperatorNode(getPrevious(), getPrevious().getType(),
                    left, parseComparison(rule));
        }
        return left;
    }

    private Node parseComparison(ExpressionParsingRule rule) throws Exception {
        Node left = parseRange(rule);
        if (rule.excludeComparison) return left;

        while (matchMultiple(GREATER, GREATER_EQUAL, LESS,
                LESS_EQUAL, SHIFT_LEFT, SHIFT_RIGHT) != null) {
            left = new BinaryOperatorNode(getPrevious(), getPrevious().getType(),
                    left, parseRange(rule));
        }
        return left;
    }

    private Node parseRange(ExpressionParsingRule rule) throws Exception {
        Node left = parseTerm(rule);
        if (rule.excludeRange) return left;

        if (matchMultiple(RANGE, RANGE_INCLUDE) != null) {
            boolean include = false;
            Node end = null, step = null;
            Token rangeToken = getPrevious();
            include = rangeToken.getType() == RANGE_INCLUDE;
            ExpressionParsingRule rangeRule = new ExpressionParsingRule();
            rangeRule.excludeRange = true;
            end = parseOr(rangeRule);
            if (match(RANGE) != null)
                step = parseOr(rangeRule);
            return new RangeNode(rangeToken, left, end, step, include);
        }
        return left;
    }

    private Node parseTerm(ExpressionParsingRule rule) throws Exception {
        Node left = parseFactor(rule);
        while (matchMultiple(PLUS, MINUS) != null) {
            left = new BinaryOperatorNode(getPrevious(), getPrevious().getType(),
                    left, parseFactor(rule));
        }
        return left;
    }

    private Node parseFactor(ExpressionParsingRule rule) throws Exception {
        Node left = parsePower(rule);
        while (matchMultiple(MULTIPLY, DIVIDE, INTDIV, MODULO) != null) {
            left = new BinaryOperatorNode(getPrevious(), getPrevious().getType(),
                    left, parsePower(rule));
        }
        return left;
    }

    private Node parsePower(ExpressionParsingRule rule) throws Exception {
        Node left = parseUnary(rule);
        while (match(POWER) != null) {
            left = new BinaryOperatorNode(getPrevious(), POWER,
                    left, parseUnary(rule));
        }
        return left;
    }

    private Node parseUnary(ExpressionParsingRule rule) throws Exception {
        if (matchMultiple(
                NOT,
                MINUS,
                HASH
        ) != null) {
            return new UnaryOperatorNode(getPrevious(), getPrevious().getType(), parseUnary(rule));
        }
        return parseCall(rule);
    }

    private Node parseCall(ExpressionParsingRule rule) throws Exception {
        Node left = parsePrimary(rule);
        while (true) {
            if (matchSameLine(TokenType.LPAR) != null) {
                left = finishCall(left);
            } else if (matchSameLine(DOT) != null) {
                Token dot = getPrevious();
                Token name = require(ID, "Expected id");
                left = new FieldReferenceNode(dot, left, name.getLexeme());
            } else {
                break;
            }
        }

        while (matchSameLine(TokenType.LSPAR) != null) {
            Token leftBracket = getPrevious();
            ExpressionParsingRule indexationRule = new ExpressionParsingRule();
            indexationRule.excludeRange = true;
            Node start = null, end = null, step = null;
            boolean isSubscript = true;
            if (match(RANGE) != null) { // [:x] [:x:y] [::x]
                if (match(RANGE) != null) {
                    step = parseOr(indexationRule); // [::x]
                } else {
                    end = parseOr(indexationRule); // [:x]
                    if (match(RANGE) != null) {
                        step = parseOr(indexationRule); // [:x:y]
                    }
                }
            } else { // [x:y] [x::y] [x:y:z] [x:+y] [x:+y:z]
                start = parseOr(indexationRule);
                if (matchMultiple(RANGE, RANGE_INCLUDE) != null) {
                    if (match(RANGE) != null) {
                        step = parseOr(indexationRule); // [x::y]
                    } else if (match(RSPAR) != null) {
                        pos--; // [x:]
                    } else {
                        end = parseOr(indexationRule); // [x:y] [x:+y]
                        if (matchMultiple(RANGE) != null) {
                            step = parseOr(indexationRule); // [x:y:z] [x:+y:z]
                        }
                    }
                } else isSubscript = false;
            }
            require(RSPAR, "Expected ] to close indexing");
            if (isSubscript)
                left = new SubscriptNode(leftBracket, left, start, end, step);
            else
                left = new IndexingNode(leftBracket, left, start);
        }
        return left;
    }

    private Node finishCall(Node callee) throws Exception {
        Token leftBracket = getPrevious();
        List<Node> arguments = new ArrayList<>();
        HashMap<String, Node> keywordArguments = new HashMap<>();
        if (match(RPAR) == null) {
            do {
                Node argument = parseExpression(new ExpressionParsingRule());
                if (argument instanceof AssignNode)
                    keywordArguments.put(((AssignNode) argument).variable, ((AssignNode) argument).value);
                else
                    arguments.add(argument);
            } while (match(TokenType.COMMA) != null);
        }
        require(TokenType.RPAR, "Expected closing bracket");
        return new CallNode(leftBracket, arguments, callee, keywordArguments);
    }

    private Node parseModifiers() throws Exception {
        if (matchMultiple(
                MOD_ANYOF,
                MOD_FINAL,
                MOD_LOCAL,
                MOD_REQUIRE,
                MOD_STATIC,
                TYPE_BOOL,
                TYPE_CONTAINER,
                TYPE_FUNC,
                TYPE_LIST,
                TYPE_NUM,
                TYPE_OBJECT,
                TYPE_STRING,
                TYPE_VOID
            ) == null)
            return new ModifierSequence(getCurrent(), new ArrayList<>());

        pos--;
        List<VariableModifier> modifiers = new ArrayList<>();
        Token modifierToken = null;
        while (true) {
            modifierToken = getCurrent();
            if (match(MOD_STATIC) != null) {
                modifiers.add(new StaticModifier());
            } else if (match(MOD_LOCAL) != null) {
                modifiers.add(new LocalModifier());
            } else if (match(MOD_FINAL) != null) {
                modifiers.add(new FinalModifier());
            } else if (match(MOD_REQUIRE) != null) {
                modifiers.add(new RequireModifier());
            } else if (matchMultiple(
                    TYPE_BOOL,
                    TYPE_CONTAINER,
                    TYPE_FUNC,
                    TYPE_LIST,
                    TYPE_NUM,
                    TYPE_OBJECT,
                    TYPE_STRING,
                    TYPE_VOID
            ) != null) {
                Token modifier = getPrevious();
                /*
                * string s = "123.43"
                * out(num(s) + 13.62)
                * */
                if (match(LPAR) != null) {
                    Token leftBracket = getPrevious();
                    Node castValue = parseExpression(new ExpressionParsingRule());
                    require(RPAR, "Expected ) to close typecast");
                    return new TypeCastNode(leftBracket, modifier.getType(), castValue);
                }
                if (modifier.getType() == TYPE_OBJECT && match(LESS) != null) {
                    ExpressionParsingRule rule = new ExpressionParsingRule();
                    rule.excludeComparison = true;
                    Node requiredClass = parseExpression(rule);
                    require(GREATER, "Expected > to close object<Class>");
                    modifiers.add(new TypeModifier(requiredClass));
                } else {
                    modifiers.add(new TypeModifier(modifier.getType()));
                }
            } else if (match(MOD_ANYOF) != null) {
                List<VariableModifier> anyOfModifiers = new ArrayList<>();
                if (match(MOD_STATIC) != null) {
                    anyOfModifiers.add(new StaticModifier());
                } else if (match(MOD_LOCAL) != null) {
                    anyOfModifiers.add(new LocalModifier());
                } else if (match(MOD_FINAL) != null) {
                    anyOfModifiers.add(new FinalModifier());
                } else if (match(MOD_REQUIRE) != null) {
                    anyOfModifiers.add(new RequireModifier());
                } else if (matchMultiple(
                        TYPE_BOOL,
                        TYPE_CONTAINER,
                        TYPE_FUNC,
                        TYPE_LIST,
                        TYPE_NUM,
                        TYPE_OBJECT,
                        TYPE_STRING,
                        TYPE_VOID
                ) != null) {
                    Token modifier = getPrevious();
                    if (modifier.getType() == TYPE_OBJECT && match(LESS) != null) {
                        ExpressionParsingRule rule = new ExpressionParsingRule();
                        rule.excludeComparison = true;
                        Node requiredClass = parseExpression(rule);
                        require(GREATER, "Expected > to close object<Class>");
                        anyOfModifiers.add(new TypeModifier(requiredClass));
                    } else {
                        anyOfModifiers.add(new TypeModifier(modifier.getType()));
                    }
                }
                modifiers.add(new AnyOfModifier(anyOfModifiers));
            } else break;
        }
        if (getCurrent().getType() == ID && getNext().getType() == LPAR) {
            Token name = match(ID);
            Token leftParent = match(LPAR);
            boolean isStatic = false;
            for (VariableModifier modifier : modifiers)
                if (modifier instanceof StaticModifier) {
                    isStatic = true;
                    break;
                }
            Node args = parsePrimary(new ExpressionParsingRule());
            return new LiteralFunction(name, name.getLexeme(),
                    tupleIfNeeded(args), parseStatement(), isStatic);
        }
        return new ModifierSequence(modifierToken, modifiers);
    }

    private Node parsePrimary(ExpressionParsingRule rule) throws Exception {
        if (match(LITERAL_FALSE) != null) return new LiteralBool(getPrevious(), false);
        if (match(LITERAL_TRUE) != null) return new LiteralBool(getPrevious(), true);
        if (match(LITERAL_NULL) != null) return new LiteralNull(getPrevious());
        if (match(LITERAL_NUM) != null) return new LiteralNum(getPrevious(),
                Double.parseDouble(getPrevious().getLexeme()));
        if (match(LITERAL_STR) != null) return new LiteralString(getPrevious(),
                getPrevious().getLexeme().substring(1, getPrevious().getLexeme().length() - 1));

        Node mods = parseModifiers();
        if (mods instanceof TypeCastNode) return mods;
        if (!(mods instanceof ModifierSequence))
            error("Unknown error while parsing modifiers");
        List<VariableModifier> modifiers = ((ModifierSequence) mods).modifiers;

        if (match(ID) != null) {
            Token variable = getPrevious();
            if (match(CONSUMER) != null) {
                return new VariableNode(variable, variable.getLexeme(),
                        true, false, modifiers);
            } else if (match(KWARG_CONSUMER) != null) {
                return new VariableNode(variable, variable.getLexeme(),
                        false, true, modifiers);
            } else return new VariableNode(variable, variable.getLexeme(),
                    false, false, modifiers);
        }

        if (match(LPAR) != null) {
            Token leftParent = getPrevious();
            if (match(RPAR) != null)
                return new TupleNode(getPrevious(), new ArrayList<>());

            Node value = parseExpression(rule);

            if (match(COMMA) != null) {
                List<Node> tupleNodes = new ArrayList<>();
                do {
                    tupleNodes.add(parseExpression(rule));
                } while (match(COMMA) != null);
                require(RPAR, "Expected ) to close tuple");

                if (match(LAMBDA_ARROW) != null) {
                    Token arrow = getPrevious();
                    return new LiteralFunction(arrow, arrow.getLexeme(),
                            new TupleNode(leftParent, tupleNodes), parseStatement(), false);
                }
                return new TupleNode(leftParent, tupleNodes);
            }
            require(RPAR, "Expected ) to close tuple");
            if (match(LAMBDA_ARROW) != null) {
                Token arrow = getPrevious();
                return new LiteralFunction(arrow, arrow.getLexeme(),
                        new TupleNode(leftParent, Arrays.asList(value)),
                        parseStatement(), false);
            }
            return value;
        }

        if (match(LSPAR) != null) {
            Token leftBracket = getPrevious();
            Node expr = parseExpression(rule);
            if (match(CONTROL_FOR) != null) {
                Node condition = null, fallback = null;
                List<String> iterators = new ArrayList<>();
                do {
                    iterators.add(require(ID, "Expected iterator after for").getLexeme());
                } while (match(COMMA) != null);
                require(IN, "Expected in after iterator in for");
                Node iterable = parseExpression(rule);
                if (match(CONTROL_IF) != null) {
                    condition = parseExpression(rule);
                    if (match(CONTROL_ELSE) != null)
                        fallback = parseExpression(rule);
                }
                require(RSPAR, "Expected ] to close generator");
                return new ListGeneratorNode(
                        leftBracket,
                        expr,
                        iterators,
                        iterable,
                        condition,
                        fallback
                );
            }

            List<Node> listContents = new ArrayList<>();
            listContents.add(expr);
            while (match(COMMA) != null)
                listContents.add(parseExpression(rule));

            return new LiteralList(leftBracket, listContents);
        }

        if (match(LCPAR) != null) {
            Token leftBracket = getPrevious();
            Node key = parseOr(rule);
            require(ASSIGN, "Expected key=value pair");
            Node value = parseOr(rule);
            if (match(CONTROL_FOR) != null) {
                Node condition = null, fallback = null;
                List<String> iterators = new ArrayList<>();
                do {
                    iterators.add(require(ID, "Expected iterator after for").getLexeme());
                } while (match(COMMA) != null);
                require(IN, "Expected in after iterator in for");
                Node iterable = parseExpression(rule);
                if (match(CONTROL_IF) != null) {
                    condition = parseExpression(rule);
                    if (match(CONTROL_ELSE) != null)
                        fallback = parseExpression(rule);
                }
                require(RCPAR, "Expected } to close generator");
                return new ContainerGeneratorNode(
                        leftBracket,
                        key,
                        value,
                        iterators,
                        iterable,
                        condition,
                        fallback
                );
            }

            List<Node> keys = new ArrayList<>();
            HashMap<Integer, Node> values = new HashMap<>();
            keys.add(key);
            values.put(0, value);
            while (match(COMMA) != null) {
                Node k = parseOr(rule);
                require(ASSIGN, "Expected key=value pair");
                Node v = parseOr(rule);
                keys.add(k);
                values.put(keys.size() - 1, v);
            }

            return new LiteralContainer(leftBracket, new ContainerPreRuntimeContents(keys, values));
        }

        if (match(ANONYMOUS) != null) {
            Token declaration = getPrevious();
            List<Node> arguments = new ArrayList<>();
            Token leftParent = require(LPAR, "Expected ( to open arguments");
            do {
                arguments.add(parseExpression(rule));
            } while (match(COMMA) != null);
            require(RPAR, "Expected ) to close arguments");
            return new LiteralFunction(declaration, "->",
                    new TupleNode(leftParent, arguments), parseStatement(), false);
        }

        error(getCurrent(), "Invalid expression");
        return null;
    }


}
