package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.world.CustomBiomeFilter;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public final class IafPlacedFeatures {

    public static final RegistryKey<PlacedFeature> PLACED_FIRE_DRAGON_ROOST = registerKey("fire_dragon_roost");
    public static final RegistryKey<PlacedFeature> PLACED_ICE_DRAGON_ROOST = registerKey("ice_dragon_roost");
    public static final RegistryKey<PlacedFeature> PLACED_LIGHTNING_DRAGON_ROOST = registerKey("lightning_dragon_roost");
    public static final RegistryKey<PlacedFeature> PLACED_FIRE_DRAGON_CAVE = registerKey("fire_dragon_cave");
    public static final RegistryKey<PlacedFeature> PLACED_ICE_DRAGON_CAVE = registerKey("ice_dragon_cave");
    public static final RegistryKey<PlacedFeature> PLACED_LIGHTNING_DRAGON_CAVE = registerKey("lightning_dragon_cave");
    public static final RegistryKey<PlacedFeature> PLACED_CYCLOPS_CAVE = registerKey("cyclops_cave");
    public static final RegistryKey<PlacedFeature> PLACED_PIXIE_VILLAGE = registerKey("pixie_village");
    public static final RegistryKey<PlacedFeature> PLACED_SIREN_ISLAND = registerKey("siren_island");
    public static final RegistryKey<PlacedFeature> PLACED_HYDRA_CAVE = registerKey("hydra_cave");
    public static final RegistryKey<PlacedFeature> PLACED_MYRMEX_HIVE_DESERT = registerKey("myrmex_hive_desert");
    public static final RegistryKey<PlacedFeature> PLACED_MYRMEX_HIVE_JUNGLE = registerKey("myrmex_hive_jungle");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_DEATH_WORM = registerKey("spawn_death_worm");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_L = registerKey("spawn_dragon_skeleton_lightning");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_F = registerKey("spawn_dragon_skeleton_fire");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_I = registerKey("spawn_dragon_skeleton_ice");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_HIPPOCAMPUS = registerKey("spawn_hippocampus");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_SEA_SERPENT = registerKey("spawn_sea_serpent");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_STYMPHALIAN_BIRD = registerKey("spawn_stymphalian_bird");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_WANDERING_CYCLOPS = registerKey("spawn_wandering_cyclops");
    public static final RegistryKey<PlacedFeature> PLACED_SILVER_ORE = registerKey("silver_ore");
    public static final RegistryKey<PlacedFeature> PLACED_SAPPHIRE_ORE = registerKey("sapphire_ore");
    public static final RegistryKey<PlacedFeature> PLACED_FIRE_LILY = registerKey("fire_lily");
    public static final RegistryKey<PlacedFeature> PLACED_LIGHTNING_LILY = registerKey("lightning_lily");
    public static final RegistryKey<PlacedFeature> PLACED_FROST_LILY = registerKey("frost_lily");


    private static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, SquarePlacementModifier.of(), pHeightRange, BiomePlacementModifier.of());
    }
    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacementModifier.of(pCount), pHeightRange);
    }
    public static void bootstrap(Registerable<PlacedFeature> context) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> features = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        // Surface
        context.register(PLACED_FIRE_DRAGON_ROOST, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.FIRE_DRAGON_ROOST), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_ICE_DRAGON_ROOST, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.ICE_DRAGON_ROOST), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_LIGHTNING_DRAGON_ROOST, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.LIGHTNING_DRAGON_ROOST), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_CYCLOPS_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.CYCLOPS_CAVE), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_PIXIE_VILLAGE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.PIXIE_VILLAGE), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SIREN_ISLAND, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SIREN_ISLAND), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_HYDRA_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.HYDRA_CAVE), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_MYRMEX_HIVE_DESERT, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.MYRMEX_HIVE_DESERT), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_MYRMEX_HIVE_JUNGLE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.MYRMEX_HIVE_JUNGLE), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SPAWN_DEATH_WORM, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_DEATH_WORM), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SPAWN_DRAGON_SKELETON_L, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_DRAGON_SKELETON_L), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SPAWN_DRAGON_SKELETON_F, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_DRAGON_SKELETON_F), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SPAWN_DRAGON_SKELETON_I, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_DRAGON_SKELETON_I), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SPAWN_HIPPOCAMPUS, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_HIPPOCAMPUS), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SPAWN_SEA_SERPENT, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_SEA_SERPENT), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SPAWN_STYMPHALIAN_BIRD, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_STYMPHALIAN_BIRD), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_SPAWN_WANDERING_CYCLOPS, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_WANDERING_CYCLOPS), List.of(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())));
       //TODO: Check gen chance VegetationFeatures.java
        context.register(PLACED_FIRE_LILY, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.FIRE_LILY), List.of(RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_FROST_LILY, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.FROST_LILY), List.of(RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())));
        context.register(PLACED_LIGHTNING_LILY, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.LIGHTNING_LILY), List.of(RarityFilterPlacementModifier.of(32), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())));

        // Underground
        //TODO: Make it different from copper ore
        context.register(PLACED_SILVER_ORE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SILVER_ORE), commonOrePlacement(16, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-16), YOffset.fixed(112)))));
        //TODO: Maybe copper emerald ore?
        context.register(PLACED_SAPPHIRE_ORE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SAPPHIRE_ORE), commonOrePlacement(4, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-16), YOffset.fixed(112)))));
        context.register(PLACED_FIRE_DRAGON_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.FIRE_DRAGON_CAVE), List.of(CustomBiomeFilter.biome())));
        context.register(PLACED_ICE_DRAGON_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.ICE_DRAGON_CAVE), List.of(CustomBiomeFilter.biome())));
        context.register(PLACED_LIGHTNING_DRAGON_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.LIGHTNING_DRAGON_CAVE), List.of(CustomBiomeFilter.biome())));

    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("iceandfire",name));
    }
}
