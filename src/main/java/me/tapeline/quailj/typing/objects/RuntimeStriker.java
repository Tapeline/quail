package me.tapeline.quailj.typing.objects;

public class RuntimeStriker extends Exception {

    public enum Type {
        RETURN,
        BREAK,
        CONTINUE,
        EXCEPTION,
        STOP_ITERATION
    }

    public String stringRepresentation;
    public QObject exception;
    public Type type;
    public long strikeHP = 0;
    public QObject returnValue;

    public RuntimeStriker(String message) {
        super(message);
        exception = QObject.Val(message);
        stringRepresentation = message;
        type = Type.EXCEPTION;
    }

    public RuntimeStriker(QObject exception, String stringRepresentation) {
        this.exception = exception;
        this.stringRepresentation = stringRepresentation;
        type = Type.EXCEPTION;
    }

    public RuntimeStriker(Type type) {
        this.type = type;
    }

    public RuntimeStriker(QObject object) {
        this.type = Type.RETURN;
        returnValue = object;
    }

    public RuntimeStriker(long hp) {
        type = Type.BREAK;
        strikeHP = hp;
    }

}
