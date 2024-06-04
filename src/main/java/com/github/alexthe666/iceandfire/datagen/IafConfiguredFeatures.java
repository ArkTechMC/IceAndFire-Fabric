package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
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
    public static final RegistryKey<ConfiguredFeature<?, ?>> FIRE_DRAGON_CAVE = registerKey("fire_dragon_cave");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ICE_DRAGON_CAVE = registerKey("ice_dragon_cave");
    public static final RegistryKey<ConfiguredFeature<?, ?>> LIGHTNING_DRAGON_CAVE = registerKey("lightning_dragon_cave");
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
        context.register(FIRE_DRAGON_ROOST, new ConfiguredFeature<>(IafWorldRegistry.FIRE_DRAGON_ROOST.get(), FeatureConfig.DEFAULT));
        context.register(ICE_DRAGON_ROOST, new ConfiguredFeature<>(IafWorldRegistry.ICE_DRAGON_ROOST.get(), FeatureConfig.DEFAULT));
        context.register(LIGHTNING_DRAGON_ROOST, new ConfiguredFeature<>(IafWorldRegistry.LIGHTNING_DRAGON_ROOST.get(), FeatureConfig.DEFAULT));
        context.register(FIRE_DRAGON_CAVE, new ConfiguredFeature<>(IafWorldRegistry.FIRE_DRAGON_CAVE.get(), FeatureConfig.DEFAULT));
        context.register(ICE_DRAGON_CAVE, new ConfiguredFeature<>(IafWorldRegistry.ICE_DRAGON_CAVE.get(), FeatureConfig.DEFAULT));
        context.register(LIGHTNING_DRAGON_CAVE, new ConfiguredFeature<>(IafWorldRegistry.LIGHTNING_DRAGON_CAVE.get(), FeatureConfig.DEFAULT));
        context.register(CYCLOPS_CAVE, new ConfiguredFeature<>(IafWorldRegistry.CYCLOPS_CAVE.get(), FeatureConfig.DEFAULT));
        context.register(PIXIE_VILLAGE, new ConfiguredFeature<>(IafWorldRegistry.PIXIE_VILLAGE.get(), FeatureConfig.DEFAULT));
        context.register(SIREN_ISLAND, new ConfiguredFeature<>(IafWorldRegistry.SIREN_ISLAND.get(), FeatureConfig.DEFAULT));
        context.register(HYDRA_CAVE, new ConfiguredFeature<>(IafWorldRegistry.HYDRA_CAVE.get(), FeatureConfig.DEFAULT));
        context.register(MYRMEX_HIVE_DESERT, new ConfiguredFeature<>(IafWorldRegistry.MYRMEX_HIVE_DESERT.get(), FeatureConfig.DEFAULT));
        context.register(MYRMEX_HIVE_JUNGLE, new ConfiguredFeature<>(IafWorldRegistry.MYRMEX_HIVE_JUNGLE.get(), FeatureConfig.DEFAULT));
        context.register(SPAWN_DEATH_WORM, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DEATH_WORM.get(), FeatureConfig.DEFAULT));
        context.register(SPAWN_DRAGON_SKELETON_L, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_L.get(), FeatureConfig.DEFAULT));
        context.register(SPAWN_DRAGON_SKELETON_F, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_F.get(), FeatureConfig.DEFAULT));
        context.register(SPAWN_DRAGON_SKELETON_I, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_I.get(), FeatureConfig.DEFAULT));
        context.register(SPAWN_HIPPOCAMPUS, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_HIPPOCAMPUS.get(), FeatureConfig.DEFAULT));
        context.register(SPAWN_SEA_SERPENT, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_SEA_SERPENT.get(), FeatureConfig.DEFAULT));
        context.register(SPAWN_STYMPHALIAN_BIRD, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_STYMPHALIAN_BIRD.get(), FeatureConfig.DEFAULT));
        context.register(SPAWN_WANDERING_CYCLOPS, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_WANDERING_CYCLOPS.get(), FeatureConfig.DEFAULT));

        RuleTest stoneOreRule = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreRule = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreFeatureConfig.Target> silverOreConfiguration = List.of(OreFeatureConfig.createTarget(stoneOreRule, IafBlockRegistry.SILVER_ORE.get().getDefaultState()), OreFeatureConfig.createTarget(deepslateOreRule, IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().getDefaultState()));

        context.register(SILVER_ORE, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(silverOreConfiguration, 4)));

        //TODO: Sapphires should only generate for ice dragon stuff
        context.register(SAPPHIRE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), IafBlockRegistry.SAPPHIRE_ORE.get().getDefaultState(), 4, 0.5f)));
        //TODO: Look at VegetationFeatures.java
        context.register(FIRE_LILY, new ConfiguredFeature(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.FIRE_LILY.get())));
        context.register(FROST_LILY, new ConfiguredFeature(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.FROST_LILY.get())));
        context.register(LIGHTNING_LILY, new ConfiguredFeature(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.LIGHTNING_LILY.get())));

    }

}
