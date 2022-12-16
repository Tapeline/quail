package me.tapeline.quailj.libmanagement;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;

public class EmbedIntegrator {

    public Runtime runtime;

    public EmbedIntegrator(Runtime runtime) {
        this.runtime = runtime;
    }

    public QObject integrateEmbed(Embed embed, HashMap<String, QObject> args) {
        return embed.integrate(this, args == null? new HashMap<>() : args);
    }
}