package me.tapeline.quailj.language.types;

public class NumType extends QType {

    public double value;

    public NumType(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value % 1 == 0? Long.toString(Math.round(value)) : Double.toString(value);
    }

}
