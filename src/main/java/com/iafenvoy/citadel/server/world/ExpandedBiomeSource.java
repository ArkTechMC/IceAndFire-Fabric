package com.iafenvoy.citadel.server.world;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.Set;

public interface ExpandedBiomeSource {
    Map<RegistryKey<Biome>, RegistryEntry<Biome>> getResourceKeyMap();

    void setResourceKeyMap(Map<RegistryKey<Biome>, RegistryEntry<Biome>> map);

    void expandBiomesWith(Set<RegistryEntry<Biome>> newGenBiomes);
}
