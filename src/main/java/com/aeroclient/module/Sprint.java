package com.aeroclient.module;

import org.lwjgl.glfw.GLFW;

public class Sprint extends Module {
    public Sprint() { super("Sprint", GLFW.GLFW_KEY_V); }

    @Override
    public void onTick() {
        if (mc.options.forwardKey.isPressed()) mc.options.sprintKey.setPressed(true);
    }

    @Override
    protected void onDisable() { mc.options.sprintKey.setPressed(false); }
}
