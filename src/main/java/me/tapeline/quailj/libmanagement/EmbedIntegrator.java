package me.tapeline.quailj.libmanagement;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.utils.Pair;

import java.util.HashMap;
import java.util.Queue;

public class EmbedIntegrator {

    public Runtime runtime;

    public EmbedIntegrator(Runtime runtime) {
        this.runtime = runtime;
    }

    public QObject integrateEmbed(Embed embed, HashMap<String, QObject> args) throws Exception {
        return embed.integrate(this, args == null? new HashMap<>() : args);
    }
}