package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import com.iafenvoy.iceandfire.world.IafWorldData;
import com.iafenvoy.iceandfire.world.feature.*;
import com.iafenvoy.iceandfire.world.gen.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;

public final class IafFeatures {
    //TODO: Should be a structure
    public static final Feature<DefaultFeatureConfig> CYCLOPS_CAVE = register("cyclops_cave", new WorldGenCyclopsCave(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> PIXIE_VILLAGE = register("pixie_village", new WorldGenPixieVillage(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SIREN_ISLAND = register("siren_island", new WorldGenSirenIsland(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> HYDRA_CAVE = register("hydra_cave", new WorldGenHydraCave(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_DEATH_WORM = register("spawn_death_worm", new SpawnDeathWorm(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_DRAGON_SKELETON_L = register("spawn_dragon_skeleton_lightning", new SpawnDragonSkeleton(IafEntities.LIGHTNING_DRAGON, DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_DRAGON_SKELETON_F = register("spawn_dragon_skeleton_fire", new SpawnDragonSkeleton(IafEntities.FIRE_DRAGON, DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_DRAGON_SKELETON_I = register("spawn_dragon_skeleton_ice", new SpawnDragonSkeleton(IafEntities.ICE_DRAGON, DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_HIPPOCAMPUS = register("spawn_hippocampus", new SpawnHippocampus(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_SEA_SERPENT = register("spawn_sea_serpent", new SpawnSeaSerpent(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_STYMPHALIAN_BIRD = register("spawn_stymphalian_bird", new SpawnStymphalianBird(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> SPAWN_WANDERING_CYCLOPS = register("spawn_wandering_cyclops", new SpawnWanderingCyclops(DefaultFeatureConfig.CODEC));

    private static <F extends Feature<? extends FeatureConfig>> F register(String name, F feature) {
        return Registry.register(Registries.FEATURE, new Identifier(IceAndFire.MOD_ID, name), feature);
    }

    public static void init() {
        addFeatures();
    }

    public static boolean isFarEnoughFromSpawn(final WorldAccess level, final BlockPos position) {
        WorldProperties spawnPoint = level.getLevelProperties();
        BlockPos spawnRelative = new BlockPos(spawnPoint.getSpawnX(), position.getY(), spawnPoint.getSpawnY());
        return !spawnRelative.isWithinDistance(position, IafCommonConfig.INSTANCE.worldGen.dangerousDistanceLimit.getValue().floatValue());
    }

    public static boolean isFarEnoughFromDangerousGen(final ServerWorldAccess level, final BlockPos position, final String id) {
        return isFarEnoughFromDangerousGen(level, position, id, IafWorldData.FeatureType.SURFACE);
    }

    public static boolean isFarEnoughFromDangerousGen(final ServerWorldAccess level, final BlockPos position, final String id, final IafWorldData.FeatureType type) {
        IafWorldData data = IafWorldData.get(level.toServerWorld());
        return data.check(type, position, id);
    }

    public static void addFeatures() {
        addFeatureToBiome(IafBiomeTags.FIRE, IafPlacedFeatures.PLACED_FIRE_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.ICE, IafPlacedFeatures.PLACED_FROST_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.LIGHTNING, IafPlacedFeatures.PLACED_LIGHTNING_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.FIRE, IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_F, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.ICE, IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_I, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.LIGHTNING, IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_L, GenerationStep.Feature.SURFACE_STRUCTURES);

        addFeatureToBiome(IafBiomeTags.SILVER_ORE, IafPlacedFeatures.PLACED_SILVER_ORE, GenerationStep.Feature.UNDERGROUND_ORES);
        addFeatureToBiome(IafBiomeTags.SAPPHIRE_ORE, IafPlacedFeatures.PLACED_SAPPHIRE_ORE, GenerationStep.Feature.UNDERGROUND_ORES);

        addFeatureToBiome(IafBiomeTags.CYCLOPS_CAVE, IafPlacedFeatures.PLACED_CYCLOPS_CAVE, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.PIXIE_VILLAGE, IafPlacedFeatures.PLACED_PIXIE_VILLAGE, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.HYDRA_CAVE, IafPlacedFeatures.PLACED_HYDRA_CAVE, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.SIREN_ISLAND, IafPlacedFeatures.PLACED_SIREN_ISLAND, GenerationStep.Feature.SURFACE_STRUCTURES);

        addFeatureToBiome(IafBiomeTags.DEATHWORM, IafPlacedFeatures.PLACED_SPAWN_DEATH_WORM, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.WANDERING_CYCLOPS, IafPlacedFeatures.PLACED_SPAWN_WANDERING_CYCLOPS, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.HIPPOCAMPUS, IafPlacedFeatures.PLACED_SPAWN_HIPPOCAMPUS, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.SEA_SERPENT, IafPlacedFeatures.PLACED_SPAWN_SEA_SERPENT, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.STYMPHALIAN_BIRD, IafPlacedFeatures.PLACED_SPAWN_STYMPHALIAN_BIRD, GenerationStep.Feature.SURFACE_STRUCTURES);
    }

    private static void addFeatureToBiome(TagKey<Biome> biomeTag, RegistryKey<PlacedFeature> featureResource, GenerationStep.Feature step) {
        BiomeModifications.addFeature(context -> context.hasTag(biomeTag), step, featureResource);
    }
}
