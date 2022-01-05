package me.tapeline.quailj.tokenizetools;

import me.tapeline.quailj.debugtools.AALFrame;
import me.tapeline.quailj.debugtools.AdvancedActionLogger;
import me.tapeline.quailj.tokenizetools.tokens.Token;
import me.tapeline.quailj.tokenizetools.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final String code;
    private List<Token> tokens = new ArrayList<>();
    private int pos;
    private boolean stop = false;
    private AdvancedActionLogger aal;

    public Lexer(String code, AdvancedActionLogger aal) {
        this.code = code;
        this.aal = aal;
    }

    public static List<String> findAll(Pattern r, String sub) {
        List<String> allMatches = new ArrayList<String>();
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

    public boolean nextToken() {
        if (pos > code.length())
            return false;
        List<TokenType> ttval = TokenType.tokenTypeList;
        aal.log("QLexer", "Analyzing code. Pos " + pos + " . Tokens " + tokens.size());
        for (TokenType i : ttval) {
            aal.log("QLexer", "Trying " + i.toString());
            Pattern regex = Pattern.compile("^\\s*" + i.regex);
            String sub = code.substring(pos);
            if (sub.length() < 1 || sub.equals(" ")) return false;
            List<String> result = findAll(regex, sub);
            if (result != null && result.size() > 0) {
                Token token = new Token(i, result.get(0).trim().replaceAll("&q;", "\""), pos);
                pos += result.get(0).length();
                if (!token.t.equals(TokenType.WHITESPACE) &&
                    !token.t.equals(TokenType.COMMENT)) tokens.add(token);
                aal.log("QLexer", "Found ", token);
                return true;
            }
        }
        System.err.println("[QLexer] (X) Exception occured: Unparsable code: `" + code.substring(pos) + "`");
        aal.err("QLexer", "Exception occured: Unparsable code: `" + code.substring(pos) + "`");
        new AALFrame(aal);
        stop = true;
        Scanner sc = new Scanner(System.in);
        sc.next();
        return false;
    }

    public List<Token> lex() {
        aal.log("QLexer", "Begin lexing process...");
        while (nextToken()) {}
        return stop? null : tokens;
    }

    public List<Token> fixBooleans() {
        aal.log("QLexer", "Fixing values...");
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
            }
        }
        return tokens;
    }

}
