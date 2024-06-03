package com.github.alexthe666.citadel.server.world;

import com.google.common.collect.ImmutableSet;
import java.util.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;

public class ExpandedBiomes {
    private static final Map<RegistryKey<DimensionOptions>, List<RegistryKey<Biome>>> biomes = new HashMap<>();

    public static void addExpandedBiome(RegistryKey<Biome> biome, RegistryKey<DimensionOptions> dimension){
        List<RegistryKey<Biome>> list;
        if(!biomes.containsKey(dimension)){
            list = new ArrayList<>();
        }else{
            list = biomes.get(dimension);
        }
        if(!list.contains(biome)){
            list.add(biome);
        }
        biomes.put(dimension, list);
    }

    public static Set<RegistryEntry<Biome>> buildBiomeList(DynamicRegistryManager registryAccess, RegistryKey<DimensionOptions> dimension){
        List<RegistryKey<Biome>> list = biomes.get(dimension);
        if(list == null || list.isEmpty()){
            return Set.of();
        }
        Registry<Biome> allBiomes = registryAccess.get(RegistryKeys.BIOME);
        ImmutableSet.Builder<RegistryEntry<Biome>> biomeHolders = ImmutableSet.builder();
        for(RegistryKey<Biome> biomeResourceKey : list){
            Optional<RegistryEntry.Reference<Biome>> holderOptional = allBiomes.getEntry(biomeResourceKey);
            holderOptional.ifPresent(biomeHolders::add);
        }
        return biomeHolders.build();
    }
}
