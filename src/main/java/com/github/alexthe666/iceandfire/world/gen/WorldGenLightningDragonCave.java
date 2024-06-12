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

public class WorldGenLightningDragonCave extends WorldGenDragonCave {
    public static final Identifier LIGHTNING_DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/lightning_dragon_female_cave");
    public static final Identifier LIGHTNING_DRAGON_CHEST_MALE = new Identifier(IceAndFire.MOD_ID, "chest/lightning_dragon_male_cave");

    public WorldGenLightningDragonCave(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration);
        this.DRAGON_CHEST = LIGHTNING_DRAGON_CHEST;
        this.DRAGON_MALE_CHEST = LIGHTNING_DRAGON_CHEST_MALE;
        this.CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.CRACKLED_STONE.get(), 6);
        this.PALETTE_BLOCK1 = IafBlockRegistry.CRACKLED_STONE.get().getDefaultState();
        this.PALETTE_BLOCK2 = IafBlockRegistry.CRACKLED_COBBLESTONE.get().getDefaultState();
        this.TREASURE_PILE = IafBlockRegistry.COPPER_PILE.get().getDefaultState();
        this.dragonTypeOreTag = IafBlockTags.LIGHTNING_DRAGON_CAVE_ORES;
    }

    @Override
    public EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.LIGHTNING_DRAGON.get();
    }
}
