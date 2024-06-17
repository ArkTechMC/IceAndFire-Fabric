package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.block.BlockGoldPile;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;

import java.util.stream.Collectors;

public class WorldGenRoostGoldPile {
    private final Block block;

    public WorldGenRoostGoldPile(Block block) {
        this.block = block;
    }

    public boolean generate(WorldAccess worldIn, Random rand, BlockPos position) {
        int radius = rand.nextInt(3);
        for (int i = 0; i < radius; i++) {
            int j = radius - i;
            int l = radius - i;
            float f = (float) (j + l) * 0.333F + 0.5F;
            BlockPos up = position.up(i);
            for (BlockPos blockpos : BlockPos.stream(up.add(-j, 0, -l), up.add(j, 0, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (blockpos.getSquaredDistance(position) <= (double) (f * f)) {
                    blockpos = worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, blockpos);
                    if (this.block instanceof BlockGoldPile) {
                        if (worldIn.isAir(blockpos)) {
                            worldIn.setBlockState(blockpos, this.block.getDefaultState().with(BlockGoldPile.LAYERS, 1 + rand.nextInt(7)), 2);
                            if (worldIn.getBlockState(blockpos.down()).getBlock() instanceof BlockGoldPile) {
                                worldIn.setBlockState(blockpos.down(), this.block.getDefaultState().with(BlockGoldPile.LAYERS, 8), 2);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
