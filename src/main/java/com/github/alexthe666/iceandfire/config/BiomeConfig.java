package com.github.alexthe666.iceandfire.config;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeConfig;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.util.IdUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BiomeConfig {
    public static Pair<String, SpawnBiomeData> oreGenBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "ore_gen_biomes"), DefaultBiomes.OVERWORLD);
    public static Pair<String, SpawnBiomeData> sapphireBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "sapphire_gen_biomes"), DefaultBiomes.VERY_SNOWY);
    public static Pair<String, SpawnBiomeData> fireLilyBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "fire_lily_biomes"), DefaultBiomes.VERY_HOT);
    public static Pair<String, SpawnBiomeData> frostLilyBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "frost_lily_biomes"), DefaultBiomes.VERY_SNOWY);
    public static Pair<String, SpawnBiomeData> lightningLilyBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "lightning_lily_biomes"), DefaultBiomes.SAVANNAS);
    public static Pair<String, SpawnBiomeData> fireDragonBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "fire_dragon_biomes"), DefaultBiomes.FIREDRAGON_ROOST);
    public static Pair<String, SpawnBiomeData> fireDragonCaveBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "fire_dragon_cave_biomes"), DefaultBiomes.FIREDRAGON_CAVE);
    public static Pair<String, SpawnBiomeData> iceDragonBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "ice_dragon_biomes"), DefaultBiomes.ICEDRAGON_ROOST);
    public static Pair<String, SpawnBiomeData> iceDragonCaveBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "ice_dragon_cave_biomes"), DefaultBiomes.ICEDRAGON_CAVE);
    public static Pair<String, SpawnBiomeData> lightningDragonBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "lightning_dragon_biomes"), DefaultBiomes.LIGHTNING_ROOST);
    public static Pair<String, SpawnBiomeData> lightningDragonCaveBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "lightning_dragon_cave_biomes"), DefaultBiomes.LIGHTNING_CAVE);

    public static Pair<String, SpawnBiomeData> cyclopsCaveBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "cyclops_cave_biomes"), DefaultBiomes.BEACHES);
    public static Pair<String, SpawnBiomeData> hippogryphBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippogryph_biomes"), DefaultBiomes.HILLS);
    public static Pair<String, SpawnBiomeData> pixieBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "pixie_village_biomes"), DefaultBiomes.PIXIES);
    public static Pair<String, SpawnBiomeData> hippocampusBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippocampus_biomes"), DefaultBiomes.OCEANS);
    public static Pair<String, SpawnBiomeData> seaSerpentBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "sea_serpent_biomes"), DefaultBiomes.OCEANS);
    public static Pair<String, SpawnBiomeData> sirenBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "siren_biomes"), DefaultBiomes.OCEANS);
    public static Pair<String, SpawnBiomeData> amphithereBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "amphithere_biomes"), DefaultBiomes.JUNGLE);
    public static Pair<String, SpawnBiomeData> desertMyrmexBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "desert_myrmex_biomes"), DefaultBiomes.DESERT);
    public static Pair<String, SpawnBiomeData> jungleMyrmexBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "jungle_myrmex_biomes"), DefaultBiomes.JUNGLE);
    public static Pair<String, SpawnBiomeData> snowyTrollBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "snowy_troll_biomes"), DefaultBiomes.SNOWY);
    public static Pair<String, SpawnBiomeData> forestTrollBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "forest_troll_biomes"), DefaultBiomes.WOODLAND);
    public static Pair<String, SpawnBiomeData> mountainTrollBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "mountain_troll_biomes"), DefaultBiomes.VERY_HILLY);

    public static Pair<String, SpawnBiomeData> stymphalianBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "stymphalian_bird_biomes"), DefaultBiomes.SWAMPS);
    public static Pair<String, SpawnBiomeData> hydraBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hydra_cave_biomes"), DefaultBiomes.SWAMPS);

    public static Pair<String, SpawnBiomeData> mausoleumBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "mausoleum_biomes"), DefaultBiomes.MAUSOLEUM);
    public static Pair<String, SpawnBiomeData> graveyardBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "graveyard_biomes"), DefaultBiomes.GRAVEYARD);
    public static Pair<String, SpawnBiomeData> gorgonTempleBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "gorgon_temple_biomes"), DefaultBiomes.BEACHES);

    public static Pair<String, SpawnBiomeData> cockatriceBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "cockatrice_biomes"), DefaultBiomes.SAVANNAS);
    public static Pair<String, SpawnBiomeData> deathwormBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "deathworm_biomes"), DefaultBiomes.DESERT);
    public static Pair<String, SpawnBiomeData> wanderingCyclopsBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "wandering_cyclops_biomes"), DefaultBiomes.PLAINS);
    public static Pair<String, SpawnBiomeData> lightningDragonSkeletonBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "lightning_dragon_skeleton_biomes"), DefaultBiomes.SAVANNAS);
    public static Pair<String, SpawnBiomeData> fireDragonSkeletonBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "fire_dragon_skeleton_biomes"), DefaultBiomes.DESERT);
    public static Pair<String, SpawnBiomeData> iceDragonSkeletonBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "ice_dragon_skeleton_biomes"), DefaultBiomes.VERY_SNOWY);

    public static Pair<String, SpawnBiomeData> blackHippogryphBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippogryph_black_biomes"), DefaultBiomes.HIPPOGRYPH_BLACK);
    public static Pair<String, SpawnBiomeData> brownHippogryphBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippogryph_brown_biomes"), DefaultBiomes.VERY_HILLY);
    public static Pair<String, SpawnBiomeData> grayHippogryphBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippogryph_gray_biomes"), DefaultBiomes.HIPPOGRYPH_GRAY);
    public static Pair<String, SpawnBiomeData> chestnutHippogryphBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippogryph_chestnut_biomes"), DefaultBiomes.HIPPOGRYPH_CHESTNUT);
    public static Pair<String, SpawnBiomeData> creamyHippogryphBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippogryph_creamy_biomes"), DefaultBiomes.HIPPOGRYPH_CREAMY);
    public static Pair<String, SpawnBiomeData> darkBrownHippogryphBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippogryph_dark_brown_biomes"), DefaultBiomes.HIPPOGRYPH_DARK_BROWN);
    public static Pair<String, SpawnBiomeData> whiteHippogryphBiomes = Pair.of(IdUtil.build(IceAndFire.MOD_ID, "hippogryph_white_biomes"), DefaultBiomes.HIPPOGRYPH_WHITE);

    private static boolean init = false;
    private static final Map<String, SpawnBiomeData> biomeConfigValues = new HashMap<>();

    public static void init() {
        try {
            for (Field f : BiomeConfig.class.getFields()) {
                Object obj = f.get(null);
                if (obj instanceof Pair) {
                    String id = (String) ((Pair<?, ?>) obj).getLeft();
                    SpawnBiomeData data = (SpawnBiomeData) ((Pair<?, ?>) obj).getRight();
                    biomeConfigValues.put(id, SpawnBiomeConfig.create(new Identifier(id), data));
                }
            }
        } catch (Exception e) {
            IceAndFire.LOGGER.warn("Encountered error building {} biome config .json files", IceAndFire.MOD_ID);
            e.printStackTrace();
        }
        init = true;
    }

    private static Identifier getBiomeName(RegistryEntry<Biome> biome) {
        return biome.getKeyOrValue().map(RegistryKey::getValue, (noKey) -> null);
    }

    public static boolean test(Pair<String, SpawnBiomeData> entry, RegistryEntry<Biome> biome, Identifier name) {
        if (!init) {
            init();
        }
        return biomeConfigValues.get(entry.getKey()).matches(biome, name);
    }

    public static boolean test(Pair<String, SpawnBiomeData> entry, RegistryEntry<Biome> biome) {
        return BiomeConfig.test(entry, biome, getBiomeName(biome));
    }

    public static boolean test(Pair<String, SpawnBiomeData> entry, RegistryEntry.Reference<Biome> biome) {
        return test(entry, biome, biome.registryKey().getValue());
    }

}
