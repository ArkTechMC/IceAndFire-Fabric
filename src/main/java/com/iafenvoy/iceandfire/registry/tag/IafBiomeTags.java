package com.iafenvoy.iceandfire.registry.tag;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class IafBiomeTags {
    public static final TagKey<Biome> GORGON_TEMPLE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/gorgon_temple"));
    public static final TagKey<Biome> MAUSOLEUM = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/mausoleum"));
    public static final TagKey<Biome> NO_GRAVEYARD = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/no_graveyard"));
    public static final TagKey<Biome> FIRE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/fire"));
    public static final TagKey<Biome> ICE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/ice"));
    public static final TagKey<Biome> LIGHTENING = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/lightening"));
    public static final TagKey<Biome> CYCLOPS_CAVE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/cyclops_cave"));
    public static final TagKey<Biome> HYDRA_CAVE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/hydra_cave"));
    public static final TagKey<Biome> PIXIE_VILLAGE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/pixie_village"));
    public static final TagKey<Biome> MYRMEX_HIVE_DESERT = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/myrmex_hive_desert"));
    public static final TagKey<Biome> MYRMEX_HIVE_JUNGLE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/myrmex_hive_jungle"));
    public static final TagKey<Biome> SIREN_ISLAND = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/siren_island"));

    public static final TagKey<Biome> SILVER_ORE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "ore_gen/silver"));
    public static final TagKey<Biome> SAPPHIRE_ORE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "ore_gen/sapphire"));

    public static final TagKey<Biome> DEATHWORM = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/deathworm"));
    public static final TagKey<Biome> WANDERING_CYCLOPS = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/wandering_cyclops"));
    public static final TagKey<Biome> HIPPOCAMPUS = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippocampus"));
    public static final TagKey<Biome> SEA_SERPENT = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/sea_serpent"));
    public static final TagKey<Biome> STYMPHALIAN_BIRD = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/stymphalian_bird"));
    public static final TagKey<Biome> HIPPOGRYPH = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippogryph"));
    public static final TagKey<Biome> HIPPOGRYPH_BLACK = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippogryph_black"));
    public static final TagKey<Biome> HIPPOGRYPH_BROWN = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippogryph_brown"));
    public static final TagKey<Biome> HIPPOGRYPH_CHESTNUT = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippogryph_chestnut"));
    public static final TagKey<Biome> HIPPOGRYPH_CREAMY = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippogryph_creamy"));
    public static final TagKey<Biome> HIPPOGRYPH_DARK_BROWN = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippogryph_dark_brown"));
    public static final TagKey<Biome> HIPPOGRYPH_GRAY = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippogryph_gray"));
    public static final TagKey<Biome> HIPPOGRYPH_WHITE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/hippogryph_white"));
    public static final TagKey<Biome> COCKATRICE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/cockatrice"));
    public static final TagKey<Biome> AMPHITHERE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/amphithere"));
    public static final TagKey<Biome> TROLL = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/troll"));
    public static final TagKey<Biome> FOREST_TROLL = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/troll_forest"));
    public static final TagKey<Biome> MOUNTAIN_TROLL = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/troll_mountain"));
    public static final TagKey<Biome> SNOWY_TROLL = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "entity_gen/troll_snowy"));
}
