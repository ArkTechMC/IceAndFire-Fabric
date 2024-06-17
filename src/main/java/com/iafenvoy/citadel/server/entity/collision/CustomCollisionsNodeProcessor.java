package com.iafenvoy.citadel.server.entity.collision;

import com.iafenvoy.iceandfire.util.PathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class CustomCollisionsNodeProcessor extends LandPathNodeMaker {
    public CustomCollisionsNodeProcessor() {
    }

    public static PathNodeType getLandNodeType(BlockView view, BlockPos.Mutable mutable) {
        int i = mutable.getX();
        int j = mutable.getY();
        int k = mutable.getZ();
        PathNodeType pathnodetype = getNodes(view, mutable);
        if (pathnodetype == PathNodeType.OPEN && j >= 1) {
            PathNodeType nodes = getNodes(view, mutable.set(i, j - 1, k));
            pathnodetype = nodes != PathNodeType.WALKABLE && nodes != PathNodeType.OPEN && nodes != PathNodeType.WATER && nodes != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
            if (nodes == PathNodeType.DAMAGE_FIRE)
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            if (nodes == PathNodeType.DAMAGE_OTHER)
                pathnodetype = PathNodeType.DAMAGE_OTHER;
            if (nodes == PathNodeType.STICKY_HONEY)
                pathnodetype = PathNodeType.STICKY_HONEY;
        }
        if (pathnodetype == PathNodeType.WALKABLE)
            pathnodetype = getNodeTypeFromNeighbors(view, mutable.set(i, j, k), pathnodetype);
        return pathnodetype;
    }


    protected static PathNodeType getNodes(BlockView p_237238_0_, BlockPos p_237238_1_) {
        BlockState blockstate = p_237238_0_.getBlockState(p_237238_1_);
        PathNodeType type = PathUtil.getAiPathNodeType(blockstate, (WorldView) p_237238_0_, p_237238_1_);
        if (type != null) return type;
        if (blockstate.isAir()) return PathNodeType.OPEN;
        else if (blockstate.getBlock() == Blocks.BAMBOO) return PathNodeType.OPEN;
        else return getCommonNodeType(p_237238_0_, p_237238_1_);
    }

    @Override
    public PathNodeType getDefaultNodeType(BlockView blockaccessIn, int x, int y, int z) {
        return getLandNodeType(blockaccessIn, new BlockPos.Mutable(x, y, z));
    }

    @Override
    protected PathNodeType adjustNodeType(BlockView world, BlockPos pos, PathNodeType nodeType) {
        BlockState state = world.getBlockState(pos);
        return ((ICustomCollisions) this.entity).canPassThrough(pos, state, state.getSidesShape(world, pos)) ? PathNodeType.OPEN : super.adjustNodeType(world, pos, nodeType);
    }
}