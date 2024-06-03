package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class BlockGenericStairs extends StairsBlock {

    public BlockGenericStairs(BlockState modelState) {
        super(
            modelState,
            Settings
                .create()
                .strength(20F)
        );
    }
}
