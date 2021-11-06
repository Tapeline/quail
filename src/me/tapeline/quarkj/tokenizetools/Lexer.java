package me.tapeline.quarkj.tokenizetools;

import me.tapeline.quarkj.tokenizetools.tokens.Token;
import me.tapeline.quarkj.tokenizetools.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final String code;
    private List<Token> tokens = new ArrayList<>();
    private int pos;
    private boolean stop = false;

    public Lexer(String code) {
        this.code = code;
    }

    public List<String> findAll(Pattern r, String sub) {
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
        for (TokenType i : ttval) {
            Pattern regex = Pattern.compile("^\\s*" + i.regex);
            String sub = code.substring(pos);
            if (sub.length() < 1 || sub.equals(" ")) return false;
            List<String> result = findAll(regex, sub);
            if (result != null && result.size() > 0) {
                Token token = new Token(i, result.get(0).trim().replaceAll("&:q", "\""), pos);
                pos += result.get(0).length();
                if (!token.t.equals(TokenType.WHITESPACE) &&
                    !token.t.equals(TokenType.COMMENT)) tokens.add(token);
                return true;
            }
        }
        System.err.println("[QLexer] (X) Exception occured: Unparsable code: `" + code.substring(pos) + "`");
        System.err.println(!code.substring(code.length()-2).equals(" ")?
                "Did you forget the trailing whitespace?" : "");
        stop = true;
        return false;
    }

    public List<Token> lex() {
        while (nextToken()) {}
        return stop? null : tokens;
    }

}
