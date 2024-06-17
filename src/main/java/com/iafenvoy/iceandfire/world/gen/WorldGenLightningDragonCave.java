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

public class WorldGenLightningDragonCave extends WorldGenDragonCave {
    public static final Identifier LIGHTNING_DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/lightning_dragon_female_cave");
    public static final Identifier LIGHTNING_DRAGON_CHEST_MALE = new Identifier(IceAndFire.MOD_ID, "chest/lightning_dragon_male_cave");

    public WorldGenLightningDragonCave(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
        this.DRAGON_CHEST = LIGHTNING_DRAGON_CHEST;
        this.DRAGON_MALE_CHEST = LIGHTNING_DRAGON_CHEST_MALE;
        this.CEILING_DECO = new WorldGenCaveStalactites(IafBlocks.CRACKLED_STONE, 6);
        this.PALETTE_BLOCK1 = IafBlocks.CRACKLED_STONE.getDefaultState();
        this.PALETTE_BLOCK2 = IafBlocks.CRACKLED_COBBLESTONE.getDefaultState();
        this.TREASURE_PILE = IafBlocks.COPPER_PILE.getDefaultState();
        this.dragonTypeOreTag = IafBlockTags.LIGHTNING_DRAGON_CAVE_ORES;
    }

    @Override
    public EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntities.LIGHTNING_DRAGON;
    }
}
