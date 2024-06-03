package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.client.event.EventGetStarBrightness;
import com.github.alexthe666.citadel.client.tick.ClientTickRateTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientLevelMixin extends World {

    protected ClientLevelMixin(MutableWorldProperties writableLevelData, RegistryKey<World> levelResourceKey, DynamicRegistryManager registryAccess, RegistryEntry<DimensionType> dimensionTypeHolder, Supplier<Profiler> filler, boolean b1, boolean b2, long seed, int i) {
        super(writableLevelData, levelResourceKey, registryAccess, dimensionTypeHolder, filler, b1, b2, seed, i);
    }

    @Inject(at = @At("RETURN"), remap = CitadelConstants.REMAPREFS, method = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F", cancellable = true)
    private void citadel_getStarBrightness(float partialTicks, CallbackInfoReturnable<Float> cir) {
        EventGetStarBrightness event = new EventGetStarBrightness(((ClientWorld) (Object) this), cir.getReturnValue(), partialTicks);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.ALLOW) {
            cir.setReturnValue(event.getBrightness());
        }
    }

    @ModifyConstant(
            method = "Lnet/minecraft/client/multiplayer/ClientLevel;tickTime()V",
            remap = CitadelConstants.REMAPREFS,
            constant = @Constant(longValue = 1L),
            expect = 2)
    private long citadel_clientSetDayTime(long timeIn) {
        return ClientTickRateTracker.getForClient(MinecraftClient.getInstance()).getDayTimeIncrement(timeIn);
    }
}

