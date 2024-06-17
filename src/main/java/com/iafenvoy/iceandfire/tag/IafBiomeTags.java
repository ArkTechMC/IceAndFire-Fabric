package com.iafenvoy.iceandfire.tag;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class IafBiomeTags {
    public static final TagKey<Biome> HAS_GORGON_TEMPLE = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "has_structure/gorgon_temple"));
    public static final TagKey<Biome> HAS_MAUSOLEUM = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "has_structure/mausoleum"));
    public static final TagKey<Biome> HAS_GRAVEYARD = TagKey.of(RegistryKeys.BIOME, new Identifier(IceAndFire.MOD_ID, "has_structure/graveyard"));
}
