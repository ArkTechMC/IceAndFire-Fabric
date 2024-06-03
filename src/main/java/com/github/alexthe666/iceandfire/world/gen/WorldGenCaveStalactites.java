package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

public class WorldGenCaveStalactites {
    private final Block block;
    private int maxHeight = 3;

    public WorldGenCaveStalactites(Block block, int maxHeight) {
        this.block = block;
        this.maxHeight = maxHeight;
    }

    public boolean generate(WorldAccess worldIn, Random rand, BlockPos position) {
        int height = maxHeight + rand.nextInt(3);
        for (int i = 0; i < height; i++) {
            if (i < height / 2) {
                worldIn.setBlockState(position.down(i).north(), block.getDefaultState(), 2);
                worldIn.setBlockState(position.down(i).east(), block.getDefaultState(), 2);
                worldIn.setBlockState(position.down(i).south(), block.getDefaultState(), 2);
                worldIn.setBlockState(position.down(i).west(), block.getDefaultState(), 2);
            }
            worldIn.setBlockState(position.down(i), block.getDefaultState(), 2);
        }
        return true;
    }
}
