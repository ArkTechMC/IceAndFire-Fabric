package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.event.EntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow
    public abstract ServerWorld toServerWorld();

    @Inject(method = "addPlayer", at = @At("HEAD"), cancellable = true)
    private void onPlayerJoin(ServerPlayerEntity player, CallbackInfo ci) {
        if (EntityEvents.ON_JOIN_WORLD.invoker().onJoinWorld(player, this.toServerWorld()))
            ci.cancel();
    }

    @Inject(method = "addEntities", at = @At("HEAD"))
    private void onEntityJoin(Stream<Entity> entities, CallbackInfo ci) {
//        for (Entity entity : entities.toList())
//            EntityEvents.ON_JOIN_WORLD.invoker().onJoinWorld(entity, this.toServerWorld());
    }
}
