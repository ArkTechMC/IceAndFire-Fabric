package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import com.iafenvoy.iceandfire.world.feature.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;

@SuppressWarnings("unused")
public final class IafFeatures {
    public static final Feature<DefaultFeatureConfig> SPAWN_DEATH_WORM = feature("spawn_death_worm", new SpawnDeathWorm(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_DRAGON_SKELETON_L = feature("spawn_dragon_skeleton_lightning", new SpawnDragonSkeleton(IafEntities.LIGHTNING_DRAGON, DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_DRAGON_SKELETON_F = feature("spawn_dragon_skeleton_fire", new SpawnDragonSkeleton(IafEntities.FIRE_DRAGON, DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_DRAGON_SKELETON_I = feature("spawn_dragon_skeleton_ice", new SpawnDragonSkeleton(IafEntities.ICE_DRAGON, DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_HIPPOCAMPUS = feature("spawn_hippocampus", new SpawnHippocampus(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_SEA_SERPENT = feature("spawn_sea_serpent", new SpawnSeaSerpent(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_STYMPHALIAN_BIRD = feature("spawn_stymphalian_bird", new SpawnStymphalianBird(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_WANDERING_CYCLOPS = feature("spawn_wandering_cyclops", new SpawnWanderingCyclops(DefaultFeatureConfig.CODEC));

    private static <F extends Feature<? extends FeatureConfig>> F feature(String name, F feature) {
        return Registry.register(Registries.FEATURE, new Identifier(IceAndFire.MOD_ID, name), feature);
    }

    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_DEATH_WORM = key("spawn_death_worm");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_L = key("spawn_dragon_skeleton_lightning");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_F = key("spawn_dragon_skeleton_fire");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_I = key("spawn_dragon_skeleton_ice");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_HIPPOCAMPUS = key("spawn_hippocampus");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_SEA_SERPENT = key("spawn_sea_serpent");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_STYMPHALIAN_BIRD = key("spawn_stymphalian_bird");
    public static final RegistryKey<PlacedFeature> PLACED_SPAWN_WANDERING_CYCLOPS = key("spawn_wandering_cyclops");
    public static final RegistryKey<PlacedFeature> PLACED_SILVER_ORE = key("silver_ore");
    public static final RegistryKey<PlacedFeature> PLACED_SAPPHIRE_ORE = key("sapphire_ore");
    public static final RegistryKey<PlacedFeature> PLACED_FIRE_LILY = key("fire_lily");
    public static final RegistryKey<PlacedFeature> PLACED_LIGHTNING_LILY = key("lightning_lily");
    public static final RegistryKey<PlacedFeature> PLACED_FROST_LILY = key("frost_lily");

    public static RegistryKey<PlacedFeature> key(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(IceAndFire.MOD_ID, name));
    }

    public static void init() {
        addFeatureToBiome(IafBiomeTags.FIRE, PLACED_FIRE_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.ICE, PLACED_FROST_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.LIGHTNING, PLACED_LIGHTNING_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.FIRE, PLACED_SPAWN_DRAGON_SKELETON_F, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.ICE, PLACED_SPAWN_DRAGON_SKELETON_I, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.LIGHTNING, PLACED_SPAWN_DRAGON_SKELETON_L, GenerationStep.Feature.SURFACE_STRUCTURES);

        addFeatureToBiome(IafBiomeTags.SILVER_ORE, PLACED_SILVER_ORE, GenerationStep.Feature.UNDERGROUND_ORES);
        addFeatureToBiome(IafBiomeTags.SAPPHIRE_ORE, PLACED_SAPPHIRE_ORE, GenerationStep.Feature.UNDERGROUND_ORES);

        addFeatureToBiome(IafBiomeTags.DEATHWORM, PLACED_SPAWN_DEATH_WORM, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.WANDERING_CYCLOPS, PLACED_SPAWN_WANDERING_CYCLOPS, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.HIPPOCAMPUS, PLACED_SPAWN_HIPPOCAMPUS, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.SEA_SERPENT, PLACED_SPAWN_SEA_SERPENT, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.STYMPHALIAN_BIRD, PLACED_SPAWN_STYMPHALIAN_BIRD, GenerationStep.Feature.SURFACE_STRUCTURES);
    }

    private static void addFeatureToBiome(TagKey<Biome> biomeTag, RegistryKey<PlacedFeature> featureResource, GenerationStep.Feature step) {
        BiomeModifications.addFeature(context -> context.hasTag(biomeTag), step, featureResource);
    }
}
