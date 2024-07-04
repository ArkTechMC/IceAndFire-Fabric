package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.citadel.config.biome.SpawnBiomeData;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import com.iafenvoy.iceandfire.world.IafWorldData;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class IafFeatures {
    public static final RegistryKey<PlacedFeature> PLACED_FIRE_DRAGON_ROOST = registerKey("fire_dragon_roost");
    public static final RegistryKey<PlacedFeature> PLACED_ICE_DRAGON_ROOST = registerKey("ice_dragon_roost");
    public static final RegistryKey<PlacedFeature> PLACED_LIGHTNING_DRAGON_ROOST = registerKey("lightning_dragon_roost");
    public static final RegistryKey<PlacedFeature> PLACED_FIRE_DRAGON_CAVE = registerKey("fire_dragon_cave");
    public static final RegistryKey<PlacedFeature> PLACED_ICE_DRAGON_CAVE = registerKey("ice_dragon_cave");
    public static final RegistryKey<PlacedFeature> PLACED_LIGHTNING_DRAGON_CAVE = registerKey("lightning_dragon_cave");
    //TODO: Should be a structure
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

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(IceAndFire.MOD_ID, name));
    }

    public static void init() {
        addFeatures();
    }

    public static boolean isFarEnoughFromSpawn(final WorldAccess level, final BlockPos position) {
        WorldProperties spawnPoint = level.getLevelProperties();
        BlockPos spawnRelative = new BlockPos(spawnPoint.getSpawnX(), position.getY(), spawnPoint.getSpawnY());
        return !spawnRelative.isWithinDistance(position, IafConfig.getInstance().worldGen.dangerousDistanceLimit);
    }

    public static boolean isFarEnoughFromDangerousGen(final ServerWorldAccess level, final BlockPos position, final String id) {
        return isFarEnoughFromDangerousGen(level, position, id, IafWorldData.FeatureType.SURFACE);
    }

    public static boolean isFarEnoughFromDangerousGen(final ServerWorldAccess level, final BlockPos position, final String id, final IafWorldData.FeatureType type) {
        IafWorldData data = IafWorldData.get(level.toServerWorld());
        return data.check(type, position, id);
    }

    public static void addFeatures() {
        addFeatureToBiome(IafBiomeTags.FIRE, PLACED_FIRE_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.ICE, PLACED_FROST_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.LIGHTENING, PLACED_LIGHTNING_LILY, GenerationStep.Feature.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.FIRE, PLACED_FIRE_DRAGON_ROOST, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.ICE, PLACED_ICE_DRAGON_ROOST, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.LIGHTENING, PLACED_LIGHTNING_DRAGON_ROOST, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.FIRE, PLACED_FIRE_DRAGON_CAVE, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.ICE, PLACED_ICE_DRAGON_CAVE, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.LIGHTENING, PLACED_LIGHTNING_DRAGON_CAVE, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.FIRE, PLACED_SPAWN_DRAGON_SKELETON_F, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.ICE, PLACED_SPAWN_DRAGON_SKELETON_I, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.LIGHTENING, PLACED_SPAWN_DRAGON_SKELETON_L, GenerationStep.Feature.SURFACE_STRUCTURES);

        addFeatureToBiome(IafBiomeTags.SILVER_ORE, PLACED_SILVER_ORE, GenerationStep.Feature.UNDERGROUND_ORES);
        addFeatureToBiome(IafBiomeTags.SAPPHIRE_ORE, PLACED_SAPPHIRE_ORE, GenerationStep.Feature.UNDERGROUND_ORES);

        addFeatureToBiome(IafBiomeTags.CYCLOPS_CAVE, PLACED_CYCLOPS_CAVE, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.PIXIE_VILLAGE, PLACED_PIXIE_VILLAGE, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.HYDRA_CAVE, PLACED_HYDRA_CAVE, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.MYRMEX_HIVE_DESERT, PLACED_MYRMEX_HIVE_DESERT, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.MYRMEX_HIVE_JUNGLE, PLACED_MYRMEX_HIVE_JUNGLE, GenerationStep.Feature.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.SIREN_ISLAND, PLACED_SIREN_ISLAND, GenerationStep.Feature.SURFACE_STRUCTURES);

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
