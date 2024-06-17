package com.iafenvoy.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

import java.util.stream.Collectors;

public class WorldGenRoostPile {
    private final Block block;

    public WorldGenRoostPile(Block block) {
        this.block = block;
    }

    public boolean generate(WorldAccess worldIn, Random rand, BlockPos position) {
        int radius = rand.nextInt(4);
        for (int i = 0; i < radius; i++) {
            int j = radius - i;
            int l = radius - i;
            float f = (float) (j + l) * 0.333F + 0.5F;
            BlockPos up = position.up(i);
            for (BlockPos blockpos : BlockPos.stream(up.add(-j, 0, -l), up.add(j, 0, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (blockpos.getSquaredDistance(position) <= (double) (f * f)) {
                    worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
                }
            }
        }
        return true;
    }
}
