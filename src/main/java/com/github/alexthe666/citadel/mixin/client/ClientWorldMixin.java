package com.github.alexthe666.citadel.mixin.client;

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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    protected ClientWorldMixin(MutableWorldProperties writableLevelData, RegistryKey<World> levelResourceKey, DynamicRegistryManager registryAccess, RegistryEntry<DimensionType> dimensionTypeHolder, Supplier<Profiler> filler, boolean b1, boolean b2, long seed, int i) {
        super(writableLevelData, levelResourceKey, registryAccess, dimensionTypeHolder, filler, b1, b2, seed, i);
    }

    @ModifyConstant(method = "tickTime", constant = @Constant(longValue = 1L), expect = 2)
    private long citadel_clientSetDayTime(long timeIn) {
        return ClientTickRateTracker.getForClient(MinecraftClient.getInstance()).getDayTimeIncrement(timeIn);
    }
}

