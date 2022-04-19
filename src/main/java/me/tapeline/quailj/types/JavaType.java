package me.tapeline.quailj.types;

import java.util.HashMap;

public class JavaType<T> extends QType {

    public T value;

    public JavaType(T data) {
        value = data;
        this.table = new HashMap<>();
    }

    @Override
    public JavaType copy() {
        JavaType<T> v = new JavaType<>(this.value);
        v.table.putAll(this.table);
        v.value = this.value;
        return v;
    }

    @Override
    public String toString() {
        return "JavaType " + value.getClass().toString();
    }
}
