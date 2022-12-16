package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.javaadapter.AdapterFuncJavaCall;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class JiFuncGetAdapter extends QBuiltinFunc {

    public JiFuncGetAdapter(Runtime runtime) {
        super(
                "getAdapter",
                Collections.singletonList(
                        new FuncArgument(
                                "obj",
                                new ArrayList<>(),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        return new QJavaAdapter<>(runtime, AdapterFuncJavaCall.convertArgument(args.get("obj")));
    }

}
