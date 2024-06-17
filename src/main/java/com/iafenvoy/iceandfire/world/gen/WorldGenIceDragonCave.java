package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.tag.IafBlockTags;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class WorldGenIceDragonCave extends WorldGenDragonCave {
    public static final Identifier ICE_DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/ice_dragon_female_cave");
    public static final Identifier ICE_DRAGON_CHEST_MALE = new Identifier(IceAndFire.MOD_ID, "chest/ice_dragon_male_cave");

    public WorldGenIceDragonCave(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
        this.DRAGON_CHEST = ICE_DRAGON_CHEST;
        this.DRAGON_MALE_CHEST = ICE_DRAGON_CHEST_MALE;
        this.CEILING_DECO = new WorldGenCaveStalactites(IafBlocks.FROZEN_STONE, 3);
        this.PALETTE_BLOCK1 = IafBlocks.FROZEN_STONE.getDefaultState();
        this.PALETTE_BLOCK2 = IafBlocks.FROZEN_COBBLESTONE.getDefaultState();
        this.TREASURE_PILE = IafBlocks.SILVER_PILE.getDefaultState();
        this.dragonTypeOreTag = IafBlockTags.ICE_DRAGON_CAVE_ORES;
    }

    @Override
    public EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntities.ICE_DRAGON;
    }
}
