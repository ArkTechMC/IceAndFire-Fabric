package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.registry.IafBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

public class WorldGenRoostSpire {

    public void generate(WorldAccess worldIn, Random rand, BlockPos position) {
        int height = 5 + rand.nextInt(5);
        Direction bumpDirection = Direction.NORTH;
        for (int i = 0; i < height; i++) {
            worldIn.setBlockState(position.up(i), IafBlocks.CRACKLED_STONE.getDefaultState(), 2);
            if (rand.nextBoolean()) {
                bumpDirection = bumpDirection.rotateYClockwise();
            }
            int offset = 1;
            if (i < 4) {
                worldIn.setBlockState(position.up(i).north(), IafBlocks.CRACKLED_GRAVEL.getDefaultState(), 2);
                worldIn.setBlockState(position.up(i).south(), IafBlocks.CRACKLED_GRAVEL.getDefaultState(), 2);
                worldIn.setBlockState(position.up(i).east(), IafBlocks.CRACKLED_GRAVEL.getDefaultState(), 2);
                worldIn.setBlockState(position.up(i).west(), IafBlocks.CRACKLED_GRAVEL.getDefaultState(), 2);
                offset = 2;
            }
            if (i < height - 2) {
                worldIn.setBlockState(position.up(i).offset(bumpDirection, offset), IafBlocks.CRACKLED_COBBLESTONE.getDefaultState(), 2);
            }
        }
    }
}