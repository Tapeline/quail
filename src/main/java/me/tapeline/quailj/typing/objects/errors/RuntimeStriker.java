package me.tapeline.quailj.typing.objects.errors;

import me.tapeline.quailj.typing.objects.QObject;

public class RuntimeStriker extends Exception {

    public enum Type {
        RETURN,
        BREAK,
        CONTINUE,
        EXCEPTION,
        STOP_ITERATION,
        EXIT
    }

    public ErrorMessage error;

    public Type type;
    public long strikeHP = 0;
    public QObject returnValue;
    public int code;

    public RuntimeStriker(ErrorMessage message) {
        super(message.toString());
        error = message;
        type = Type.EXCEPTION;
    }

    public RuntimeStriker(Error flag, QObject exception) {
        error = new ErrorMessage(exception.get("type").toString(), exception.get("message").toString());
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
