package com.aeroclient;

import com.aeroclient.gui.ClickGui;
import com.aeroclient.module.Module;
import com.aeroclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AeroClient implements ClientModInitializer {
    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "AeroClient GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "AeroClient"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            while (guiKey.wasPressed()) client.setScreen(new ClickGui());
            for (Module m : ModuleManager.all()) {
                while (m.key.wasPressed()) m.toggle();
                if (m.isEnabled()) m.onTick();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ModuleManager.disableAll());
    }
}
