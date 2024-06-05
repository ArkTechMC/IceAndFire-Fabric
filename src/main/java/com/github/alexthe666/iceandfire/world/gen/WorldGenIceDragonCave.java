package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafBlockTags;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class WorldGenIceDragonCave extends WorldGenDragonCave {
    public static Identifier ICE_DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/ice_dragon_female_cave");
    public static Identifier ICE_DRAGON_CHEST_MALE = new Identifier(IceAndFire.MOD_ID, "chest/ice_dragon_male_cave");

    public WorldGenIceDragonCave(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
        this.DRAGON_CHEST = ICE_DRAGON_CHEST;
        this.DRAGON_MALE_CHEST = ICE_DRAGON_CHEST_MALE;
        this.CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.FROZEN_STONE.get(), 3);
        this.PALETTE_BLOCK1 = IafBlockRegistry.FROZEN_STONE.get().defaultBlockState();
        this.PALETTE_BLOCK2 = IafBlockRegistry.FROZEN_COBBLESTONE.get().defaultBlockState();
        this.TREASURE_PILE = IafBlockRegistry.SILVER_PILE.get().defaultBlockState();
        this.dragonTypeOreTag = IafBlockTags.ICE_DRAGON_CAVE_ORES;
    }

    @Override
    public EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.ICE_DRAGON.get();
    }
}
