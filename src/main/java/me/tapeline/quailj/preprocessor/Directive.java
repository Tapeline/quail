package me.tapeline.quailj.preprocessor;

public class Directive {

    public enum Type {
        ALIAS, DEFINE, PUT, UNDEF;
    }

    public Type t;
    public String a;
    public String b;

    public Directive(Type t, String a, String b) {
        this.t = t;
        this.a = a;
        this.b = b;
    }

    public Directive(Type t, String a) {
        this.t = t;
        this.a = a;
    }
}
