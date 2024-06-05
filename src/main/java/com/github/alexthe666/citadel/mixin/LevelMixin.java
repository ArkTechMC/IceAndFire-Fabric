package com.github.alexthe666.citadel.mixin;


import com.github.alexthe666.citadel.Citadel;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(World.class)
public class LevelMixin {
    @Shadow
    @Final
    public boolean isClient;

    @Inject(at = @At("HEAD"), cancellable = true, method = "tickEntity")
    private void citadel_guardEntityTick(Consumer<Entity> ticker, Entity entity, CallbackInfo ci) {
        if (!this.isClient) {
            if (!Citadel.PROXY.canEntityTickServer((World) (Object) this, entity)) {
                ci.cancel();
            }
        } else {
            if (!Citadel.PROXY.canEntityTickClient((World) (Object) this, entity)) {
                ci.cancel();
            }
        }
    }
}
