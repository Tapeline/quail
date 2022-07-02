package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.VariableTable;

import java.util.HashMap;

public class JavaType<T> extends QType {

    public T value;

    public JavaType(T data) {
        value = data;
        this.table = new VariableTable("JavaType<" +
                this.getClass().getTypeParameters()[0].getTypeName() + "> Table");
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
