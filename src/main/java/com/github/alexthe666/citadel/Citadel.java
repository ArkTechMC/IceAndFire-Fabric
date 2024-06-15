package com.github.alexthe666.citadel;

import com.github.alexthe666.citadel.config.ConfigHolder;
import com.github.alexthe666.citadel.config.ServerConfig;
import com.github.alexthe666.citadel.server.generation.VillageHouseManager;
import com.github.alexthe666.citadel.server.world.ExpandedBiomeSource;
import dev.arktechmc.iafextra.StaticVariables;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Citadel {
    public static final String MOD_ID = "citadel";
    public static final Logger LOGGER = LogManager.getLogger("citadel");

    public Citadel() {
        this.onModConfigEvent();
    }

    public static void onServerAboutToStart() {
        DynamicRegistryManager registryAccess = StaticVariables.server.getRegistryManager();
        VillageHouseManager.addAllHouses(registryAccess);
        Registry<Biome> allBiomes = registryAccess.get(RegistryKeys.BIOME);
        Registry<DimensionOptions> levelStems = registryAccess.get(RegistryKeys.DIMENSION);
        Map<RegistryKey<Biome>, RegistryEntry<Biome>> biomeMap = new HashMap<>();
        for (RegistryKey<Biome> biomeResourceKey : allBiomes.getKeys()) {
            Optional<RegistryEntry.Reference<Biome>> holderOptional = allBiomes.getEntry(biomeResourceKey);
            holderOptional.ifPresent(biomeHolder -> biomeMap.put(biomeResourceKey, biomeHolder));
        }
        for (RegistryKey<DimensionOptions> levelStemResourceKey : levelStems.getKeys()) {
            Optional<RegistryEntry.Reference<DimensionOptions>> holderOptional = levelStems.getEntry(levelStemResourceKey);
            if (holderOptional.isPresent() && holderOptional.get().value().chunkGenerator().getBiomeSource() instanceof ExpandedBiomeSource expandedBiomeSource)
                expandedBiomeSource.setResourceKeyMap(biomeMap);
        }
    }

    public void onModConfigEvent() {
        // Rebake the configs when they change
        ServerConfig.skipWarnings = ConfigHolder.SERVER.skipDatapackWarnings.get();
        ServerConfig.citadelEntityTrack = ConfigHolder.SERVER.citadelEntityTracker.get();
        ServerConfig.chunkGenSpawnModifierVal = ConfigHolder.SERVER.chunkGenSpawnModifier.get();
        //citadelTestBiomeData = SpawnBiomeConfig.create(new ResourceLocation("citadel:config_biome"), CitadelBiomeDefinitions.TERRALITH_TEST);
    }
}
