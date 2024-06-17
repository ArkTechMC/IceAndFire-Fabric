package com.iafenvoy.iceandfire.entity.block;

import com.iafenvoy.iceandfire.registry.IafBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BlockEntityDragonForgeBrick extends BlockEntity {
    public BlockEntityDragonForgeBrick(BlockPos pos, BlockState state) {
        super(IafBlockEntities.DRAGONFORGE_BRICK, pos, state);
    }
}
