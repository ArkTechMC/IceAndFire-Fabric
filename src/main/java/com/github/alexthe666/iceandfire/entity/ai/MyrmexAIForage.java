package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import dev.arktechmc.iafextra.event.EventBus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyrmexAIForage extends Goal {

    private static final int RADIUS = 16;
    private final EntityMyrmexWorker myrmex;
    private final BlockSorter targetSorter;
    private final int chance;
    private BlockPos targetBlock = null;
    private int wanderRadius;
    private PathResult path;
    private int failedToFindPath = 0;

    public MyrmexAIForage(EntityMyrmexWorker myrmex, int chanceIn) {
        super();
        this.myrmex = myrmex;
        this.targetSorter = new BlockSorter();
        this.setControls(EnumSet.of(Control.MOVE));
        this.chance = chanceIn;
    }

    @Override
    public boolean canStart() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || !this.myrmex.getNavigation().isIdle()
                || this.myrmex.isInHive() || this.myrmex.shouldEnterHive()) {
            return false;
        }
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.hasVehicle()) {
            return false;
        }
        if (this.myrmex.getWaitTicks() > 0) {
            return false;
        }

        // Get nearby edible blocks
        List<BlockPos> edibleBlocks = this.getEdibleBlocks();
        // If there are no edible blocks nearby
        if (edibleBlocks.isEmpty()) {
            return this.myrmex.getRandom().nextInt(this.chance) == 0 && this.increaseRadiusAndWander();
        }
        // Set closest block as target
        edibleBlocks.sort(this.targetSorter);
        this.targetBlock = edibleBlocks.get(0);
        this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.targetBlock.getX(),
                this.targetBlock.getY(), this.targetBlock.getZ(), 1D);
        return this.myrmex.getRandom().nextInt(this.chance) == 0;
    }

    @Override
    public boolean shouldContinue() {
        if (this.targetBlock == null) return false;
        if (this.myrmex.getWaitTicks() > 0) return false;
        if (this.myrmex.shouldEnterHive()) {
            this.myrmex.keepSearching = false;
            return false;
        }
        return !this.myrmex.holdingSomething();
    }

    @Override
    public void tick() {
        // If we haven't found an edible block as a target
        if (this.targetBlock != null && this.myrmex.keepSearching) {
            // If the myrmex is close enough to the target or at the end of the path
            if (this.myrmex.isCloseEnoughToTarget(this.targetBlock, 12)
                    || !this.myrmex.pathReachesTarget(this.path, this.targetBlock, 12)) {
                this.failedToFindPath = 0;
                List<BlockPos> edibleBlocks = this.getEdibleBlocks();
                // If we found an edible block nearby
                if (!edibleBlocks.isEmpty()) {
                    this.myrmex.keepSearching = false;
                    edibleBlocks.sort(this.targetSorter);
                    this.targetBlock = edibleBlocks.get(0);
                    this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.targetBlock.getX(),
                            this.targetBlock.getY(), this.targetBlock.getZ(), 1D);
                }
                // If there are still no edible blocks nearby
                else {
                    this.increaseRadiusAndWander();
                }
            }
            // if we have found an edible block
        } else if (!this.myrmex.keepSearching) {
            this.failedToFindPath = 0;
            BlockState block = this.myrmex.getWorld().getBlockState(this.targetBlock);
            // Test if the block is edible
            if (EntityMyrmexBase.isEdibleBlock(block)) {
                final double distance = this.getDistanceSq(this.targetBlock);
                if (distance < 6) {
                    block.getBlock();
                    // Routine to break block and add item to myrmex
                    List<ItemStack> drops = Block.getDroppedStacks(block, (ServerWorld) this.myrmex.getWorld(), this.targetBlock,
                            this.myrmex.getWorld().getBlockEntity(this.targetBlock)); // use the old method until it gets removed, for
                    // backward compatibility
                    if (!drops.isEmpty()) {
                        this.myrmex.getWorld().breakBlock(this.targetBlock, false);
                        ItemStack heldStack = drops.get(0).copy();
                        heldStack.setCount(1);
                        drops.get(0).decrement(1);
                        this.myrmex.setStackInHand(Hand.MAIN_HAND, heldStack);
                        for (ItemStack stack : drops) {
                            ItemEntity itemEntity = new ItemEntity(this.myrmex.getWorld(),
                                    this.targetBlock.getX() + this.myrmex.getRandom().nextDouble(),
                                    this.targetBlock.getY() + this.myrmex.getRandom().nextDouble(),
                                    this.targetBlock.getZ() + this.myrmex.getRandom().nextDouble(), stack);
                            itemEntity.setToDefaultPickupDelay();
                            if (!this.myrmex.getWorld().isClient) {
                                this.myrmex.getWorld().spawnEntity(itemEntity);
                            }
                        }
                        this.targetBlock = null;
                        this.stop();
                        this.myrmex.keepSearching = false;
                        this.wanderRadius = RADIUS;
                    }
                }
                // If the myrmex reached the end of the path but is still not close enough to
                // the target
                else if (!this.myrmex.pathReachesTarget(this.path, this.targetBlock, 12)) {
                    List<BlockPos> edibleBlocks = this.getEdibleBlocks();
                    // If we found an edible block nearby
                    if (!edibleBlocks.isEmpty()) {
                        this.myrmex.keepSearching = false;
                        // This time choose a different random edible block
                        this.targetBlock = edibleBlocks.get(this.myrmex.getRandom().nextInt(edibleBlocks.size()));
                        this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.targetBlock.getX(),
                                this.targetBlock.getY(), this.targetBlock.getZ(), 1D);
                    } else {
                        this.myrmex.keepSearching = true;
                    }
                }
            }
        }

    }

    @Override
    public void stop() {
        this.targetBlock = null;
        this.myrmex.keepSearching = true;
    }

    private double getDistanceSq(BlockPos pos) {
        final double deltaX = this.myrmex.getX() - (pos.getX() + 0.5);
        final double deltaY = this.myrmex.getY() + this.myrmex.getStandingEyeHeight() - (pos.getY() + 0.5);
        final double deltaZ = this.myrmex.getZ() - (pos.getZ() + 0.5);
        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    private List<BlockPos> getEdibleBlocks() {
        List<BlockPos> allBlocks = new ArrayList<>();
        BlockPos.stream(this.myrmex.getBlockPos().add(-RADIUS, -RADIUS / 2, -RADIUS),
                this.myrmex.getBlockPos().add(RADIUS, RADIUS / 2, RADIUS)).map(BlockPos::toImmutable).forEach(pos -> {
            if (!EventBus.post(new GenericGriefEvent(this.myrmex, pos.getX(), pos.getY(), pos.getZ()))) {
                if (EntityMyrmexBase.isEdibleBlock(this.myrmex.getWorld().getBlockState(pos))) {
                    allBlocks.add(pos);
                    this.myrmex.keepSearching = false;
                }
            }
        });
        return allBlocks;
    }

    private boolean increaseRadiusAndWander() {
        this.myrmex.keepSearching = true;
        if (this.myrmex.getHive() != null) {
            this.wanderRadius = this.myrmex.getHive().getWanderRadius();
            this.myrmex.getHive().setWanderRadius(this.wanderRadius * 2);
        }
        // Increase wander radius
        this.wanderRadius *= 2;
        // this.myrmex.setWaitTicks(80+new Random().nextInt(40));
        // Set target as random position inside wanderRadius
        if (this.wanderRadius >= IafConfig.myrmexMaximumWanderRadius) {
            this.wanderRadius = IafConfig.myrmexMaximumWanderRadius;
            this.myrmex.setWaitTicks(80 + ThreadLocalRandom.current().nextInt(40));
            // Keep track of how many times the myrmex has potentially not found a path to a
            // target
            this.failedToFindPath++;
            if (this.failedToFindPath >= 10) {
                this.myrmex.setWaitTicks(800 + ThreadLocalRandom.current().nextInt(40));
            }
        }
        Vec3d vec = NoPenaltyTargeting.find(this.myrmex, this.wanderRadius, 7);
        if (vec != null) {
            this.targetBlock = BlockPos.ofFloored(vec);
        }
        if (this.targetBlock != null) {
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.targetBlock.getX(),
                    this.targetBlock.getY(), this.targetBlock.getZ(), 1D);
            return true;
        }
        return false;
    }

    public class BlockSorter implements Comparator<BlockPos> {

        @Override
        public int compare(BlockPos pos1, BlockPos pos2) {
            double distance1 = MyrmexAIForage.this.getDistanceSq(pos1);
            double distance2 = MyrmexAIForage.this.getDistanceSq(pos2);
            return Double.compare(distance1, distance2);
        }
    }

}
