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

public class WorldGenFireDragonCave extends WorldGenDragonCave {
    public static final Identifier FIRE_DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_female_cave");
    public static final Identifier FIRE_DRAGON_CHEST_MALE = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_male_cave");

    public WorldGenFireDragonCave(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
        this.DRAGON_CHEST = FIRE_DRAGON_CHEST;
        this.DRAGON_MALE_CHEST = FIRE_DRAGON_CHEST_MALE;
        this.CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.CHARRED_STONE, 3);
        this.PALETTE_BLOCK1 = IafBlockRegistry.CHARRED_STONE.getDefaultState();
        this.PALETTE_BLOCK2 = IafBlockRegistry.CHARRED_COBBLESTONE.getDefaultState();
        this.TREASURE_PILE = IafBlockRegistry.GOLD_PILE.getDefaultState();
        this.dragonTypeOreTag = IafBlockTags.FIRE_DRAGON_CAVE_ORES;
    }

    @Override
    public EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.FIRE_DRAGON;
    }
}
