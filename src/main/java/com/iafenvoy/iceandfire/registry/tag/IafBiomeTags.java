package com.iafenvoy.iceandfire.registry.tag;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class IafBiomeTags {
    public static final TagKey<Biome> GORGON_TEMPLE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/gorgon_temple"));
    public static final TagKey<Biome> MAUSOLEUM = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/mausoleum"));
    public static final TagKey<Biome> GRAVEYARD = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/graveyard"));
    public static final TagKey<Biome> FIRE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/fire"));
    public static final TagKey<Biome> ICE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/ice"));
    public static final TagKey<Biome> LIGHTENING = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "structure_gen/lightening"));

    public static final TagKey<Biome> SILVER_ORE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "ore_gen/silver"));
    public static final TagKey<Biome> SAPPHIRE_ORE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "ore_gen/sapphire"));
}
