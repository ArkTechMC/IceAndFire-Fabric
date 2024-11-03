package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;
import java.util.function.Function;

public final class IafConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> FIRE_DRAGON_ROOST = registerKey("fire_dragon_roost");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ICE_DRAGON_ROOST = registerKey("ice_dragon_roost");
    public static final RegistryKey<ConfiguredFeature<?, ?>> LIGHTNING_DRAGON_ROOST = registerKey("lightning_dragon_roost");
    //TODO: Should be a structure
    public static final RegistryKey<ConfiguredFeature<?, ?>> CYCLOPS_CAVE = registerKey("cyclops_cave");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PIXIE_VILLAGE = registerKey("pixie_village");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SIREN_ISLAND = registerKey("siren_island");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HYDRA_CAVE = registerKey("hydra_cave");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MYRMEX_HIVE_DESERT = registerKey("myrmex_hive_desert");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MYRMEX_HIVE_JUNGLE = registerKey("myrmex_hive_jungle");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_DEATH_WORM = registerKey("spawn_death_worm");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_L = registerKey("spawn_dragon_skeleton_lightning");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_F = registerKey("spawn_dragon_skeleton_fire");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_I = registerKey("spawn_dragon_skeleton_ice");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_HIPPOCAMPUS = registerKey("spawn_hippocampus");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_SEA_SERPENT = registerKey("spawn_sea_serpent");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_STYMPHALIAN_BIRD = registerKey("spawn_stymphalian_bird");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_WANDERING_CYCLOPS = registerKey("spawn_wandering_cyclops");

    public static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_ORE = registerKey("silver_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SAPPHIRE_ORE = registerKey("sapphire_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FIRE_LILY = registerKey("fire_lily");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FROST_LILY = registerKey("frost_lily");
    public static final RegistryKey<ConfiguredFeature<?, ?>> LIGHTNING_LILY = registerKey("lightning_lily");

    private static final Function<Block, RandomPatchFeatureConfig> flowerConf = (block) -> ConfiguredFeatures.createRandomPatchFeatureConfig(8, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(block.getDefaultState().getBlock()))));

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(IceAndFire.MOD_ID, name));
    }

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        context.register(FIRE_DRAGON_ROOST, new ConfiguredFeature<>(IafFeatures.FIRE_DRAGON_ROOST, FeatureConfig.DEFAULT));
        context.register(ICE_DRAGON_ROOST, new ConfiguredFeature<>(IafFeatures.ICE_DRAGON_ROOST, FeatureConfig.DEFAULT));
        context.register(LIGHTNING_DRAGON_ROOST, new ConfiguredFeature<>(IafFeatures.LIGHTNING_DRAGON_ROOST, FeatureConfig.DEFAULT));
        context.register(CYCLOPS_CAVE, new ConfiguredFeature<>(IafFeatures.CYCLOPS_CAVE, FeatureConfig.DEFAULT));
        context.register(PIXIE_VILLAGE, new ConfiguredFeature<>(IafFeatures.PIXIE_VILLAGE, FeatureConfig.DEFAULT));
        context.register(SIREN_ISLAND, new ConfiguredFeature<>(IafFeatures.SIREN_ISLAND, FeatureConfig.DEFAULT));
        context.register(HYDRA_CAVE, new ConfiguredFeature<>(IafFeatures.HYDRA_CAVE, FeatureConfig.DEFAULT));
        context.register(MYRMEX_HIVE_DESERT, new ConfiguredFeature<>(IafFeatures.MYRMEX_HIVE_DESERT, FeatureConfig.DEFAULT));
        context.register(MYRMEX_HIVE_JUNGLE, new ConfiguredFeature<>(IafFeatures.MYRMEX_HIVE_JUNGLE, FeatureConfig.DEFAULT));
        context.register(SPAWN_DEATH_WORM, new ConfiguredFeature<>(IafFeatures.SPAWN_DEATH_WORM, FeatureConfig.DEFAULT));
        context.register(SPAWN_DRAGON_SKELETON_L, new ConfiguredFeature<>(IafFeatures.SPAWN_DRAGON_SKELETON_L, FeatureConfig.DEFAULT));
        context.register(SPAWN_DRAGON_SKELETON_F, new ConfiguredFeature<>(IafFeatures.SPAWN_DRAGON_SKELETON_F, FeatureConfig.DEFAULT));
        context.register(SPAWN_DRAGON_SKELETON_I, new ConfiguredFeature<>(IafFeatures.SPAWN_DRAGON_SKELETON_I, FeatureConfig.DEFAULT));
        context.register(SPAWN_HIPPOCAMPUS, new ConfiguredFeature<>(IafFeatures.SPAWN_HIPPOCAMPUS, FeatureConfig.DEFAULT));
        context.register(SPAWN_SEA_SERPENT, new ConfiguredFeature<>(IafFeatures.SPAWN_SEA_SERPENT, FeatureConfig.DEFAULT));
        context.register(SPAWN_STYMPHALIAN_BIRD, new ConfiguredFeature<>(IafFeatures.SPAWN_STYMPHALIAN_BIRD, FeatureConfig.DEFAULT));
        context.register(SPAWN_WANDERING_CYCLOPS, new ConfiguredFeature<>(IafFeatures.SPAWN_WANDERING_CYCLOPS, FeatureConfig.DEFAULT));

        RuleTest stoneOreRule = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreRule = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreFeatureConfig.Target> silverOreConfiguration = List.of(OreFeatureConfig.createTarget(stoneOreRule, IafBlocks.SILVER_ORE.getDefaultState()), OreFeatureConfig.createTarget(deepslateOreRule, IafBlocks.DEEPSLATE_SILVER_ORE.getDefaultState()));

        context.register(SILVER_ORE, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(silverOreConfiguration, 4)));

        //TODO: Sapphires should only generate for ice dragon stuff
        context.register(SAPPHIRE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), IafBlocks.SAPPHIRE_ORE.getDefaultState(), 4, 0.5f)));
        //TODO: Look at VegetationFeatures.java
        context.register(FIRE_LILY, new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(IafBlocks.FIRE_LILY)));
        context.register(FROST_LILY, new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(IafBlocks.FROST_LILY)));
        context.register(LIGHTNING_LILY, new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(IafBlocks.LIGHTNING_LILY)));
    }
}
