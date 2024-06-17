package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.datagen.tags.IafItemTags;
import com.iafenvoy.iceandfire.entity.EntityHippogryph;
import com.iafenvoy.iceandfire.util.IafMath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class HippogryphAITargetItems<T extends ItemEntity> extends TrackTargetGoal {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected final int targetChance;
    protected ItemEntity targetEntity;
    private List<ItemEntity> list = IafMath.emptyItemEntityList;

    public HippogryphAITargetItems(MobEntity creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public HippogryphAITargetItems(MobEntity creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby, null);
    }

    public HippogryphAITargetItems(MobEntity creature, int chance, boolean checkSight, boolean onlyNearby, final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetChance = chance;
        this.targetEntitySelector = (Predicate<ItemEntity>) item -> item != null && !item.getStack().isEmpty() && item.getStack().isIn(IafItemTags.TAME_HIPPOGRYPH);
    }

    @Override
    public boolean canStart() {
        if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0)
            return false;
        if (!((EntityHippogryph) this.mob).canMove()) {
            this.list = IafMath.emptyItemEntityList;
            return false;
        }

        return this.updateList();
    }

    private boolean updateList() {
        this.list = this.mob.getWorld().getEntitiesByClass(ItemEntity.class, this.getTargetableArea(this.getFollowRange()), this.targetEntitySelector);
        if (this.list.isEmpty()) return false;
        else {
            this.list.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = this.list.get(0);
            return true;
        }
    }

    protected Box getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || !this.targetEntity.isAlive())
            this.stop();
        else if (this.getAttackReachSqr(this.targetEntity) >= this.mob.squaredDistanceTo(this.targetEntity)) {
            EntityHippogryph hippo = (EntityHippogryph) this.mob;
            this.targetEntity.getStack().decrement(1);
            this.mob.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            hippo.setAnimation(EntityHippogryph.ANIMATION_EAT);
            hippo.feedings++;
            hippo.heal(4);
            if (hippo.feedings > 3 && (hippo.feedings > 7 || hippo.getRandom().nextInt(3) == 0) && !hippo.isTamed() && this.targetEntity.getOwner() instanceof PlayerEntity owner) {
                hippo.setOwner(owner);
                hippo.setTarget(null);
                hippo.setCommand(1);
                hippo.setSitting(true);
            }
            this.stop();
        } else this.updateList();
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle();
    }

    protected double getAttackReachSqr(Entity attackTarget) {
        return this.mob.getWidth() * 2.0F * this.mob.getWidth() * 2.0F + attackTarget.getWidth();
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