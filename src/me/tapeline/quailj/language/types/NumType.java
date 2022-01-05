package me.tapeline.quailj.language.types;

public class NumType extends QType {

    public double value;

    public NumType(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

}
