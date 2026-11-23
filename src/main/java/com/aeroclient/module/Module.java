package com.aeroclient.module;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    public final String name;
    public final KeyBinding key;
    private boolean enabled;

    protected Module(String name, int defaultKey) {
        this.name = name;
        this.key = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("AC " + name, InputUtil.Type.KEYSYM, defaultKey, "AeroClient"));
    }

    public boolean isEnabled() { return enabled; }

    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean value) {
        if (value == enabled) return;
        enabled = value;
        if (value) onEnable(); else onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}
    public void onTick() {}
}
