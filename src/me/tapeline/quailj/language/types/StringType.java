package me.tapeline.quailj.language.types;

public class StringType extends QType {

    public String value;

    public StringType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
