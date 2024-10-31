package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.entity.ai.*;
import com.iafenvoy.uranus.object.entity.pathfinding.raycoms.AdvancedPathNavigate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class EntityMyrmexSwarmer extends EntityMyrmexRoyal {

    private static final TrackedData<Optional<UUID>> SUMMONER_ID = DataTracker.registerData(EntityMyrmexSwarmer.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Integer> TICKS_ALIVE = DataTracker.registerData(EntityMyrmexSwarmer.class, TrackedDataHandlerRegistry.INTEGER);

    public EntityMyrmexSwarmer(EntityType type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new FlyMoveHelper(this);
        this.navigation = this.createNavigator(this.getWorld(), AdvancedPathNavigate.MovementType.FLYING);
        this.switchNavigator(false);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 0D);
    }

    @Override
    public int getXpToDrop() {
        return 0;
    }

    @Override
    protected void switchNavigator(boolean onLand) {
    }

    @Override
    protected double attackDistance() {
        return 25;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MyrmexAIFollowSummoner(this, 1.0D, 10.0F, 5.0F));
        this.goalSelector.add(2, new AIFlyAtTarget());
        this.goalSelector.add(3, new AIFlyRandom());
        this.goalSelector.add(4, new EntityAIAttackMeleeNoCooldown(this, 1.0D, true));
        this.goalSelector.add(5, new MyrmexAIWander(this, 1D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new MyrmexAISummonerHurtByTarget(this));
        this.targetSelector.add(3, new MyrmexAISummonerHurtTarget(this));
    }

    @Override
    protected void pushAway(Entity entityIn) {
        if (entityIn instanceof EntityMyrmexSwarmer) {
            super.pushAway(entityIn);
        }
    }

    @Override
    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        super.fall(y, onGroundIn, state, pos);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SUMMONER_ID, Optional.empty());
        this.dataTracker.startTracking(TICKS_ALIVE, 0);
    }

    public LivingEntity getSummoner() {
        try {
            UUID uuid = this.getSummonerUUID();
            return uuid == null ? null : this.getWorld().getPlayerByUuid(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    @Override
    public boolean isTeammate(Entity entityIn) {
        if (entityIn == null) {
            return false;
        }
        if (this.getSummonerUUID() == null || entityIn instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID() == null) {
            return false;
        }
        if (entityIn instanceof TameableEntity) {
            UUID ownerID = ((TameableEntity) entityIn).getOwnerUuid();
            return ownerID != null && ownerID.equals(this.getSummonerUUID());
        }
        return entityIn.getUuid().equals(this.getSummonerUUID()) || entityIn instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID() != null && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID().equals(this.getSummonerUUID());
    }

    public void setSummonerID(UUID uuid) {
        this.dataTracker.set(SUMMONER_ID, Optional.ofNullable(uuid));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        if (this.getSummonerUUID() == null) {
            compound.putString("SummonerUUID", "");
        } else {
            compound.putString("SummonerUUID", this.getSummonerUUID().toString());
        }
        compound.putInt("SummonTicks", this.getTicksAlive());

    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        String s = "";
        if (compound.containsUuid("SummonerUUID")) {
            s = compound.getString("SummonerUUID");
        }
        if (!s.isEmpty()) {
            try {
                this.setSummonerID(UUID.fromString(s));
            } catch (Throwable ignored) {
            }
        }
        this.setTicksAlive(compound.getInt("SummonTicks"));
    }

    public void setSummonedBy(PlayerEntity player) {
        this.setSummonerID(player.getUuid());
    }

    public UUID getSummonerUUID() {
        return this.dataTracker.get(SUMMONER_ID).orElse(null);
    }

    public int getTicksAlive() {
        return this.dataTracker.get(TICKS_ALIVE);
    }

    public void setTicksAlive(int ticks) {
        this.dataTracker.set(TICKS_ALIVE, ticks);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.setFlying(true);
        boolean flying = this.isFlying() && !this.isOnGround();
        this.setTicksAlive(this.getTicksAlive() + 1);
        if (flying) {
            this.setVelocity(this.getVelocity().add(0, -0.08D, 0));
            if (this.moveControl.getTargetY() > this.getY()) {
                this.setVelocity(this.getVelocity().add(0, 0.08D, 0));
            }
        }
        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().add(0, 0.2D, 0));
        }
        if (this.getTarget() != null) {
            this.moveControl.moveTo(this.getTarget().getX(), this.getTarget().getBoundingBox().minY, this.getTarget().getZ(), 1.0F);
            if (this.getAttackBounds().intersects(this.getTarget().getBoundingBox())) {
                this.setAnimation(this.random.nextBoolean() ? ANIMATION_BITE : ANIMATION_STING);
            }
        }
        if (this.getTicksAlive() > 1800) {
            this.kill();
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getTarget() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            double dist = this.squaredDistanceTo(this.getTarget());
            if (dist < this.attackDistance()) {
                this.getTarget().damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getTarget() != null && this.getAnimationTick() == 6) {
            this.playStingSound();
            double dist = this.squaredDistanceTo(this.getTarget());
            if (dist < this.attackDistance()) {
                this.getTarget().damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() * 2));
                // After calling hurt the target can become null due to forge hooks
                if (this.getTarget() != null)
                    this.getTarget().addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 70, 1));
            }
        }
    }

    @Override
    public int getGrowthStage() {
        return 2;
    }

    @Override
    protected Identifier getLootTableId() {
        return null;
    }

    @Override
    public float getModelScale() {
        return 0.25F;
    }

    @Override
    public boolean shouldHaveNormalAI() {
        return false;
    }

    @Override
    public int getCasteImportance() {
        return 0;
    }

    @Override
    public boolean isBreedingSeason() {
        return false;
    }

    public boolean shouldAttackEntity(LivingEntity attacker, LivingEntity LivingEntity) {
        return !this.isTeammate(attacker);
    }
}
