package me.tapeline.quailj.preprocessor;

import me.tapeline.quailj.utils.Pair;
import org.apache.commons.io.FileUtils;
import sun.reflect.misc.FieldUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Preprocessor {

    public List<String> defined = new ArrayList<>();
    public HashMap<String, String> aliases = new HashMap<>();
    public Pair<List<Directive>, String> resolveDirectives(String code) {
        List<Directive> directives = new ArrayList<>();
        String[] lines = code.split("\n");
        String newCode = "";
        int line = 0;
        while (line < lines.length) {
            if (lines[line].startsWith("#:")) {
                String directed = lines[line].substring(2);
                while (directed.endsWith("\\")) {
                    directed += lines[line++];
                }
                if (directed.startsWith("alias")) {
                    directed = directed.substring(6 + 1);
                    String alias = "";
                    int pos = 0;
                    boolean escape = false;
                    while (pos < directed.length()) {
                        if (directed.charAt(pos) == '"' && !escape) break;
                        if (escape) escape = false;
                        if (directed.charAt(pos) == '\\') {
                            escape = true;
                            pos++;
                            continue;
                        }
                        alias += directed.charAt(pos);
                        pos++;
                    }
                    String aliasTo = directed.substring(pos + 2);
                    directives.add(new Directive(Directive.Type.ALIAS, alias, aliasTo));
                } else if (directed.startsWith("define")) {
                    directives.add(new Directive(Directive.Type.DEFINE, directed.substring(7)));
                } else if (directed.startsWith("put")) {
                    directed = directed.substring(4 + 1);
                    String path = "";
                    int pos = 0;
                    boolean escape = false;
                    while (pos < directed.length()) {
                        if (directed.charAt(pos) == '"' && !escape) break;
                        if (escape) escape = false;
                        if (directed.charAt(pos) == '\\') {
                            escape = true;
                            pos++;
                            continue;
                        }
                        path += directed.charAt(pos);
                        pos++;
                    }
                    directives.add(new Directive(Directive.Type.PUT, path));
                /*} else if (directed.startsWith("ifdef")) {
                    directed = directed.substring(5 + 1);
                    String definition = directed.substring(0, directed.indexOf(" "));
                    directed = directed.substring(definition.length());
                    directives.add(new Directive(Directive.Type.IFDEF, definition, directed));
                } else if (directed.startsWith("ifndef")) {
                    String definition = directed.substring(6 + 1, directed.indexOf(' '));
                    directed = directed.substring(directed.indexOf(' ') + 1);
                    directives.add(new Directive(Directive.Type.IFNDEF, definition, directed));
                */} else if (directed.startsWith("undef")) {
                    directives.add(new Directive(Directive.Type.DEFINE, directed.substring(6)));
                }
            } else {
                newCode += lines[line] + "\n";
            }
            line++;
        }
        return new Pair<>(directives, newCode);
    }

    public String applyDirectives(String code, List<Directive> directives) {
        for (Directive dir : directives) {
            if (dir.t == Directive.Type.DEFINE) {
                defined.add(dir.a);
            } else if (dir.t == Directive.Type.UNDEF) {
                for (String s : defined) {
                    if (s.equals(dir.a)) {
                        defined.remove(s);
                        break;
                    }
                }
            } else if (dir.t == Directive.Type.ALIAS) {
                aliases.put(dir.a, dir.b);
            }
        }

        for (String alias : aliases.keySet()) {
            code = code.replaceAll(alias, aliases.get(alias));
        }

        return code;
    }

    public String process(String code) {
        String c1 = code;
        while (true) {
            Pair<List<Directive>, String> dirs = resolveDirectives(c1);
            c1 = dirs.b;
            code = applyDirectives(c1, dirs.a);
            if (dirs.a.size() == 0) break;
        }
        return code;
    }

    public static void main(String[] args) throws IOException {
        Preprocessor p = new Preprocessor();
        String code = FileUtils.readFileToString(new File("preproc.test.q"), "UTF-8");
        code = p.process(code);
        System.out.println(code);
    }

}
