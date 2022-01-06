package me.tapeline.quailj.language.types;

public class RuntimeStriker extends Exception {

    public RuntimeStrikerTypes type;

    public RuntimeStriker(RuntimeStrikerTypes typ) {
        super(typ.name());
        type = typ;
    }

    public RuntimeStriker(String message) {
        super(message);
        type = RuntimeStrikerTypes.EXCEPTION;
    }
}
