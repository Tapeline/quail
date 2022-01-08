package me.tapeline.quailj.language.types;

public class RuntimeStriker extends Exception {

    public RuntimeStrikerTypes type;
    public String msg;
    public QType retVal;

    public RuntimeStriker(RuntimeStrikerTypes typ) {
        super(typ.name());
        type = typ;
    }

    public RuntimeStriker(String message) {
        super(message);
        msg = message;
        type = RuntimeStrikerTypes.EXCEPTION;
    }

    public RuntimeStriker(QType v) {
        super("return");
        retVal = v;
        type = RuntimeStrikerTypes.RETURN;
    }
}
