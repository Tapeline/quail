package me.tapeline.quailj;

import java.util.HashMap;

public class Language {
    public static String lang = "en";

    public static HashMap<String, HashMap<String, String>> langStorage = new HashMap<>();

    public static void setupAndSelect(String l) {
        HashMap<String, String> en = new HashMap<>();
        en.put("p.expected-func-event-container",
                "All code should be wrapped with functions, event handlers or containers!");
        en.put("p.func.no-id", "Expected ID in function definition!");
        en.put("p.event.no-id", "Expected event name in event handler definition!");
        en.put("p.event.no-as", "Expected `as` in event handler definition!");
        en.put("p.event.no-var", "Expected variable in event handler definition after `as`!");
        en.put("p.container.no-has", "Expected `has` at end of container definition!");
        en.put("p.container.unknown-content", "Expected object builder, function or expression in container definition");
        en.put("p.common.no-par", "Expected bracket!");
        en.put("p.common.no-end", "Expected `end`!");
        en.put("p.common.expected-variable", "Expected variable!");
        en.put("p.loop.no-as-in", "Expected `as` (if `through`) or `in` (if `every`)!");
        en.put("p.loop.invalid-expression", "Invalid expression!");
        en.put("p.loop.no-stop-when", "Expected `stop when` after `loop` block!");
        en.put("p.statement.no-valid-case", "No valid statement parsing case found!");
        en.put("p.expr.no-valid-case", "No valid expression parsing case found!");
        en.put("p.expr.invalid-assign-target", "Invalid assignment target!");
        en.put("p.expr.argument-len-exceeded", "Exceeded argument length! (Max. 256)");
        en.put("p.expr.expected-id-after-dot", "Expected ID after `.`!");
        en.put("p.expr.expected-key-value", "Expected key=value pair in container definition!");
        en.put("p.container.no-like-id", "Expected ID after `like`!");



        en.put("r.type.convert", "Cannot convert types! ");
        langStorage.put("en", en);
        lang = l;
    }

    public static String get(String k) {
        return langStorage.get(lang).get(k);
    }
}
