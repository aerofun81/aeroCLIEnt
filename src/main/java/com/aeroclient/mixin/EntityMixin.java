package com.aeroclient.mixin;

import com.aeroclient.module.ModuleManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "slowMovement", at = @At("HEAD"), cancellable = true)
    private void aeroclient$noWeb(BlockState state, Vec3d multiplier, CallbackInfo ci) {
        if ((Object) this instanceof ClientPlayerEntity
                && ModuleManager.NO_WEB != null && ModuleManager.NO_WEB.isEnabled()) {
            ci.cancel();
        }
    }
}
