package me.tapeline.quarkj.language.types;

public class VoidType extends QType {

    public boolean value;

    public VoidType(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "VoidType";
    }

}
