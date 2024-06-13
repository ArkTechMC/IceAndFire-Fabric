package com.github.alexthe666.citadel.server.entity.collision;

import dev.arktechmc.iafextra.util.PathUtil;
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

    public static PathNodeType getLandNodeType(BlockView blockView, BlockPos.Mutable mutable) {
        int i = mutable.getX();
        int j = mutable.getY();
        int k = mutable.getZ();
        PathNodeType pathnodetype = getNodes(blockView, mutable);
        if (pathnodetype == PathNodeType.OPEN && j >= 1) {
            PathNodeType nodes = getNodes(blockView, mutable.set(i, j - 1, k));
            pathnodetype = nodes != PathNodeType.WALKABLE && nodes != PathNodeType.OPEN && nodes != PathNodeType.WATER && nodes != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
            if (nodes == PathNodeType.DAMAGE_FIRE)
                pathnodetype = PathNodeType.DAMAGE_FIRE;

            if (nodes == PathNodeType.DAMAGE_OTHER)
                pathnodetype = PathNodeType.DAMAGE_OTHER;

            if (nodes == PathNodeType.STICKY_HONEY)
                pathnodetype = PathNodeType.STICKY_HONEY;
        }

        if (pathnodetype == PathNodeType.WALKABLE)
            pathnodetype = getNodeTypeFromNeighbors(blockView, mutable.set(i, j, k), pathnodetype);

        return pathnodetype;
    }


    protected static PathNodeType getNodes(BlockView blockView, BlockPos blockPos) {
        BlockState blockstate = blockView.getBlockState(blockPos);
        PathNodeType type = PathUtil.getAiPathNodeType(blockstate, (WorldView) blockView, blockPos);
        if (type != null) return type;
        if (blockstate.isAir()) return PathNodeType.OPEN;
        else if (blockstate.getBlock() == Blocks.BAMBOO) return PathNodeType.OPEN;
        else return getCommonNodeType(blockView, blockPos);

    }

    @Override
    public PathNodeType getDefaultNodeType(BlockView world, int x, int y, int z) {
        return getLandNodeType(world, new BlockPos.Mutable(x, y, z));
    }

    @Override
    protected PathNodeType adjustNodeType(BlockView world, BlockPos pos, PathNodeType nodeType) {
        BlockState state = world.getBlockState(pos);
        return ((ICustomCollisions) this.entity).canPassThrough(pos, state, state.getSidesShape(world, pos)) ? PathNodeType.OPEN : super.adjustNodeType(world, pos, nodeType);
    }
}