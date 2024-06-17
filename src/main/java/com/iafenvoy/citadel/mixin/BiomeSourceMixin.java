package com.iafenvoy.citadel.mixin;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.iafenvoy.citadel.server.world.ExpandedBiomeSource;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin implements ExpandedBiomeSource {
    @Mutable
    @Final
    @Shadow
    private Supplier<Set<RegistryEntry<Biome>>> biomes;
    @Unique
    private boolean expanded;
    @Unique
    private Map<RegistryKey<Biome>, RegistryEntry<Biome>> map = new HashMap<>();

    @Override
    public Map<RegistryKey<Biome>, RegistryEntry<Biome>> getResourceKeyMap() {
        return this.map;
    }

    @Override
    public void setResourceKeyMap(Map<RegistryKey<Biome>, RegistryEntry<Biome>> map) {
        this.map = map;
    }

    @Override
    public void expandBiomesWith(Set<RegistryEntry<Biome>> newGenBiomes) {
        if (!this.expanded) {
            ImmutableSet.Builder<RegistryEntry<Biome>> builder = ImmutableSet.builder();
            builder.addAll(this.biomes.get());
            builder.addAll(newGenBiomes);
            this.biomes = Suppliers.memoize(builder::build);
            this.expanded = true;
        }
    }

}