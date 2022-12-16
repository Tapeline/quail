package me.tapeline.quailj.typing.objects.errors;

public class ErrorMessage {

    public int line;
    public int character;
    public int length;
    public String message;
    public String errorType;
    public boolean positionResolved = false;

    public ErrorMessage(int line, int character, int length, String message, String errorType) {
        this.line = line;
        this.character = character;
        this.length = length;
        this.message = message;
        this.errorType = errorType;
        positionResolved = true;
    }

    public ErrorMessage(String errorType, String message) {
        this.message = message;
        this.errorType = errorType;
    }

    public void setPosition(int line, int character, int length) {
        this.line = line;
        this.character = character;
        this.length = length;
        positionResolved = true;
    }

    public String toString() {
        return "Error on line " + line + " character " + character + ":\n" + errorType + ":\n" + message;
    }

}
