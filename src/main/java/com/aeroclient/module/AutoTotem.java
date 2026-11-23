package com.aeroclient.module;

import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    private int delay;

    public AutoTotem() { super("AutoTotem", UNBOUND); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (p == null || mc.interactionManager == null) return;
        if (p.currentScreenHandler != p.playerScreenHandler) return;
        if (delay > 0) { delay--; return; }
        if (p.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) return;

        for (int i = 0; i < 36; i++) {
            if (p.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                int screenSlot = i < 9 ? i + 36 : i;
                // button 40 = swap with offhand
                mc.interactionManager.clickSlot(p.playerScreenHandler.syncId, screenSlot, 40,
                        SlotActionType.SWAP, p);
                delay = 3;
                return;
            }
        }
    }
}
