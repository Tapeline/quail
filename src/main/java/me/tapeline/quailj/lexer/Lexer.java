package me.tapeline.quailj.lexer;

import javafx.scene.control.SeparatorMenuItem;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final String code;
    private List<Token> tokens = new ArrayList<>();
    private int pos = 0;

    public Lexer(String code) {
        this.code = code;
    }

    public static List<String> findAll(Pattern r, String sub) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = r.matcher(sub);
        while (m.find()) {
            allMatches.add(m.group());
        }
        return allMatches;
    }

    public static String stripLeading(String s) {
        int pos = 0;
        int size = s.length();
        while (true) {
            if (pos == size) break;
            if (s.charAt(pos) == ' ') {
                s = s.substring(pos + 1);
            } else {
                break;
            }
            size = s.length();
            pos++;
        }
        return s;
    }

    public boolean nextToken() throws RuntimeStriker {
        if (pos > code.length())
            return false;
        List<TokenType> ttval = TokenType.tokenTypeList;
        for (TokenType i : ttval) {
            Pattern regex = Pattern.compile("^\\s*" + i.regex);
            String sub = code.substring(pos);
            if (sub.length() < 1 || sub.equals(" ")) return false;
            List<String> result = findAll(regex, sub);
            if (result != null && result.size() > 0) {
                String content = result.get(0).trim().replaceAll("\"", "");
                Token token = new Token(i.equals(TokenType.WORDBINARYOPERATOR) ? TokenType.BINARYOPERATOR : i,
                        content.replaceAll("&q;", "\""), pos);
                pos += result.get(0).length();
                if (    !token.t.equals(TokenType.WHITESPACE) &&
                        !token.t.equals(TokenType.COMMENT) &&
                        !(token.c.equals("") && token.t != TokenType.LITERALSTRING)
                        && !token.t.equals(TokenType.SEMICOLON)) tokens.add(token);
                return true;
            }
        }
        throw new RuntimeStriker("Failed to lex code at " + code.substring(pos));
    }

    public List<Token> lex() throws RuntimeStriker {
        while (nextToken()) {
        }
        return tokens;
    }

    public List<Token> lexAndFix() throws RuntimeStriker {
        lex();
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).t.equals(TokenType.ID)) {
                switch (tokens.get(i).c) {
                    case "true":
                        tokens.set(i, new Token(TokenType.LITERALBOOL, "true", tokens.get(i).p));
                        break;
                    case "false":
                        tokens.set(i, new Token(TokenType.LITERALBOOL, "false", tokens.get(i).p));
                        break;
                    case "null":
                        tokens.set(i, new Token(TokenType.LITERALNULL, "null", tokens.get(i).p));
                        break;
                }
            } else if (Arrays.asList(
                    "should have", "should be", "should now be",
                    "should now be set", "should be set").contains(tokens.get(i).c)) {
                tokens.set(i, new Token(TokenType.BINARYOPERATOR, "=", tokens.get(i).p));
            } else if (tokens.get(i).t.equals(TokenType.BINARYOPERATOR)) {
                Token t = tokens.get(i);
                t.c = t.c.replaceAll("plus", "\\+");
                t.c = t.c.replaceAll("minus", "\\-");
                t.c = t.c.replaceAll("in power of", "\\^");
                t.c = t.c.replaceAll("multiplied by", "\\*");
                t.c = t.c.replaceAll("divided by", "\\/");
                t.c = t.c.replaceAll("is greater than", "\\>");
                t.c = t.c.replaceAll("is less than", "\\<");
                t.c = t.c.replaceAll("is greater or equal to", "\\>=");
                t.c = t.c.replaceAll("is less or equal to", "\\<=");
                t.c = t.c.replaceAll("&&", "and");
                t.c = t.c.replaceAll("\\|\\|", "or");
                t.t = TokenType.BINARYOPERATOR;
                tokens.set(i, t);
            }
        }
        return tokens;
    }
}