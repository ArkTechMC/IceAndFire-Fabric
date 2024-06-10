package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class DeathwormAITargetItems<T extends ItemEntity> extends TrackTargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected final int targetChance;
    private final EntityDeathWorm worm;
    private final List<ItemEntity> list = IAFMath.emptyItemEntityList;
    protected ItemEntity targetEntity;

    public DeathwormAITargetItems(EntityDeathWorm creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public DeathwormAITargetItems(EntityDeathWorm creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 10, checkSight, onlyNearby, null);
    }

    public DeathwormAITargetItems(EntityDeathWorm creature, int chance, boolean checkSight, boolean onlyNearby,
                                  final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.worm = creature;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {
            @Override
            public boolean test(ItemEntity item) {
                return item != null && !item.getStack().isEmpty() && item.getStack().getItem() == Blocks.TNT.asItem() &&
                        item.getWorld().getBlockState(item.getBlockPos().down()).isIn(BlockTags.SAND);
            }
        };
        this.setControls(EnumSet.of(Control.TARGET));

    }

    @Override
    public boolean canStart() {
        if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
            return false;
        }
        List<ItemEntity> list = this.mob.getWorld().getEntitiesByClass(ItemEntity.class,
                this.getTargetableArea(this.getFollowRange()), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    protected Box getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.targetEntity.getX(), this.targetEntity.getY(),
                this.targetEntity.getZ(), 1);
        super.start();
    }

    @Override
    public boolean shouldContinue() {
        Entity itemTarget = this.targetEntity;

        if (itemTarget == null) {
            return false;
        } else if (!itemTarget.isAlive()) {
            return false;
        } else {
            AbstractTeam team = this.mob.getScoreboardTeam();
            AbstractTeam team1 = itemTarget.getScoreboardTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getFollowRange();
                return !(this.mob.squaredDistanceTo(itemTarget) > d0 * d0);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.stop();
        } else if (this.mob.squaredDistanceTo(this.targetEntity) < 1) {
            EntityDeathWorm deathWorm = (EntityDeathWorm) this.mob;
            this.targetEntity.getStack().decrement(1);
            this.mob.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            deathWorm.setAnimation(EntityDeathWorm.ANIMATION_BITE);
            PlayerEntity thrower = null;
            if (this.targetEntity.getOwner() != null)
                thrower = this.targetEntity.getWorld().getPlayerByUuid(this.targetEntity.getOwner().getUuid());
            deathWorm.setExplosive(true, thrower);
            this.stop();
        }

        if (this.worm.getNavigation().isIdle()) {
            this.worm.getNavigation().startMovingTo(this.targetEntity, 1.0F);
        }

    }


}
