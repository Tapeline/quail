package me.tapeline.quailj.runtime;

public class HereScope {

    public Memory scope;
    public Runtime runtime;

    public HereScope(Memory scope, Runtime runtime) {
        this.scope = scope;
        this.runtime = runtime;
    }

}
