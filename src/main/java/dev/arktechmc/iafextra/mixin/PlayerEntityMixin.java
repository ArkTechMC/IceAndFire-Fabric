package dev.arktechmc.iafextra.mixin;

import dev.arktechmc.iafextra.event.AttackEntityEvent;
import dev.arktechmc.iafextra.event.EventBus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci) {
        if (!EventBus.post(new AttackEntityEvent((PlayerEntity) (Object) this, target)))
            ci.cancel();
    }
}
