package me.tapeline.quailj.language.types;

import java.util.ArrayList;
import java.util.List;

public class ListType extends QType {

    public List<QType> values = new ArrayList<>();

    public ListType() {

    }

    public ListType(List<QType> l) {
        this.values = l;
    }

    @Override
    public String toString() {
        return values.toString();
    }

}
