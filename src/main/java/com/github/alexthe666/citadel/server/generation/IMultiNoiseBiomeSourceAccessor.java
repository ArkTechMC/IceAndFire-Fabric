package com.github.alexthe666.citadel.server.generation;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public interface IMultiNoiseBiomeSourceAccessor {

    void setLastSampledSeed(long seed);

    void setLastSampledDimension(RegistryKey<World> dimension);
}
