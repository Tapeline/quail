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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas keyboard:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas keyboard:invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas keyboard: invalid arg0 type");
        HashMap<String, QType> data = new HashMap<>();
        QWindow win = ((QWindow) ((JavaType<?>) a.get(0)).value);
        data.put("any", QType.V(win.keyboard.pressed.size() > 0));
        ListType l = new ListType();
        for (Character c : win.keyboard.pressed) l.values.add(QType.V(c + ""));
        data.put("pressed", l);
        return new ContainerType(data);
    }

    @Override
    public QType copy() {
        return new CanvasFuncKeyboard();
    }
}
