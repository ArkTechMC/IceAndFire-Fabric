package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIForageForItems<T extends ItemEntity> extends TrackTargetGoal {
    public final EntityMyrmexWorker myrmex;
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected ItemEntity targetEntity;

    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public MyrmexAIForageForItems(EntityMyrmexWorker myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = (Predicate<ItemEntity>) item -> item != null && !item.getStack().isEmpty() && !item.isTouchingWater();
        this.myrmex = myrmex;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || this.myrmex.shouldEnterHive() || !this.myrmex.keepSearching || this.myrmex.getTarget() != null) {
            this.list = IAFMath.emptyItemEntityList;
            return false;
        }

        if (this.myrmex.getWorld().getTime() % 4 == 0) // only update the list every 4 ticks
            this.list = this.mob.getWorld().getEntitiesByClass(ItemEntity.class, this.getTargetableArea(), this.targetEntitySelector);

        if (this.list.isEmpty())
            return false;

        this.list.sort(this.theNearestAttackableTargetSorter);
        this.targetEntity = this.list.get(0);
        return true;
    }

    protected Box getTargetableArea() {
        return this.mob.getBoundingBox().expand(32, 5, 32);
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || (!this.targetEntity.isAlive() || this.targetEntity.isTouchingWater())) {
            this.stop();
        } else if (this.mob.squaredDistanceTo(this.targetEntity) < 8F) {
            this.myrmex.onPickupItem(this.targetEntity);
            this.myrmex.setStackInHand(Hand.MAIN_HAND, this.targetEntity.getStack());
            this.targetEntity.remove(Entity.RemovalReason.DISCARDED);
            this.stop();
        }
    }

    @Override
    public void stop() {
        this.myrmex.getNavigation().stop();
        super.stop();
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle() && this.myrmex.getTarget() == null;
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            final double d0 = this.theEntity.squaredDistanceTo(p_compare_1_);
            final double d1 = this.theEntity.squaredDistanceTo(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}