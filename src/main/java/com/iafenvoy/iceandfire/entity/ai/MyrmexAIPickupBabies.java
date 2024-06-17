package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;
import com.iafenvoy.iceandfire.entity.EntityMyrmexEgg;
import com.iafenvoy.iceandfire.entity.EntityMyrmexWorker;
import com.iafenvoy.iceandfire.util.IafMath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIPickupBabies<T extends ItemEntity> extends TrackTargetGoal {
    public final EntityMyrmexWorker myrmex;
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super LivingEntity> targetEntitySelector;
    protected LivingEntity targetEntity;

    private List<LivingEntity> listBabies = IafMath.emptyLivingEntityList;

    public MyrmexAIPickupBabies(EntityMyrmexWorker myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = (Predicate<LivingEntity>) myrmex1 -> (myrmex1 instanceof EntityMyrmexBase && ((EntityMyrmexBase) myrmex1).getGrowthStage() < 2
                && !((EntityMyrmexBase) myrmex1).isInNursery()
                || myrmex1 instanceof EntityMyrmexEgg && !((EntityMyrmexEgg) myrmex1).isInNursery());
        this.myrmex = myrmex;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || !this.myrmex.getNavigation().isIdle() || this.myrmex.shouldEnterHive() || !this.myrmex.keepSearching || this.myrmex.holdingBaby()) {
            this.listBabies = IafMath.emptyLivingEntityList;
            return false;
        }

        if (this.myrmex.getWorld().getTime() % 4 == 0) // only update the list every 4 ticks
            this.listBabies = this.mob.getWorld().getEntitiesByClass(LivingEntity.class, this.getTargetableArea(), this.targetEntitySelector);

        if (this.listBabies.isEmpty()) return false;

        this.listBabies.sort(this.theNearestAttackableTargetSorter);
        this.targetEntity = this.listBabies.get(0);
        return true;
    }

    protected Box getTargetableArea() {
        return this.mob.getBoundingBox().expand(20, 4.0D, 20);
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.squaredDistanceTo(this.targetEntity) < 2)
            this.targetEntity.startRiding(this.myrmex);
        this.stop();
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(EntityMyrmexBase theEntityIn) {
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