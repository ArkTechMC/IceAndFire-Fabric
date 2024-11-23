package com.iafenvoy.iceandfire.item.block;

import com.iafenvoy.iceandfire.registry.IafFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class DreadwoodSaplingBlock extends SaplingBlock {
    public DreadwoodSaplingBlock() {
        super(new Generator(), Settings.copy(Blocks.OAK_SAPLING));
    }

    private static class Generator extends SaplingGenerator {
        @Override
        protected @Nullable RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
            return random.nextInt(10) == 0 ? IafFeatures.DREADWOOD_LARGE : IafFeatures.DREADWOOD;
        }
    }
}
