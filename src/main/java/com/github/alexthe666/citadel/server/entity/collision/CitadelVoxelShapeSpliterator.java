package com.github.alexthe666.citadel.server.entity.collision;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.CuboidBlockIterator;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.border.WorldBorder;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class CitadelVoxelShapeSpliterator extends Spliterators.AbstractSpliterator<VoxelShape> {
    private final Entity entity;
    private final Box aabb;
    private final ShapeContext context;
    private final CuboidBlockIterator cubeCoordinateIterator;
    private final BlockPos.Mutable mutablePos;
    private final VoxelShape shape;
    private final CollisionView reader;
    private final BiPredicate<BlockState, BlockPos> statePositionPredicate;
    private boolean needsBorderCheck;

    public CitadelVoxelShapeSpliterator(CollisionView reader, Entity entity, Box aabb) {
        this(reader, entity, aabb, (p_241459_0_, p_241459_1_) -> {
            return true;
        });
    }

    public CitadelVoxelShapeSpliterator(CollisionView reader, Entity entity, Box aabb, BiPredicate<BlockState, BlockPos> statePositionPredicate) {
        super(Long.MAX_VALUE, Spliterator.NONNULL | Spliterator.IMMUTABLE);
        this.context = entity == null ? ShapeContext.absent() : ShapeContext.of(entity);
        this.mutablePos = new BlockPos.Mutable();
        this.shape = VoxelShapes.cuboid(aabb);
        this.reader = reader;
        this.needsBorderCheck = entity != null;
        this.entity = entity;
        this.aabb = aabb;
        this.statePositionPredicate = statePositionPredicate;
        int i = MathHelper.floor(aabb.minX - 1.0E-7D) - 1;
        int j = MathHelper.floor(aabb.maxX + 1.0E-7D) + 1;
        int k = MathHelper.floor(aabb.minY - 1.0E-7D) - 1;
        int l = MathHelper.floor(aabb.maxY + 1.0E-7D) + 1;
        int i1 = MathHelper.floor(aabb.minZ - 1.0E-7D) - 1;
        int j1 = MathHelper.floor(aabb.maxZ + 1.0E-7D) + 1;
        this.cubeCoordinateIterator = new CuboidBlockIterator(i, k, i1, j, l, j1);
    }

    private static boolean isCloseToBorder(VoxelShape p_241460_0_, Box p_241460_1_) {
        return VoxelShapes.matchesAnywhere(p_241460_0_, VoxelShapes.cuboid(p_241460_1_.expand(1.0E-7D)), BooleanBiFunction.AND);
    }

    private static boolean isOutsideBorder(VoxelShape p_241461_0_, Box p_241461_1_) {
        return VoxelShapes.matchesAnywhere(p_241461_0_, VoxelShapes.cuboid(p_241461_1_.contract(1.0E-7D)), BooleanBiFunction.AND);
    }

    public static boolean isBoxFullyWithinWorldBorder(WorldBorder p_234877_0_, Box p_234877_1_) {
        double d0 = MathHelper.floor(p_234877_0_.getBoundWest());
        double d1 = MathHelper.floor(p_234877_0_.getBoundNorth());
        double d2 = MathHelper.ceil(p_234877_0_.getBoundEast());
        double d3 = MathHelper.ceil(p_234877_0_.getBoundSouth());
        return p_234877_1_.minX > d0 && p_234877_1_.minX < d2 && p_234877_1_.minZ > d1 && p_234877_1_.minZ < d3 && p_234877_1_.maxX > d0 && p_234877_1_.maxX < d2 && p_234877_1_.maxZ > d1 && p_234877_1_.maxZ < d3;
    }

    public boolean tryAdvance(Consumer<? super VoxelShape> p_tryAdvance_1_) {
        return this.needsBorderCheck && this.worldBorderCheck(p_tryAdvance_1_) || this.collisionCheck(p_tryAdvance_1_);
    }

    boolean collisionCheck(Consumer<? super VoxelShape> p_234878_1_) {
        while (true) {
            if (this.cubeCoordinateIterator.step()) {
                int i = this.cubeCoordinateIterator.getX();
                int j = this.cubeCoordinateIterator.getY();
                int k = this.cubeCoordinateIterator.getZ();
                int l = this.cubeCoordinateIterator.getEdgeCoordinatesCount();
                if (l == 3) {
                    continue;
                }

                BlockView iblockreader = this.getChunk(i, k);
                if (iblockreader == null) {
                    continue;
                }

                this.mutablePos.set(i, j, k);
                BlockState blockstate = iblockreader.getBlockState(this.mutablePos);
                if (!this.statePositionPredicate.test(blockstate, this.mutablePos) || l == 1 && !blockstate.exceedsCube() || l == 2 && blockstate.getBlock() != Blocks.MOVING_PISTON) {
                    continue;
                }
                VoxelShape voxelshape = blockstate.getCollisionShape(this.reader, this.mutablePos, this.context);
                if (entity instanceof ICustomCollisions && ((ICustomCollisions) entity).canPassThrough(mutablePos, blockstate, voxelshape)) {
                    continue;
                }
                if (voxelshape == VoxelShapes.fullCube()) {
                    if (!this.aabb.intersects(i, j, k, (double) i + 1.0D, (double) j + 1.0D, (double) k + 1.0D)) {
                        continue;
                    }

                    p_234878_1_.accept(voxelshape.offset(i, j, k));
                    return true;
                }

                VoxelShape voxelshape1 = voxelshape.offset(i, j, k);
                if (!VoxelShapes.matchesAnywhere(voxelshape1, this.shape, BooleanBiFunction.AND)) {
                    continue;
                }

                p_234878_1_.accept(voxelshape1);
                return true;
            }

            return false;
        }
    }

    private BlockView getChunk(int p_234876_1_, int p_234876_2_) {
        int i = p_234876_1_ >> 4;
        int j = p_234876_2_ >> 4;
        return this.reader.getChunkAsView(i, j);
    }

    boolean worldBorderCheck(Consumer<? super VoxelShape> p_234879_1_) {
        Objects.requireNonNull(this.entity);
        this.needsBorderCheck = false;
        WorldBorder worldborder = this.reader.getWorldBorder();
        Box axisalignedbb = this.entity.getBoundingBox();
        if (!isBoxFullyWithinWorldBorder(worldborder, axisalignedbb)) {
            VoxelShape voxelshape = worldborder.asVoxelShape();
            if (!isOutsideBorder(voxelshape, axisalignedbb) && isCloseToBorder(voxelshape, axisalignedbb)) {
                p_234879_1_.accept(voxelshape);
                return true;
            }
        }

        return false;
    }
}
