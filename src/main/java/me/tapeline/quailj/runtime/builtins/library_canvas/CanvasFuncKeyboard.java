package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.builtins.intype_list.ListFuncClear;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CanvasFuncKeyboard extends FuncType {
    public CanvasFuncKeyboard() {
        super("keyboard", Arrays.asList("c"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas keyboard:invalid args size");
        Assert.require(a.get(0).v instanceof JavaType, "canvas keyboard:invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(0).v).value instanceof QWindow, "canvas keyboard: invalid arg0 type");
        HashMap<String, QValue> data = new HashMap<>();
        QWindow win = ((QWindow) ((JavaType<?>) a.get(0).v).value);
        data.put("any", new QValue(win.keyboard.pressed.size() > 0));
        ListType l = new ListType();
        for (Character c : win.keyboard.pressed) l.values.add(new QValue(c + ""));
        data.put("pressed", new QValue(l));
        return new QValue(new ContainerType(data));
    }

    @Override
    public QType copy() {
        return new CanvasFuncKeyboard();
    }
}
