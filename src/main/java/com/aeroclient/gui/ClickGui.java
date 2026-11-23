package com.aeroclient.gui;

import com.aeroclient.module.Module;
import com.aeroclient.module.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    public ClickGui() { super(Text.literal("AeroClient")); }

    private static Text label(Module m) {
        return Text.literal(m.name + ": " + (m.isEnabled() ? "ON" : "OFF"));
    }

    @Override
    protected void init() {
        int y = 20;
        for (Module m : ModuleManager.all()) {
            addDrawableChild(ButtonWidget.builder(label(m), b -> {
                m.toggle();
                b.setMessage(label(m));
            }).dimensions(width / 2 - 70, y, 140, 20).build());
            y += 24;
        }
    }

    @Override
    public boolean shouldPause() { return false; }
}
