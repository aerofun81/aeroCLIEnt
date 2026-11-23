package com.aeroclient.module;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.input.Input;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class Freecam extends Module {
    public double speed = 0.6;
    private OtherClientPlayerEntity cam;
    private Input savedInput;

    public Freecam() { super("Freecam", GLFW.GLFW_KEY_G); }

    @Override
    protected void onEnable() {
        var p = mc.player;
        if (p == null || mc.world == null) return;
        cam = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), "Freecam"));
        cam.refreshPositionAndAngles(p.getX(), p.getY(), p.getZ(), p.getYaw(), p.getPitch());
        cam.noClip = true;
        savedInput = p.input;
        p.input = new Input();
        mc.setCameraEntity(cam);
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) {
            if (savedInput != null) mc.player.input = savedInput;
            mc.setCameraEntity(mc.player);
        }
        cam = null;
        savedInput = null;
    }

    @Override
    public void onTick() {
        var p = mc.player;
        if (p == null || cam == null) return;
        float yaw = p.getYaw(), pitch = p.getPitch();
        Vec3d fwd = Vec3d.fromPolar(pitch, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);
        Vec3d v = Vec3d.ZERO;
        var o = mc.options;
        if (o.forwardKey.isPressed()) v = v.add(fwd);
        if (o.backKey.isPressed()) v = v.subtract(fwd);
        if (o.rightKey.isPressed()) v = v.add(right);
        if (o.leftKey.isPressed()) v = v.subtract(right);
        if (o.jumpKey.isPressed()) v = v.add(0, 1, 0);
        if (o.sneakKey.isPressed()) v = v.add(0, -1, 0);
        if (v.lengthSquared() > 0) v = v.normalize().multiply(speed);

        cam.prevX = cam.lastRenderX = cam.getX();
        cam.prevY = cam.lastRenderY = cam.getY();
        cam.prevZ = cam.lastRenderZ = cam.getZ();
        cam.prevYaw = cam.getYaw();
        cam.prevPitch = cam.getPitch();
        cam.setPosition(cam.getX() + v.x, cam.getY() + v.y, cam.getZ() + v.z);
        cam.setYaw(yaw);
        cam.setPitch(pitch);
    }
}
