package me.tapeline.quailj.libmanagement;

import me.tapeline.quailj.runtime.Runtime;

import java.io.InputStream;
import java.io.PrintStream;

public class EmbedIntegrator {

    public Runtime runtime;

    public EmbedIntegrator(Runtime runtime) {
        this.runtime = runtime;
    }

    public int integrateEmbed(Embed embed) {
        try {
            embed.integrate(this);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}