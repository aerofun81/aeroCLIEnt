package com.aeroclient.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class KillAura extends Module {
    public double range = 3.0;
    public boolean attackPlayers = false;

    public KillAura() { super("KillAura", GLFW.GLFW_KEY_R); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (p == null || mc.world == null || mc.interactionManager == null) return;
        if (p.getAttackCooldownProgress(0.5f) < 1.0f) return;

        LivingEntity best = null;
        double bestDist = range * range;
        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof LivingEntity le) || le == p || !le.isAlive()) continue;
            boolean ok = le instanceof Monster || (attackPlayers && le instanceof PlayerEntity);
            if (!ok) continue;
            double d = p.squaredDistanceTo(le);
            if (d <= bestDist) { bestDist = d; best = le; }
        }
        if (best != null) {
            mc.interactionManager.attackEntity(p, best);
            p.swingHand(Hand.MAIN_HAND);
        }
    }
}
