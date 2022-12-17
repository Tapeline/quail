package me.tapeline.quailj.runtime.std.javaadapter;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.*;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AdapterFuncJavaCall extends QBuiltinFunc {

    public AdapterFuncJavaCall(Runtime runtime) {
        super(
                "javaCall",
                Arrays.asList(
                        new FuncArgument(
                                "adapter",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "method",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "list",
                                new ArrayList<>(),
                                true
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        if (!(args.get("adapter") instanceof QJavaAdapter<?>))
            Runtime.error("Not a JavaAdapter");
        Object obj = ((QJavaAdapter<?>) args.get("adapter")).object;
        List<Object> convertedArguments = new ArrayList<>();
        for (QObject o : args.get("list").listValue())
            convertedArguments.add(convertArgument(o));
        Class<?>[] argClasses = new Class[convertedArguments.size()];
        int size = convertedArguments.size();
        for (int i = 0; i < size; i++) argClasses[i] = convertedArguments.get(i).getClass();
        primitiveClasses(argClasses);
        System.out.println(obj.getClass());
        try {
            Method method = obj.getClass().getMethod(args.get("method").toString(), argClasses);
            Object result = method.invoke(obj, convertedArguments.toArray());
            return packValue(runtime, result);
        } catch (NoSuchMethodException e) {
            Runtime.error("NoSuchMethodException while running JavaAdapter.javaCall:\n" + e);
        } catch (InvocationTargetException e) {
            Runtime.error("InvocationTargetException while running JavaAdapter.javaCall:\n" + e);
        } catch (IllegalAccessException e) {
            Runtime.error("IllegalAccessException while running JavaAdapter.javaCall:\n" + e);
        }
        return QObject.Val();
    }

    public static Object convertArgument(QObject object) {
        if (object instanceof QNumber)
            return ((QNumber) object).value;
        else if (object instanceof QString)
            return ((QString) object).value;
        else if (object instanceof QList) {
            List<Object> l = new ArrayList<>();
            for (QObject o : ((QList) object).values)
                l.add(convertArgument(o));
            return l;
        } else if (object instanceof QBool)
            return ((QBool) object).value;
        else if (object instanceof QNull)
            return null;
        else if (object instanceof QJavaAdapter<?>) {
            if (object.get("dontUnwrap").isTrue())
                return object;
            else
                return ((QJavaAdapter<?>) object).object;
        } else return object;
    }

    public static void primitiveClasses(Class<?>[] classes) {
        for (int i = 0; i < classes.length; i++) {
            if (classes[i] == Integer.class) classes[i] = int.class;
            if (classes[i] == Long.class) classes[i] = long.class;
            if (classes[i] == Byte.class) classes[i] = byte.class;
            if (classes[i] == Short.class) classes[i] = short.class;
            if (classes[i] == Double.class) classes[i] = double.class;
            if (classes[i] == Boolean.class) classes[i] = boolean.class;
            if (classes[i] == Float.class) classes[i] = float.class;
            if (classes[i] == Character.class) classes[i] = char.class;
        }
    }

    public static QObject packValue(Runtime runtime, Object obj) {
        if (obj instanceof String) {
            return QObject.Val((String) obj);
        } else if (obj instanceof Integer) {
            return QObject.Val((Integer) obj);
        } else if (obj instanceof Long) {
            return QObject.Val((Long) obj);
        } else if (obj instanceof Byte) {
            return QObject.Val((Byte) obj);
        } else if (obj instanceof Short) {
            return QObject.Val((Short) obj);
        } else if (obj instanceof Float) {
            return QObject.Val((Float) obj);
        } else if (obj instanceof Double) {
            return QObject.Val((Double) obj);
        } else if (obj instanceof Boolean) {
            return QObject.Val((Boolean) obj);
        } else if (obj instanceof Character) {
            return QObject.Val("" + obj);
        } else if (obj instanceof List<?>) {
            List<QObject> list = new ArrayList<>();
            for (Object listObj : ((List<?>) obj))
                list.add(packValue(runtime, listObj));
            return QObject.Val(list);
        } else if (obj instanceof Map<?, ?>) {
            QObject map = QObject.Val(new HashMap<>());
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet())
                map.set(entry.getKey().toString(), packValue(runtime, entry.getValue()));
            return map;
        } else if (obj instanceof QFunc) {
            return (QFunc) obj;
        } else return new QJavaAdapter<>(runtime, obj);
    }

}
