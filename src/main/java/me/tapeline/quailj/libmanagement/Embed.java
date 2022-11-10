package me.tapeline.quailj.libmanagement;

import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;
import java.util.Queue;

public interface Embed {

    QObject integrate(EmbedIntegrator integrator, HashMap<String, QObject> args);
    String getName();
    String getProvider();

}