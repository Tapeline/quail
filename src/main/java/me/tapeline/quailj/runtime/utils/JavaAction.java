package me.tapeline.quailj.runtime.utils;

import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

public abstract class JavaAction {

    public abstract QObject action(Runtime runtime, Memory memory) throws RuntimeStriker;

}
