package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityDragonforgeBrick extends BlockEntity {
    public TileEntityDragonforgeBrick(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_BRICK, pos, state);
    }
}
