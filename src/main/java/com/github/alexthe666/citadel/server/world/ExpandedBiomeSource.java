package com.github.alexthe666.citadel.server.world;

import java.util.Map;
import java.util.Set;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public interface ExpandedBiomeSource {

    void setResourceKeyMap(Map<RegistryKey<Biome>, RegistryEntry<Biome>> map);
    Map<RegistryKey<Biome>, RegistryEntry<Biome>> getResourceKeyMap();
    void expandBiomesWith(Set<RegistryEntry<Biome>> newGenBiomes);
}
