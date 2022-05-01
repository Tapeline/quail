package me.tapeline.quailj.libmanagement;

public interface Embed {

    void integrate(EmbedIntegrator integrator);
    String getName();
    String getProvider();

}