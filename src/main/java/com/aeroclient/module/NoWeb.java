package com.aeroclient.module;

public class NoWeb extends Module {
    // The actual work happens in EntityMixin (cancels Entity#slowMovement).
    public NoWeb() { super("NoWeb", UNBOUND); }
}
