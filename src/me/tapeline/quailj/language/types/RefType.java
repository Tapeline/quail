package me.tapeline.quailj.language.types;

public class RefType extends QType {

    public String value;

    public RefType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
