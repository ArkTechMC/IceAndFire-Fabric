package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public interface IPhasesThroughBlock {

    boolean canPhaseThroughBlock(WorldAccess world, BlockPos pos);
}
