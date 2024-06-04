package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerWorld.class)
public class ServerLevelMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @ModifyConstant(
            method = "tickTime",
            constant = @Constant(longValue = 1L),
            expect = 2)
    private long citadel_clientSetDayTime(long timeIn) {
        return ServerTickRateTracker.getForServer(server).getDayTimeIncrement(timeIn);
    }


}
