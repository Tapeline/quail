package me.tapeline.quailj.types;

import java.util.HashMap;

public interface QType {

    static QType nullSafe(QType v) {
        return v == null? new VoidType() : v;
    }

    HashMap<String, QType> table = new HashMap<>();

}
