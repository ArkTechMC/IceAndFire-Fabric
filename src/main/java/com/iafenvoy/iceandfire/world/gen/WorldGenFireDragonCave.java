package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.datagen.tags.IafBlockTags;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class WorldGenFireDragonCave extends WorldGenDragonCave {
    public static final Identifier FIRE_DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_female_cave");
    public static final Identifier FIRE_DRAGON_CHEST_MALE = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_male_cave");

    public WorldGenFireDragonCave(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
        this.DRAGON_CHEST = FIRE_DRAGON_CHEST;
        this.DRAGON_MALE_CHEST = FIRE_DRAGON_CHEST_MALE;
        this.CEILING_DECO = new WorldGenCaveStalactites(IafBlocks.CHARRED_STONE, 3);
        this.PALETTE_BLOCK1 = IafBlocks.CHARRED_STONE.getDefaultState();
        this.PALETTE_BLOCK2 = IafBlocks.CHARRED_COBBLESTONE.getDefaultState();
        this.TREASURE_PILE = IafBlocks.GOLD_PILE.getDefaultState();
        this.dragonTypeOreTag = IafBlockTags.FIRE_DRAGON_CAVE_ORES;
    }

    @Override
    public EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntities.FIRE_DRAGON;
    }
}
