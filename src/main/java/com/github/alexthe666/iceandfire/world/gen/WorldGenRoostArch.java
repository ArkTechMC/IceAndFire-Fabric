package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

public class WorldGenRoostArch {
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private final Block block;

    public WorldGenRoostArch(Block block) {
        this.block = block;
    }

    public boolean generate(WorldAccess worldIn, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(3);
        int width = Math.min(3, height - 2);
        Direction direction = HORIZONTALS[rand.nextInt(HORIZONTALS.length - 1)];
        boolean diagonal = rand.nextBoolean();
        for (int i = 0; i < height; i++) {
            worldIn.setBlockState(position.up(i), block.getDefaultState(), 2);
        }
        BlockPos offsetPos = position;
        int placedWidths = 0;
        for (int i = 0; i < width; i++) {
            offsetPos = position.up(height).offset(direction, i);
            if (diagonal) {
                offsetPos = position.up(height).offset(direction, i).offset(direction.rotateYClockwise(), i);
            }
            if (placedWidths < width - 1 || rand.nextBoolean()) {
                worldIn.setBlockState(offsetPos, block.getDefaultState(), 2);
            }
            placedWidths++;
        }
        while (worldIn.isAir(offsetPos.down()) && offsetPos.getY() > 0) {
            worldIn.setBlockState(offsetPos.down(), block.getDefaultState(), 2);
            offsetPos = offsetPos.down();
        }
        return true;
    }
}
