package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.vanilla.VanillaBiomeTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class IafBiomeTagGenerator extends VanillaBiomeTagProvider {
    public static final TagKey<Biome> HAS_GORGON_TEMPLE = TagKey.of(ForgeRegistries.BIOMES.getRegistryKey(), new Identifier(IceAndFire.MOD_ID, "has_structure/gorgon_temple"));
    public static final TagKey<Biome> HAS_MAUSOLEUM = TagKey.of(ForgeRegistries.BIOMES.getRegistryKey(), new Identifier(IceAndFire.MOD_ID, "has_structure/mausoleum"));
    public static final TagKey<Biome> HAS_GRAVEYARD = TagKey.of(ForgeRegistries.BIOMES.getRegistryKey(), new Identifier(IceAndFire.MOD_ID, "has_structure/graveyard"));


    public IafBiomeTagGenerator(DataOutput pOutput, CompletableFuture<RegistryWrapper.WrapperLookup> pProvider, ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, IceAndFire.MOD_ID, existingFileHelper);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup pProvider) {
        getOrCreateTagBuilder(HAS_GRAVEYARD).addTag(BiomeTags.IS_OVERWORLD);
        getOrCreateTagBuilder(HAS_MAUSOLEUM).addTag(BiomeTags.IS_OVERWORLD);
        getOrCreateTagBuilder(HAS_GORGON_TEMPLE).addTag(BiomeTags.IS_OVERWORLD);
    }

    @Override
    public String getName() {
        return "Ice and Fire Biome Tags";
    }
}
