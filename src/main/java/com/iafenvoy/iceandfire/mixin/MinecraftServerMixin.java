package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.StaticVariables;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onServerCreate(CallbackInfo ci) {
        StaticVariables.server = (MinecraftServer) (Object) this;
    }
}
