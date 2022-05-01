package me.tapeline.quailj.types;

public class RuntimeStriker extends Exception {

    public RuntimeStrikerType type;
    public QType val;
    public int posLine;
    public int posChar;

    public RuntimeStriker(RuntimeStrikerType typ) {
        super(typ.name());
        type = typ;
    }

    public RuntimeStriker(String message) {
        super(message);
        val = new StringType(message);
        type = RuntimeStrikerType.EXCEPTION;
    }

    public RuntimeStriker(String message, int p) {
        super(message);
        val = new StringType(message);
        type = RuntimeStrikerType.EXCEPTION;
        this.posLine = 0;
        this.posChar = p;
    }

    public RuntimeStriker(QType v, int pl, int pc) {
        super(v.toString());
        val = v;
        type = RuntimeStrikerType.EXCEPTION;
        this.posLine = pl;
        this.posChar = pc;
    }

    public RuntimeStriker(QType v) {
        super("return");
        val = v;
        type = RuntimeStrikerType.RETURN;
    }

    public RuntimeStriker(RuntimeStrikerType typ, QType v) {
        super(typ.name());
        type = typ;
        val = v;
    }

    public boolean isNotException() {
        return !type.equals(RuntimeStrikerType.EXCEPTION);
    }
}
