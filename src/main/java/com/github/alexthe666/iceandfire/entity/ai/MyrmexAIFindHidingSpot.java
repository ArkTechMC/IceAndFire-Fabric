package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIFindHidingSpot extends Goal {
    private static final int RADIUS = 32;
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super Entity> targetEntitySelector;
    private final EntityMyrmexSentinel myrmex;
    private BlockPos targetBlock = null;
    private int wanderRadius = RADIUS;

    public MyrmexAIFindHidingSpot(EntityMyrmexSentinel myrmex) {
        super();
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = (Predicate<Entity>) myrmex1 -> myrmex1 instanceof EntityMyrmexSentinel;
        this.myrmex = myrmex;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        this.targetBlock = this.getTargetPosition(this.wanderRadius);
        return this.myrmex.canMove() && this.myrmex.getTarget() == null && this.myrmex.canSeeSky() && !this.myrmex.isHiding() && this.myrmex.visibleTicks <= 0;
    }

    @Override
    public boolean shouldContinue() {
        return !this.myrmex.shouldEnterHive() && this.myrmex.getTarget() == null && !this.myrmex.isHiding() && this.myrmex.visibleTicks <= 0;
    }

    @Override
    public void tick() {
        if (this.targetBlock != null) {
            this.myrmex.getNavigation().startMovingTo(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            if (this.areMyrmexNear(5) || this.myrmex.isOnResin()) {
                if (this.myrmex.squaredDistanceTo(Vec3d.ofCenter(this.targetBlock)) < 9) {
                    this.wanderRadius += RADIUS;
                    this.targetBlock = this.getTargetPosition(this.wanderRadius);
                }
            } else {
                if (this.myrmex.getTarget() == null && this.myrmex.getCustomer() == null && this.myrmex.visibleTicks == 0 && this.myrmex.squaredDistanceTo(Vec3d.ofCenter(this.targetBlock)) < 9) {
                    this.myrmex.setHiding(true);
                    this.myrmex.getNavigation().stop();
                }
            }
        }

    }

    @Override
    public void stop() {
        this.targetBlock = null;
        this.wanderRadius = RADIUS;
    }

    protected Box getTargetableArea(double targetDistance) {
        return this.myrmex.getBoundingBox().expand(targetDistance, 14.0D, targetDistance);
    }

    public BlockPos getTargetPosition(int radius) {
        final int x = (int) this.myrmex.getX() + this.myrmex.getRandom().nextInt(radius * 2) - radius;
        final int z = (int) this.myrmex.getZ() + this.myrmex.getRandom().nextInt(radius * 2) - radius;
        return this.myrmex.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
    }

    private boolean areMyrmexNear(double distance) {
        List<Entity> sentinels = this.myrmex.getWorld().getOtherEntities(this.myrmex, this.getTargetableArea(distance), this.targetEntitySelector);
        List<Entity> hiddenSentinels = new ArrayList<>();
        for (Entity sentinel : sentinels) {
            if (sentinel instanceof EntityMyrmexSentinel && ((EntityMyrmexSentinel) sentinel).isHiding()) {
                hiddenSentinels.add(sentinel);
            }
        }
        return !hiddenSentinels.isEmpty();
    }

}
