package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.event.PlayerEvents;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "respawnPlayer", at = @At("HEAD"))
    private void onPlayerLoggedOut(ServerPlayerEntity serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        PlayerEvents.LOGGED_OUT.invoker().handleConnection(serverPlayer);
    }
}
