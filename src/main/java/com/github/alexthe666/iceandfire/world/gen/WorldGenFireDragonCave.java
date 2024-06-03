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
    public static Identifier FIRE_DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_female_cave");
    public static Identifier FIRE_DRAGON_CHEST_MALE = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_male_cave");

    public WorldGenFireDragonCave(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
        DRAGON_CHEST = FIRE_DRAGON_CHEST;
        DRAGON_MALE_CHEST = FIRE_DRAGON_CHEST_MALE;
        CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.CHARRED_STONE.get(), 3);
        PALETTE_BLOCK1 = IafBlockRegistry.CHARRED_STONE.get().getDefaultState();
        PALETTE_BLOCK2 = IafBlockRegistry.CHARRED_COBBLESTONE.get().getDefaultState();
        TREASURE_PILE = IafBlockRegistry.GOLD_PILE.get().getDefaultState();
        dragonTypeOreTag = IafBlockTags.FIRE_DRAGON_CAVE_ORES;
    }

    @Override
    public EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.FIRE_DRAGON.get();
    }
}
