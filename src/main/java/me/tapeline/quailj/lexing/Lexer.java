package me.tapeline.quailj.lexing;

import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.utils.ErrorFormatter;

import java.util.ArrayList;
import java.util.List;

import static me.tapeline.quailj.lexing.TokenType.*;

public class Lexer {

    private final String sourceCode;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int startOfCurrentLine = 0;
    private int line = 1;

    public Lexer(String code) {
        sourceCode = code;
    }

    private void error(String message) throws ParserException {
        throw new ParserException(
                ErrorFormatter.formatError(
                        sourceCode,
                        line - 1,
                        start,
                        current - start,
                        "LexerError",
                        message
                )
        );
    }

    private boolean reachedEnd() {
        return current >= sourceCode.length();
    }

    private char next() {
        return sourceCode.charAt(current++);
    }

    private void addToken(TokenType type) {
        String text = sourceCode.substring(start, current);
        tokens.add(new Token(type, text, line, start - startOfCurrentLine, current - start));
    }

    private void addMatrixToken(TokenType type) {
        String text = sourceCode.substring(start, current);
        tokens.add(new Token(MATRIX_MOD,
                type, text, line, start - startOfCurrentLine, current - start));
    }

    private void addArrayToken(TokenType type) {
        String text = sourceCode.substring(start, current);
        tokens.add(new Token(ARRAY_MOD,
                type, text, line, start - startOfCurrentLine, current - start));
    }

    private void addEmbedToken(String code) {
        Token token = new Token(JAVA_EMBED, code, line, start - startOfCurrentLine, current - start);
        tokens.add(token);
    }

    private boolean match(char expected) {
        if (reachedEnd()) return false;
        if (sourceCode.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private boolean match(String expected) {
        if (reachedEnd()) return false;

        if (!sourceCode.substring(current).startsWith(expected)) return false;

        current += expected.length();
        return true;
    }

    private char peek() {
        if (reachedEnd()) return '\0';
        return sourceCode.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= sourceCode.length()) return '\0';
        return sourceCode.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void addBinaryOp(TokenType type) {
        if (match('=')) {
            String text = sourceCode.substring(start, current);
            switch (type) {
                case PLUS: addToken(SHORT_PLUS); return;
                case MINUS: addToken(SHORT_MINUS); return;
                case MULTIPLY: addToken(SHORT_MULTIPLY); return;
                case DIVIDE: addToken(SHORT_DIVIDE); return;
                case INTDIV: addToken(SHORT_INTDIV); return;
                case MODULO: addToken(SHORT_MODULO); return;
                case POWER: addToken(SHORT_POWER); return;
                case SHIFT_LEFT: addToken(SHORT_SHIFT_LEFT); return;
                case SHIFT_RIGHT: addToken(SHORT_SHIFT_RIGHT); return;
            }
        }
        addToken(type);
    }

    /**
     * Returns list of scanned tokens or
     * throws an exception if there is a
     * syntax error.
     *
     * @return List<Token>
     */
    public List<Token> scan() throws ParserException {
        while (!reachedEnd()) {
            start = current;
            scanToken();
        }
        return tokens;
    }

    private void scanToken() throws ParserException {
        char c = next();
        switch (c) {
            case '(': addToken(LPAR); break;
            case ')': addToken(RPAR); break;
            case '{':
                if (match("...}"))
                    addToken(KWARG_CONSUMER);
                else
                    scanCurlyBrace(); //addToken(LCPAR);
                break;
            case '}': addToken(RCPAR); break;
            case '[': scanBracket(); break; //addToken(LSPAR); break;
            case ']': addToken(RSPAR); break;

            case ',': addToken(COMMA); break;

            case '\'':
                match('s');
                addToken(DOT);
                break;
            case '.':
                if (match(".."))
                    addToken(CONSUMER);
                else
                    addToken(DOT);
                break;

            case '-':
                if (match('>'))
                    addToken(LAMBDA_ARROW);
                else
                    addBinaryOp(MINUS);
                break;
            case '+': addBinaryOp(PLUS); break;
            case '/':
                if (match('/'))
                    addBinaryOp(INTDIV);
                else
                    addBinaryOp(DIVIDE);
                break;
            case '*': addBinaryOp(MULTIPLY); break;
            case '%': addBinaryOp(MODULO); break;
            case '^': addBinaryOp(POWER); break;
            case '>':
                if (match('>'))
                    addBinaryOp(SHIFT_RIGHT);
                else if (match('='))
                    addToken(GREATER_EQUAL);
                else
                    addToken(GREATER);
                break;
            case '<':
                if (match('<'))
                    addBinaryOp(SHIFT_LEFT);
                else if (match('='))
                    addToken(LESS_EQUAL);
                else
                    addToken(LESS);
                break;
            case '=':
                if (match('='))
                    addToken(EQUALS);
                else
                    addToken(ASSIGN);
                break;
            case '!':
                if (match('='))
                    addToken(NOT_EQUALS);
                else
                    addToken(NOT);
                break;
            case '&':
                if (match('&'))
                    addToken(AND);
                else
                    error("Unknown lexeme &. Did you mean &&?");
                break;
            case '|':
                if (match('|'))
                    addToken(OR);
                else
                    addToken(PILLAR);
                break;
            case ':':
                if (match('+'))
                    addToken(RANGE_INCLUDE);
                else
                    addToken(RANGE);
                break;
            case '#':
                if (match('#'))
                    addToken(HASH);
                else
                    scanComment();
                break;
            case ';':
            case '\n':
                addToken(EOL);
                line++;
                startOfCurrentLine = current;
                break;
            case '"': scanString(); break;
            case ' ':
                if (match("   "))
                    addToken(TAB);
                break;
            case '\r':
                break;
            case '\t':
                addToken(TAB);
                break;
            default:
                if (isDigit(c))
                    scanNumber();
                else if (isAlpha(c))
                    scanId();
                else
                    error("Unexpected character " + c);
                break;
        }
    }

    private void scanJava() throws ParserException {
        boolean isInsideString = false;
        int curlyFolding = 0;
        boolean blockBegan = false;
        boolean escape = false;

        error("Java instant-embeds aren't implemented yet");

        StringBuilder code = new StringBuilder();

        while (!reachedEnd()) {
            char c = next();
            if (curlyFolding == 0 && blockBegan) {
                code.deleteCharAt(code.length() - 1);
                break;
            }
            if (blockBegan)
                code.append(c);
            if (escape) {
                escape = false;
                continue;
            }
            if (c == '{' && !isInsideString) {
                curlyFolding++;
                blockBegan = true;
            } else if (c == '}' && !isInsideString)
                curlyFolding--;
            else if (c == '"')
                isInsideString = !isInsideString;
            else if (c == '\\')
                escape = true;
        }

        addEmbedToken(code.toString());
    }

    private void scanBracket() {
        String currentOp = null;
        for (String op : ops)
            if (sourceCode.substring(current).startsWith(op + "]")) {
                currentOp = op;
                break;
            }
        if (currentOp == null)
            addToken(LSPAR);
        else {
            match(currentOp + "]");
            addArrayToken(stringToOps.get(currentOp));
        }
    }

    private void scanCurlyBrace() {
        String currentOp = null;
        for (String op : ops)
            if (sourceCode.substring(current).startsWith(op + "}")) {
                currentOp = op;
                break;
            }
        if (currentOp == null)
            addToken(LCPAR);
        else {
            match(currentOp + "}");
            addMatrixToken(stringToOps.get(currentOp));
        }
    }


    private void scanComment() {
        while (peek() != '\n' && !reachedEnd())
            next();
    }

    private void scanString() throws ParserException {
        while (peek() != '"' && !reachedEnd()) {
            if (peek() == '\n') {
                line++;
                startOfCurrentLine = current;
            }
            next();
        }
        if (reachedEnd()) {
            error("Unexpected string end");
            return;
        }
        next();
        addToken(LITERAL_STR);
    }

    private void scanNumber() {
        while (isDigit(peek())) next();

        if (peek() == '.' && isDigit(peekNext())) {
            next();
            while (isDigit(peek())) next();
        }

        addToken(LITERAL_NUM);
    }

    private void scanId() throws ParserException {
        while (isAlphaNumeric(peek())) next();
        String text = sourceCode.substring(start, current);
        if (text.equals("java")) {
            scanJava();
        } else {
            TokenType type = keywords.get(text);
            if (type == null) type = ID;
            addToken(type);
        }
    }

}
