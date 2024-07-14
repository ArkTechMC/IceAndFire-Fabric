package com.iafenvoy.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

public class WorldGenCaveStalactites {
    private final Block block;
    private final int maxHeight;

    public WorldGenCaveStalactites(Block block, int maxHeight) {
        this.block = block;
        this.maxHeight = maxHeight;
    }

    public void generate(WorldAccess worldIn, Random rand, BlockPos position) {
        int height = this.maxHeight + rand.nextInt(3);
        for (int i = 0; i < height; i++) {
            if (i < height / 2) {
                worldIn.setBlockState(position.down(i).north(), this.block.getDefaultState(), 2);
                worldIn.setBlockState(position.down(i).east(), this.block.getDefaultState(), 2);
                worldIn.setBlockState(position.down(i).south(), this.block.getDefaultState(), 2);
                worldIn.setBlockState(position.down(i).west(), this.block.getDefaultState(), 2);
            }
            worldIn.setBlockState(position.down(i), this.block.getDefaultState(), 2);
        }
    }
}
