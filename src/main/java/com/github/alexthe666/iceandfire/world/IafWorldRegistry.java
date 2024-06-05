package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.datagen.IafPlacedFeatures;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.iafenvoy.iafextra.util.IdUtil;
import com.github.alexthe666.iceandfire.world.feature.*;
import com.github.alexthe666.iceandfire.world.gen.*;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
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
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class IafWorldRegistry {
    public static final LazyRegistrar<Feature<?>> FEATURES = LazyRegistrar.create(Registries.FEATURE, IceAndFire.MOD_ID);

    public static final RegistryObject<Feature<DefaultFeatureConfig>> FIRE_DRAGON_ROOST = register("fire_dragon_roost", () -> new WorldGenFireDragonRoosts(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> ICE_DRAGON_ROOST = register("ice_dragon_roost", () -> new WorldGenIceDragonRoosts(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> LIGHTNING_DRAGON_ROOST = register("lightning_dragon_roost", () -> new WorldGenLightningDragonRoosts(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> FIRE_DRAGON_CAVE = register("fire_dragon_cave", () -> new WorldGenFireDragonCave(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> ICE_DRAGON_CAVE = register("ice_dragon_cave", () -> new WorldGenIceDragonCave(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> LIGHTNING_DRAGON_CAVE = register("lightning_dragon_cave",
            () -> new WorldGenLightningDragonCave(DefaultFeatureConfig.CODEC));
    //TODO: Should be a structure
    public static final RegistryObject<Feature<DefaultFeatureConfig>> CYCLOPS_CAVE = register("cyclops_cave", () -> new WorldGenCyclopsCave(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> PIXIE_VILLAGE = register("pixie_village", () -> new WorldGenPixieVillage(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SIREN_ISLAND = register("siren_island", () -> new WorldGenSirenIsland(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> HYDRA_CAVE = register("hydra_cave", () -> new WorldGenHydraCave(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> MYRMEX_HIVE_DESERT = register("myrmex_hive_desert", () -> new WorldGenMyrmexHive(false, false, DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> MYRMEX_HIVE_JUNGLE = register("myrmex_hive_jungle", () -> new WorldGenMyrmexHive(false, true, DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SPAWN_DEATH_WORM = register("spawn_death_worm", () -> new SpawnDeathWorm(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SPAWN_DRAGON_SKELETON_L = register("spawn_dragon_skeleton_lightning",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.LIGHTNING_DRAGON.get(), DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SPAWN_DRAGON_SKELETON_F = register("spawn_dragon_skeleton_fire",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.FIRE_DRAGON.get(), DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SPAWN_DRAGON_SKELETON_I = register("spawn_dragon_skeleton_ice",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.ICE_DRAGON.get(), DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SPAWN_HIPPOCAMPUS = register("spawn_hippocampus", () -> new SpawnHippocampus(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SPAWN_SEA_SERPENT = register("spawn_sea_serpent", () -> new SpawnSeaSerpent(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SPAWN_STYMPHALIAN_BIRD = register("spawn_stymphalian_bird", () -> new SpawnStymphalianBird(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> SPAWN_WANDERING_CYCLOPS = register("spawn_wandering_cyclops", () -> new SpawnWanderingCyclops(DefaultFeatureConfig.CODEC));


    private static <C extends FeatureConfig, F extends Feature<C>> RegistryObject<F> register(final String name, final Supplier<? extends F> supplier) {
        return FEATURES.register(name, supplier);
    }

    public static boolean isFarEnoughFromSpawn(final WorldAccess level, final BlockPos position) {
        WorldProperties spawnPoint = level.getLevelProperties();
        BlockPos spawnRelative = new BlockPos(spawnPoint.getSpawnX(), position.getY(), spawnPoint.getSpawnY());
        return !spawnRelative.isWithinDistance(position, IafConfig.dangerousWorldGenDistanceLimit);
    }

    public static boolean isFarEnoughFromDangerousGen(final ServerWorldAccess level, final BlockPos position, final String id) {
        return isFarEnoughFromDangerousGen(level, position, id, IafWorldData.FeatureType.SURFACE);
    }

    public static boolean isFarEnoughFromDangerousGen(final ServerWorldAccess level, final BlockPos position, final String id, final IafWorldData.FeatureType type) {
        IafWorldData data = IafWorldData.get(level.toServerWorld());
        return data.check(type, position, id);
    }

    public static HashMap<String, Boolean> LOADED_FEATURES;

    static {
        LOADED_FEATURES = new HashMap<>();
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "fire_lily"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "frost_lily"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "lightning_lily"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "silver_ore"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "sapphire_ore"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "fire_dragon_roost"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "ice_dragon_roost"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "lightning_dragon_roost"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "fire_dragon_cave"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "ice_dragon_cave"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "lightning_dragon_cave"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "cyclops_cave"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "pixie_village"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "siren_island"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "hydra_cave"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "myrmex_hive_desert"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "myrmex_hive_jungle"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "spawn_death_worm"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "spawn_dragon_skeleton_lightning"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "spawn_dragon_skeleton_fire"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "spawn_dragon_skeleton_ice"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "spawn_hippocampus"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "spawn_sea_serpent"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "spawn_stymphalian_bird"), false);
        LOADED_FEATURES.put(IdUtil.build(IceAndFire.MOD_ID, "spawn_wandering_cyclops"), false);
    }

    // Only a global variable because it's too bothersome to add it to the method call (alternative: method returns identifier or null)
    private static List<String> ADDED_FEATURES;

    public static void addFeatures(RegistryEntry<Biome> biome, HashMap<String, RegistryEntry<PlacedFeature>> features, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        ADDED_FEATURES = new ArrayList<>();

        if (safelyTestBiome(BiomeConfig.fireLilyBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_FIRE_LILY, features, builder, GenerationStep.Feature.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.lightningLilyBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_LIGHTNING_LILY, features, builder, GenerationStep.Feature.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.frostLilyBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_FROST_LILY, features, builder, GenerationStep.Feature.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.oreGenBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SILVER_ORE, features, builder, GenerationStep.Feature.UNDERGROUND_ORES);
        }
        if (safelyTestBiome(BiomeConfig.sapphireBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SAPPHIRE_ORE, features, builder, GenerationStep.Feature.UNDERGROUND_ORES);
        }


        if (safelyTestBiome(BiomeConfig.fireDragonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_FIRE_DRAGON_ROOST, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.lightningDragonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_ROOST, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_ICE_DRAGON_ROOST, features, builder);
        }


        if (safelyTestBiome(BiomeConfig.fireDragonCaveBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_FIRE_DRAGON_CAVE, features, builder, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        }
        if (safelyTestBiome(BiomeConfig.lightningDragonCaveBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_CAVE, features, builder, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonCaveBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_ICE_DRAGON_CAVE, features, builder, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        }


        if (safelyTestBiome(BiomeConfig.cyclopsCaveBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_CYCLOPS_CAVE, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.pixieBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_PIXIE_VILLAGE, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.hydraBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_HYDRA_CAVE, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.desertMyrmexBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_MYRMEX_HIVE_DESERT, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.jungleMyrmexBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_MYRMEX_HIVE_JUNGLE, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.sirenBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SIREN_ISLAND, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.deathwormBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_DEATH_WORM, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.wanderingCyclopsBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_WANDERING_CYCLOPS, features, builder);
        }

        if (safelyTestBiome(BiomeConfig.lightningDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_L, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.fireDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_F, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_I, features, builder);
        }

        if (safelyTestBiome(BiomeConfig.hippocampusBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_HIPPOCAMPUS, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.seaSerpentBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_SEA_SERPENT, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.stymphalianBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_STYMPHALIAN_BIRD, features, builder);
        }

        if (!ADDED_FEATURES.isEmpty()) {
            StringBuilder featureList = new StringBuilder();

            for (String feature : ADDED_FEATURES) {
                featureList.append("\n").append("\t- ").append(feature);
            }

            IceAndFire.LOGGER.debug("Added the following features to the biome [{}]: {}", biome.getKey().get().getValue(), featureList);
        }

        ADDED_FEATURES = null;
    }

    private static void addFeatureToBiome(RegistryKey<PlacedFeature> feature, HashMap<String, RegistryEntry<PlacedFeature>> features, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        addFeatureToBiome(feature, features, builder, GenerationStep.Feature.SURFACE_STRUCTURES);
    }

    private static void addFeatureToBiome(RegistryKey<PlacedFeature> featureResource, HashMap<String, RegistryEntry<PlacedFeature>> features, ModifiableBiomeInfo.BiomeInfo.Builder builder, GenerationStep.Feature step) {
        String identifier = featureResource.getValue().toString();
        RegistryEntry<PlacedFeature> feature = features.get(identifier);

        if (feature != null) {
            builder.getGenerationSettings().getFeatures(step).add(feature);
            LOADED_FEATURES.put(identifier, true);
            ADDED_FEATURES.add(identifier);
        } else {
            IceAndFire.LOGGER.warn("Feature [{}] could not be found", identifier);
        }
    }


    private static boolean safelyTestBiome(Pair<String, SpawnBiomeData> entry, RegistryEntry<Biome> biomeHolder) {
        try {
            return BiomeConfig.test(entry, biomeHolder);
        } catch (Exception e) {
            return false;
        }
    }

}
