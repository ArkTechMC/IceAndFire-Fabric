package com.iafenvoy.iceandfire.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;

public class BlockUtil {
    public static boolean isLadder(BlockState state) {
        return state.isIn(BlockTags.CLIMBABLE);
    }

    public static boolean isLadder(Block block) {
        return isLadder(block.getDefaultState());
    }

    public static boolean isBurning(BlockState block) {
        return isBurning(block.getBlock());
    }

    public static boolean isBurning(Block block) {
        return block == Blocks.FIRE || block == Blocks.LAVA;
    }
}
