package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.datagen.IafPlacedFeatures;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.feature.*;
import com.github.alexthe666.iceandfire.world.gen.*;
import dev.arktechmc.iafextra.util.IdUtil;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
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
    public static HashMap<String, Boolean> LOADED_FEATURES;
    // Only a global variable because it's too bothersome to add it to the method call (alternative: method returns identifier or null)
    private static List<String> ADDED_FEATURES;

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

    public static void addFeatures(RegistryEntry<Biome> biome, HashMap<String, RegistryEntry<PlacedFeature>> features) {
        ADDED_FEATURES = new ArrayList<>();

        if (safelyTestBiome(BiomeConfig.fireLilyBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.fireLilyBiomes.getRight(), IafPlacedFeatures.PLACED_FIRE_LILY, features, GenerationStep.Feature.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.lightningLilyBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.lightningLilyBiomes.getRight(), IafPlacedFeatures.PLACED_LIGHTNING_LILY, features, GenerationStep.Feature.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.frostLilyBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.frostLilyBiomes.getRight(), IafPlacedFeatures.PLACED_FROST_LILY, features, GenerationStep.Feature.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.oreGenBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.oreGenBiomes.getRight(), IafPlacedFeatures.PLACED_SILVER_ORE, features, GenerationStep.Feature.UNDERGROUND_ORES);
        }
        if (safelyTestBiome(BiomeConfig.sapphireBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.sapphireBiomes.getRight(), IafPlacedFeatures.PLACED_SAPPHIRE_ORE, features, GenerationStep.Feature.UNDERGROUND_ORES);
        }


        if (safelyTestBiome(BiomeConfig.fireDragonBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.fireDragonBiomes.getRight(), IafPlacedFeatures.PLACED_FIRE_DRAGON_ROOST, features);
        }
        if (safelyTestBiome(BiomeConfig.lightningDragonBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.lightningDragonBiomes.getRight(), IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_ROOST, features);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.iceDragonBiomes.getRight(), IafPlacedFeatures.PLACED_ICE_DRAGON_ROOST, features);
        }


        if (safelyTestBiome(BiomeConfig.fireDragonCaveBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.fireDragonCaveBiomes.getRight(), IafPlacedFeatures.PLACED_FIRE_DRAGON_CAVE, features, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        }
        if (safelyTestBiome(BiomeConfig.lightningDragonCaveBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.lightningDragonCaveBiomes.getRight(), IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_CAVE, features, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonCaveBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.iceDragonCaveBiomes.getRight(), IafPlacedFeatures.PLACED_ICE_DRAGON_CAVE, features, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
        }


        if (safelyTestBiome(BiomeConfig.cyclopsCaveBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.cyclopsCaveBiomes.getRight(), IafPlacedFeatures.PLACED_CYCLOPS_CAVE, features);
        }
        if (safelyTestBiome(BiomeConfig.pixieBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.pixieBiomes.getRight(), IafPlacedFeatures.PLACED_PIXIE_VILLAGE, features);
        }
        if (safelyTestBiome(BiomeConfig.hydraBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.hydraBiomes.getRight(), IafPlacedFeatures.PLACED_HYDRA_CAVE, features);
        }
        if (safelyTestBiome(BiomeConfig.desertMyrmexBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.desertMyrmexBiomes.getRight(), IafPlacedFeatures.PLACED_MYRMEX_HIVE_DESERT, features);
        }
        if (safelyTestBiome(BiomeConfig.jungleMyrmexBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.jungleMyrmexBiomes.getRight(), IafPlacedFeatures.PLACED_MYRMEX_HIVE_JUNGLE, features);
        }
        if (safelyTestBiome(BiomeConfig.sirenBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.sirenBiomes.getRight(), IafPlacedFeatures.PLACED_SIREN_ISLAND, features);
        }
        if (safelyTestBiome(BiomeConfig.deathwormBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.deathwormBiomes.getRight(), IafPlacedFeatures.PLACED_SPAWN_DEATH_WORM, features);
        }
        if (safelyTestBiome(BiomeConfig.wanderingCyclopsBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.wanderingCyclopsBiomes.getRight(), IafPlacedFeatures.PLACED_SPAWN_WANDERING_CYCLOPS, features);
        }

        if (safelyTestBiome(BiomeConfig.lightningDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.lightningDragonSkeletonBiomes.getRight(), IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_L, features);
        }
        if (safelyTestBiome(BiomeConfig.fireDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.fireDragonSkeletonBiomes.getRight(), IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_F, features);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.iceDragonSkeletonBiomes.getRight(), IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_I, features);
        }

        if (safelyTestBiome(BiomeConfig.hippocampusBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.hippocampusBiomes.getRight(), IafPlacedFeatures.PLACED_SPAWN_HIPPOCAMPUS, features);
        }
        if (safelyTestBiome(BiomeConfig.seaSerpentBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.seaSerpentBiomes.getRight(), IafPlacedFeatures.PLACED_SPAWN_SEA_SERPENT, features);
        }
        if (safelyTestBiome(BiomeConfig.stymphalianBiomes, biome)) {
            addFeatureToBiome(BiomeConfig.stymphalianBiomes.getRight(), IafPlacedFeatures.PLACED_SPAWN_STYMPHALIAN_BIRD, features);
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

    private static void addFeatureToBiome(SpawnBiomeData data, RegistryKey<PlacedFeature> feature, HashMap<String, RegistryEntry<PlacedFeature>> features) {
        addFeatureToBiome(data, feature, features, GenerationStep.Feature.SURFACE_STRUCTURES);
    }

    private static void addFeatureToBiome(SpawnBiomeData data, RegistryKey<PlacedFeature> featureResource, HashMap<String, RegistryEntry<PlacedFeature>> features, GenerationStep.Feature step) {
        String identifier = featureResource.getValue().toString();
        RegistryEntry<PlacedFeature> feature = features.get(identifier);

        if (feature != null) {
            BiomeModifications.addFeature(context -> data.matches(context.getBiomeRegistryEntry(), context.getBiomeKey().getValue()), step, featureResource);
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
