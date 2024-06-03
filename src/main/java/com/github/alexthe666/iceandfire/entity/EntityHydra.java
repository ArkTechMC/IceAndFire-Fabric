package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityHydra extends HostileEntity implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear, IHasCustomizableAttributes {

    public static final int HEADS = 9;
    public static final double HEAD_HEALTH_THRESHOLD = 20;
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(EntityHydra.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> HEAD_COUNT = DataTracker.registerData(EntityHydra.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> SEVERED_HEAD = DataTracker.registerData(EntityHydra.class, TrackedDataHandlerRegistry.INTEGER);
    private static final float[][] ROTATE = new float[][]{
        {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
        {10F, -10F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
        {10F, 0F, -10F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
        {25F, 10F, -10F, -25F, 0F, 0F, 0F, 0F, 0F},//etc...
        {30F, 15F, 0F, -15F, -30F, 0F, 0F, 0F, 0F},
        {40F, 25F, 5F, -5F, -25F, -40F, 0F, 0F, 0F},
        {40F, 30F, 15F, 0F, -15F, -30F, -40F, 0F, 0F},
        {45F, 30F, 20F, 5F, -5F, -20F, -30F, -45F, 0F},
        {50F, 37F, 25F, 15F, 0, -15F, -25F, -37F, -50F},
    };
    public boolean[] isStriking = new boolean[HEADS];
    public float[] strikingProgress = new float[HEADS];
    public float[] prevStrikeProgress = new float[HEADS];
    public boolean[] isBreathing = new boolean[HEADS];
    public float[] speakingProgress = new float[HEADS];
    public float[] prevSpeakingProgress = new float[HEADS];
    public float[] breathProgress = new float[HEADS];
    public float[] prevBreathProgress = new float[HEADS];
    public int[] breathTicks = new int[HEADS];
    public float[] headDamageTracker = new float[HEADS];
    private int animationTick;
    private Animation currentAnimation;
    private EntityHydraHead[] headBoxes = new EntityHydraHead[HEADS * 9];
    private int strikeCooldown = 0;
    private int breathCooldown = 0;
    private int lastHitHead = 0;
    private int prevHeadCount = -1;
    private int regrowHeadCooldown = 0;
    private boolean onlyRegrowOneHeadNotTwo = false;
    private float headDamageThreshold = 20;

    public EntityHydra(EntityType<EntityHydra> type, World worldIn) {
        super(type, worldIn);
        resetParts();
        headDamageThreshold = Math.max(5, (float) IafConfig.hydraMaxHealth * 0.08F);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(EntityAttributes.GENERIC_MAX_HEALTH, IafConfig.hydraMaxHealth)
            //SPEED
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
            //ATTACK
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
            //ARMOR
            .add(EntityAttributes.GENERIC_ARMOR, 1.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(IafConfig.hydraMaxHealth);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal(this, LivingEntity.class, 10, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) && !(entity instanceof EntityMutlipartPart) && !(entity instanceof Monster) || (entity instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) entity).canBeTurnedToStone());
            }
        }));
    }

    @Override
    public boolean tryAttack(@NotNull Entity entityIn) {
        return false;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        LivingEntity attackTarget = this.getTarget();
        if (attackTarget != null && this.canSee(attackTarget)) {
            int index = random.nextInt(getHeadCount());
            if (!isBreathing[index] && !isStriking[index]) {
                if (this.distanceTo(attackTarget) < 6) {
                    if (strikeCooldown == 0 && strikingProgress[index] == 0) {
                        isBreathing[index] = false;
                        isStriking[index] = true;
                        this.getWorld().sendEntityStatus(this, (byte) (40 + index));
                        strikeCooldown = 3;
                    }
                } else if (random.nextBoolean() && breathCooldown == 0) {
                    isBreathing[index] = true;
                    isStriking[index] = false;
                    this.getWorld().sendEntityStatus(this, (byte) (50 + index));
                    breathCooldown = 15;
                }

            }

        }
        for (int i = 0; i < HEADS; i++) {
            boolean striking = isStriking[i];
            boolean breathing = isBreathing[i];
            prevStrikeProgress[i] = strikingProgress[i];
            if (striking && strikingProgress[i] > 9) {
                isStriking[i] = false;
                if (attackTarget != null && this.distanceTo(attackTarget) < 6) {
                    attackTarget.damage(getWorld().getDamageSources().mobAttack(this), (float) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue());
                    attackTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 3, false, false));
                    attackTarget.takeKnockback(0.25F, this.getX() - attackTarget.getX(), this.getZ() - attackTarget.getZ());
                }
            }
            if (breathing) {
                if (age % 7 == 0 && attackTarget != null && i < this.getHeadCount()) {
                    Vec3d Vector3d = this.getRotationVec(1.0F);
                    if (random.nextFloat() < 0.2F) {
                        this.playSound(IafSoundRegistry.HYDRA_SPIT, this.getSoundVolume(), this.getSoundPitch());
                    }
                    double headPosX = this.headBoxes[i].getX() + Vector3d.x;
                    double headPosY = this.headBoxes[i].getY() + 1.3F;
                    double headPosZ = this.headBoxes[i].getZ() + Vector3d.z;
                    double d2 = attackTarget.getX() - headPosX + this.random.nextGaussian() * 0.4D;
                    double d3 = attackTarget.getY() + attackTarget.getStandingEyeHeight() - headPosY + this.random.nextGaussian() * 0.4D;
                    double d4 = attackTarget.getZ() - headPosZ + this.random.nextGaussian() * 0.4D;
                    EntityHydraBreath entitylargefireball = new EntityHydraBreath(IafEntityRegistry.HYDRA_BREATH.get(),
                        getWorld(), this, d2, d3, d4);
                    entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
                    if (!getWorld().isClient) {
                        getWorld().spawnEntity(entitylargefireball);
                    }
                }
                if (isBreathing[i] && (attackTarget == null || !attackTarget.isAlive() || breathTicks[i] > 60) && !getWorld().isClient) {
                    isBreathing[i] = false;
                    breathTicks[i] = 0;
                    breathCooldown = 15;
                    this.getWorld().sendEntityStatus(this, (byte) (60 + i));
                }
                breathTicks[i]++;
            } else {
                breathTicks[i] = 0;
            }
            if (striking && strikingProgress[i] < 10.0F) {
                strikingProgress[i] += 2.5F;
            } else if (!striking && strikingProgress[i] > 0.0F) {
                strikingProgress[i] -= 2.5F;
            }
            prevSpeakingProgress[i] = speakingProgress[i];
            if (speakingProgress[i] > 0.0F) {
                speakingProgress[i] -= 0.1F;
            }
            prevBreathProgress[i] = breathProgress[i];
            if (breathing && breathProgress[i] < 10.0F) {
                breathProgress[i] += 1.0F;
            } else if (!breathing && breathProgress[i] > 0.0F) {
                breathProgress[i] -= 1.0F;
            }

        }
        if (strikeCooldown > 0) {
            strikeCooldown--;
        }
        if (breathCooldown > 0) {
            breathCooldown--;
        }
        if (this.getHeadCount() == 1 && this.getSeveredHead() != -1) {
            this.setSeveredHead(-1);
        }
        if (this.getHeadCount() == 1 && !this.isOnFire()) {
            this.setHeadCount(2);
            this.setSeveredHead(1);
            onlyRegrowOneHeadNotTwo = true;
        }

        if (this.getSeveredHead() != -1 && this.getSeveredHead() < this.getHeadCount()) {
            this.setSeveredHead(MathHelper.clamp(this.getSeveredHead(), 0, this.getHeadCount() - 1));
            regrowHeadCooldown++;
            if (regrowHeadCooldown >= 100) {
                headDamageTracker[this.getSeveredHead()] = 0;
                this.setSeveredHead(-1);
                if (this.isOnFire()) {
                    this.setHeadCount(this.getHeadCount() - 1);
                } else {
                    this.playSound(IafSoundRegistry.HYDRA_REGEN_HEAD, this.getSoundVolume(), this.getSoundPitch());
                    if (!onlyRegrowOneHeadNotTwo) {
                        this.setHeadCount(this.getHeadCount() + 1);
                    }
                }
                onlyRegrowOneHeadNotTwo = false;
                regrowHeadCooldown = 0;
            }
        } else {
            regrowHeadCooldown = 0;
        }
    }

    public void resetParts() {
        clearParts();
        headBoxes = new EntityHydraHead[HEADS * 2];
        for (int i = 0; i < getHeadCount(); i++) {
            float maxAngle = 5;
            headBoxes[i] = new EntityHydraHead(this, 3.2F, ROTATE[getHeadCount() - 1][i] * 1.1F, 1.0F, 0.75F, 1.75F, 1, i, false);
            headBoxes[HEADS + i] = new EntityHydraHead(this, 2.1F, ROTATE[getHeadCount() - 1][i] * 1.1F, 1.0F, 0.75F, 0.75F, 1, i, true);
            headBoxes[i].copyPositionAndRotation(this);
            headBoxes[HEADS + i].copyPositionAndRotation(this);
            headBoxes[i].setParent(this);
            headBoxes[HEADS + i].setParent(this);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (prevHeadCount != this.getHeadCount()) {
            resetParts();
        }

        float partY = 1.0F - this.limbAnimator.getSpeed() * 0.5F;

        for (int i = 0; i < getHeadCount(); i++) {
            headBoxes[i].setPosition(headBoxes[i].getX(), this.getY() + partY, headBoxes[i].getZ());
            EntityUtil.updatePart(headBoxes[i], this);

            headBoxes[HEADS + i].setPosition(headBoxes[HEADS + i].getX(), this.getY() + partY, headBoxes[HEADS + i].getZ());
            EntityUtil.updatePart(headBoxes[HEADS + 1], this);
        }

        if (getHeadCount() > 1 && !isOnFire()) {
            if (this.getHealth() < this.getMaxHealth() && this.age % 30 == 0) {
                int level = getHeadCount() - 1;
                if (this.getSeveredHead() != -1) {
                    level--;
                }

                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 30, level, false, false));
            }
        }

        if (isOnFire()) {
            this.removeStatusEffect(StatusEffects.REGENERATION);
        }

        prevHeadCount = this.getHeadCount();
    }

    private void clearParts() {
        for (Entity entity : headBoxes) {
            if (entity != null) {
                entity.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        clearParts();
        super.remove(reason);
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        speakingProgress[random.nextInt(getHeadCount())] = 1F;
        super.playHurtSound(source);
    }

    @Override
    public void playAmbientSound() {
        speakingProgress[random.nextInt(getHeadCount())] = 1F;
        super.playAmbientSound();
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 100 / getHeadCount();
    }

    @Override
    public void writeCustomDataToNbt(@NotNull NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("HeadCount", this.getHeadCount());
        compound.putInt("SeveredHead", this.getSeveredHead());
        for (int i = 0; i < HEADS; i++) {
            compound.putFloat("HeadDamage" + i, headDamageTracker[i]);
        }
    }

    @Override
    public void readCustomDataFromNbt(@NotNull NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setHeadCount(compound.getInt("HeadCount"));
        this.setSeveredHead(compound.getInt("SeveredHead"));
        for (int i = 0; i < HEADS; i++) {
            headDamageTracker[i] = compound.getFloat("HeadDamage" + i);
        }
        this.setConfigurableAttributes();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
        this.dataTracker.startTracking(HEAD_COUNT, 3);
        this.dataTracker.startTracking(SEVERED_HEAD, -1);
    }

    @Override
    public boolean damage(@NotNull DamageSource source, float amount) {
        if (lastHitHead > this.getHeadCount()) {
            lastHitHead = this.getHeadCount() - 1;
        }
        int headIndex = lastHitHead;
        headDamageTracker[headIndex] += amount;

        if (headDamageTracker[headIndex] > headDamageThreshold && (this.getSeveredHead() == -1 || this.getSeveredHead() >= this.getHeadCount())) {
            headDamageTracker[headIndex] = 0;
            this.regrowHeadCooldown = 0;
            this.setSeveredHead(headIndex);
            this.playSound(SoundEvents.ENTITY_GUARDIAN_FLOP, this.getSoundVolume(), this.getSoundPitch());
        }
        if (this.getHealth() <= amount + 5 && this.getHeadCount() > 1 && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            amount = 0;
        }
        return super.damage(source, amount);
    }

    @Override
    public EntityData initialize(@NotNull ServerWorldAccess worldIn, @NotNull LocalDifficulty difficultyIn, @NotNull SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        EntityData data = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(random.nextInt(3));
        return data;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    public int getHeadCount() {
        return MathHelper.clamp(this.dataTracker.get(HEAD_COUNT).intValue(), 1, HEADS);
    }

    public void setHeadCount(int count) {
        this.dataTracker.set(HEAD_COUNT, MathHelper.clamp(count, 1, HEADS));
    }

    public int getSeveredHead() {
        return MathHelper.clamp(this.dataTracker.get(SEVERED_HEAD).intValue(), -1, HEADS);
    }

    public void setSeveredHead(int count) {
        this.dataTracker.set(SEVERED_HEAD, MathHelper.clamp(count, -1, HEADS));
    }

    @Override
    public void handleStatus(byte id) {
        if (id >= 40 && id <= 48) {
            int index = id - 40;
            isStriking[MathHelper.clamp(index, 0, 8)] = true;
        } else if (id >= 50 && id <= 58) {
            int index = id - 50;
            isBreathing[MathHelper.clamp(index, 0, 8)] = true;
        } else if (id >= 60 && id <= 68) {
            int index = id - 60;
            isBreathing[MathHelper.clamp(index, 0, 8)] = false;
        } else {
            super.handleStatus(id);
        }
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance potioneffectIn) {
        return potioneffectIn.getEffectType() != StatusEffects.POISON && super.canHaveStatusEffect(potioneffectIn);
    }

    public void onHitHead(float damage, int headIndex) {
        lastHitHead = headIndex;
    }

    public void triggerHeadFlags(int index) {
        lastHitHead = index;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HYDRA_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.HYDRA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HYDRA_DIE;
    }

}
