package me.tapeline.quailj.language.types;

import java.util.ArrayList;
import java.util.List;

public class ListType extends QType {

    public List<QType> values = new ArrayList<>();

    @Override
    public String toString() {
        return values.toString();
    }

}
