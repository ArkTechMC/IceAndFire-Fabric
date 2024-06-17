package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.event.AttackEntityEvent;
import com.iafenvoy.iceandfire.event.EventBus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci) {
        EventBus.post(new AttackEntityEvent((PlayerEntity) (Object) this, target));
    }
}
