package me.tapeline.quailj.types;

public class RuntimeStriker extends Exception {

    public RuntimeStrikerType type;
    public QValue val;
    public int posLine;
    public int posChar;

    public RuntimeStriker(RuntimeStrikerType typ) {
        super(typ.name());
        type = typ;
    }

    public RuntimeStriker(String message) {
        super(message);
        val = new QValue(message);
        type = RuntimeStrikerType.EXCEPTION;
    }

    public RuntimeStriker(String message, int p) {
        super(message);
        val = new QValue(message);
        type = RuntimeStrikerType.EXCEPTION;
        this.posLine = 0;
        this.posChar = p;
    }

    public RuntimeStriker(QValue v, int pl, int pc) {
        super(v.toString());
        val = v;
        type = RuntimeStrikerType.EXCEPTION;
        this.posLine = pl;
        this.posChar = pc;
    }

    public RuntimeStriker(QValue v) {
        super("return");
        val = v;
        type = RuntimeStrikerType.RETURN;
    }

    public RuntimeStriker(RuntimeStrikerType typ, QValue v) {
        super(typ.name());
        type = typ;
        val = v;
    }

    public boolean isNotException() {
        return !type.equals(RuntimeStrikerType.EXCEPTION);
    }
}
