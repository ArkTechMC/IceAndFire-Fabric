package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.TargetPathNode;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

public class NodeProcessorDeathWorm extends PathNodeMaker {

    @Override
    public PathNode getStart() {
        return this.getNode(MathHelper.floor(this.entity.getBoundingBox().minX), MathHelper.floor(this.entity.getBoundingBox().minY + 0.5D), MathHelper.floor(this.entity.getBoundingBox().minZ));
    }

    @Override
    public TargetPathNode getNode(double x, double y, double z) {
        return new TargetPathNode(this.getNode(MathHelper.floor(x - 0.4), MathHelper.floor(y + 0.5D), MathHelper.floor(z - 0.4)));
    }

    @Override
    public PathNodeType getNodeType(BlockView blockaccessIn, int x, int y, int z, MobEntity entitylivingIn) {
        return this.getDefaultNodeType(blockaccessIn, x, y, z);
    }

    @Override
    public PathNodeType getDefaultNodeType(BlockView worldIn, int x, int y, int z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (!this.isPassable(worldIn, blockpos.down()) && (blockstate.isAir() || this.isPassable(worldIn, blockpos))) {
            return PathNodeType.BREACH;
        } else {
            return this.isPassable(worldIn, blockpos) ? PathNodeType.WATER : PathNodeType.BLOCKED;
        }
    }

    @Override
    public int getSuccessors(PathNode [] p_222859_1_, PathNode p_222859_2_) {
        int i = 0;

        for (Direction direction : Direction.values()) {
            PathNode pathpoint = this.getSandNode(p_222859_2_.x + direction.getOffsetX(), p_222859_2_.y + direction.getOffsetY(), p_222859_2_.z + direction.getOffsetZ());
            if (pathpoint != null && !pathpoint.visited) {
                p_222859_1_[i++] = pathpoint;
            }
        }

        return i;
    }

    private PathNode getSandNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return pathnodetype != PathNodeType.BREACH && pathnodetype != PathNodeType.WATER ? null : this.getNode(p_186328_1_, p_186328_2_, p_186328_3_);
    }

    private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        for (int i = p_186327_1_; i < p_186327_1_ + this.entityBlockXSize; ++i) {
            for (int j = p_186327_2_; j < p_186327_2_ + this.entityBlockYSize; ++j) {
                for (int k = p_186327_3_; k < p_186327_3_ + this.entityBlockZSize; ++k) {
                    BlockState blockstate = this.cachedWorld.getBlockState(blockpos$mutable.set(i, j, k));
                    if (!this.isPassable(this.cachedWorld, blockpos$mutable.down()) && (blockstate.isAir() || this.isPassable(this.cachedWorld, blockpos$mutable))) {
                        return PathNodeType.BREACH;
                    }

                }
            }
        }

        BlockState blockstate1 = this.cachedWorld.getBlockState(blockpos$mutable);
        return this.isPassable(blockstate1) ? PathNodeType.WATER : PathNodeType.BLOCKED;
    }


    private boolean isPassable(BlockView world, BlockPos pos) {
        return world.getBlockState(pos).isIn(BlockTags.SAND) || world.getBlockState(pos).isAir();
    }

    private boolean isPassable(BlockState state) {
        return state.isIn(BlockTags.SAND) || state.isAir();
    }
}