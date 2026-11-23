package com.aeroclient.module;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.lwjgl.glfw.GLFW;

public class Fullbright extends Module {
    public Fullbright() { super("Fullbright", GLFW.GLFW_KEY_B); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (p != null && !p.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, StatusEffectInstance.INFINITE, 0, false, false));
        }
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
