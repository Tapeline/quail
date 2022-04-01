package me.tapeline.quailj.utils;

import me.tapeline.quailj.types.RuntimeStriker;

import java.util.List;

public class Assert {

    public static boolean size(List d, int s, String at) throws RuntimeStriker {
        if (d.size() < s) throw new RuntimeStriker(at);
        else return true;
    }

    public static void require(boolean b, String msg) throws RuntimeStriker {
        if (!b) throw new RuntimeStriker(msg);
    }

}
