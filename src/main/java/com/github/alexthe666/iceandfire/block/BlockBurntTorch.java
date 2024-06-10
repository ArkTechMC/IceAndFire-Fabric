package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockBurntTorch extends TorchBlock implements IDreadBlock, IWallBlock {

    public BlockBurntTorch() {
        super(
                Settings.create()
                        .mapColor(MapColor.OAK_TAN)
                        .burnable()
                        .luminance((state) -> 0)
                        .sounds(BlockSoundGroup.WOOD)
                        .nonOpaque()
                        .dynamicBounds()
                        .noCollision(),
                DustParticleEffect.DEFAULT
        );
    }

    @Override
    public void randomDisplayTick(@NotNull BlockState stateIn, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Random rand) {

    }

    @Override
    public Block wallBlock() {
        return IafBlockRegistry.BURNT_TORCH_WALL.get();
    }
}