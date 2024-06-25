package com.iafenvoy.iceandfire.item.block;

import com.iafenvoy.iceandfire.item.block.util.IDreadBlock;
import com.iafenvoy.iceandfire.item.block.util.IWallBlock;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BlockBurntTorch extends TorchBlock implements IDreadBlock, IWallBlock {
    public BlockBurntTorch() {
        super(Settings.create().mapColor(MapColor.OAK_TAN).burnable().luminance((state) -> 0).sounds(BlockSoundGroup.WOOD).nonOpaque().dynamicBounds().noCollision(), DustParticleEffect.DEFAULT);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
    }

    @Override
    public Block wallBlock() {
        return IafBlocks.BURNT_TORCH_WALL;
    }
}