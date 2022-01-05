package me.tapeline.quailj.language.types;

public class BoolType extends QType {

    public boolean value;

    public BoolType(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

}
