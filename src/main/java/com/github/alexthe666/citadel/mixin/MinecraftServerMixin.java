package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.world.ModifiableTickRateServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ModifiableTickRateServer {
    @Unique
    private long modifiedMsPerTick = -1;
    @Unique
    private long masterMs;

//    @Inject(
//            method = "runServer()V",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/server/MinecraftServer;startMetricsRecordingTick()V",
//                    shift = At.Shift.BEFORE
//            )
//    )
//    protected void citadel_beforeServerTick(CallbackInfo ci) {
//        this.masterTick();
//    }

    @Unique
    private void masterTick() {
        this.masterMs += 50L;
    }

    @ModifyConstant(method = "runServer()V", constant = @Constant(longValue = 50L), expect = 4)
    private long citadel_serverMsPerTick(long value) {
        return this.modifiedMsPerTick == -1 ? value : this.modifiedMsPerTick;
    }

    @Override
    public void setGlobalTickLengthMs(long msPerTick) {
        this.modifiedMsPerTick = msPerTick;
    }

    @Override
    public long getMasterMs() {
        return this.masterMs;
    }
}
