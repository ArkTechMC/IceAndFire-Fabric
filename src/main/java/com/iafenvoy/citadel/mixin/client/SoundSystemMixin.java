package com.iafenvoy.citadel.mixin.client;

import com.iafenvoy.citadel.client.tick.ClientTickRateTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @Inject(method = "getAdjustedPitch(Lnet/minecraft/client/sound/SoundInstance;)F", cancellable = true, at = @At(value = "RETURN"))
    protected void citadel_setupRotations(SoundInstance soundInstance, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() * ClientTickRateTracker.getForClient(MinecraftClient.getInstance()).modifySoundPitch(soundInstance));
    }
}
