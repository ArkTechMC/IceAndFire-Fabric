package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAITargetSheepPlayers;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateCyclops;
import com.google.common.base.Predicate;
import dev.arktechmc.iafextra.event.EventBus;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityCyclops extends HostileEntity implements IAnimatedEntity, IBlacklistedFromStatues, IVillagerFear, IHumanoid, IHasCustomizableAttributes {

    private static final TrackedData<Boolean> BLINDED = DataTracker.registerData(EntityCyclops.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(EntityCyclops.class, TrackedDataHandlerRegistry.INTEGER);
    public static Animation ANIMATION_STOMP;
    public static Animation ANIMATION_EATPLAYER;
    public static Animation ANIMATION_KICK;
    public static Animation ANIMATION_ROAR;
    public EntityCyclopsEye eyeEntity;
    private int animationTick;
    private Animation currentAnimation;

    public EntityCyclops(EntityType<EntityCyclops> type, World worldIn) {
        super(type, worldIn);
        this.setStepHeight(2.5F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, 0.0F);
        ANIMATION_STOMP = Animation.create(27);
        ANIMATION_EATPLAYER = Animation.create(40);
        ANIMATION_KICK = Animation.create(20);
        ANIMATION_ROAR = Animation.create(30);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, IafConfig.cyclopsMaxHealth)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, IafConfig.cyclopsAttackStrength)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 20.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(IafConfig.cyclopsMaxHealth);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35D);
    }

    @Override
    protected EntityNavigation createNavigation(World worldIn) {
        return new PathNavigateCyclops(this, this.getWorld());
    }

    @Override
    public int getXpToDrop() {
        return 40;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(3, new EscapeSunlightGoal(this, 1.0D));
        this.goalSelector.add(3, new CyclopsAIAttackMelee(this, 1.0D, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F, 1.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, true, (Predicate<LivingEntity>) entity -> {
            if (EntityGorgon.isStoneMob(entity))
                return false;
            if (!DragonUtils.isAlive(entity))
                return false;
            if (entity instanceof WaterCreatureEntity)
                return false;
            if (entity instanceof PlayerEntity playerEntity) {
                if (playerEntity.isCreative() || playerEntity.isSpectator())
                    return false;
            }
            if (entity instanceof EntityCyclops)
                return false;
            if (entity instanceof AnimalEntity) {
                if (!(entity instanceof WolfEntity || entity instanceof PolarBearEntity || entity instanceof EntityDragonBase)) {
                    return false;
                }
            }
            return !ServerEvents.isSheep(entity);
        }));

        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, true, (Predicate<PlayerEntity>) entity -> entity != null && !(entity.isCreative() || entity.isSpectator())));
        this.targetSelector.add(3, new CyclopsAITargetSheepPlayers<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void pushAway(Entity entityIn) {
        if (!ServerEvents.isSheep(entityIn)) {
            entityIn.pushAwayFrom(this);
        }
    }

    @Override
    public boolean tryAttack(Entity entityIn) {
        int attackDescision = this.getRandom().nextInt(3);
        if (attackDescision == 0) {
            this.setAnimation(ANIMATION_STOMP);
            return true;
        } else if (attackDescision == 1) {
            if (!entityIn.hasPassenger(this)
                    && entityIn.getWidth() < 1.95F
                    && !(entityIn instanceof EntityDragonBase)
                    && !entityIn.getType().isIn((TagKey.of(RegistryKeys.ENTITY_TYPE, IafTagRegistry.CYCLOPS_UNLIFTABLES)))) {
                this.setAnimation(ANIMATION_EATPLAYER);
                entityIn.stopRiding();
                entityIn.startRiding(this, true);
            } else {
                this.setAnimation(ANIMATION_STOMP);
            }
            return true;
        } else {
            this.setAnimation(ANIMATION_KICK);
            return true;
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BLINDED, Boolean.FALSE);
        this.dataTracker.startTracking(VARIANT, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("Blind", this.isBlinded());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setBlinded(compound.getBoolean("Blind"));
        this.setVariant(compound.getInt("Variant"));
        this.setConfigurableAttributes();
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    public boolean isBlinded() {
        return this.dataTracker.get(BLINDED);
    }

    public void setBlinded(boolean blind) {
        this.dataTracker.set(BLINDED, blind);
    }

    @Override
    public void updatePassengerPosition(Entity passenger, PositionUpdater callback) {
        super.updatePassengerPosition(passenger, callback);
        if (this.hasPassenger(passenger)) {
            passenger.setVelocity(0, passenger.getVelocity().y, 0);
            this.setAnimation(ANIMATION_EATPLAYER);
            double raiseUp = this.getAnimationTick() < 10 ? 0 : Math.min((this.getAnimationTick() * 3 - 30) * 0.2, 5.2F);
            float pullIn = this.getAnimationTick() < 15 ? 0 : Math.min((this.getAnimationTick() - 15) * 0.15F, 0.75F);
            this.bodyYaw = this.getYaw();
            this.setYaw(0);
            float radius = -2.75F + pullIn;
            float angle = (0.01745329251F * this.bodyYaw) + 3.15F;
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            passenger.setPosition(this.getX() + extraX, this.getY() + raiseUp, this.getZ() + extraZ);
            if (this.getAnimationTick() == 32) {
                passenger.damage(this.getWorld().getDamageSources().mobAttack(this), (float) IafConfig.cyclopsBiteStrength);
                passenger.stopRiding();
            }
        }
    }

    @Override
    public void travel(Vec3d vec) {
        if (this.getAnimation() == ANIMATION_EATPLAYER) {
            super.travel(vec.multiply(0, 0, 0));
            return;
        }
        super.travel(vec);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.eyeEntity == null) {
            this.eyeEntity = new EntityCyclopsEye(this, 0.2F, 0, 7.4F, 1.2F, 0.6F, 1);
            this.eyeEntity.copyPositionAndRotation(this);
        }
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof PlayerEntity) {
            this.setTarget(null);
        }
        if (this.isBlinded() && this.getTarget() != null && this.squaredDistanceTo(this.getTarget()) > 6) {
            this.setTarget(null);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.CYCLOPS_BLINDED, 1, 1);
        }
        if (this.getAnimation() == ANIMATION_EATPLAYER && this.getAnimationTick() == 25) {
            this.playSound(IafSoundRegistry.CYCLOPS_BITE, 1, 1);
        }
        if (this.getAnimation() == ANIMATION_STOMP && this.getTarget() != null && this.squaredDistanceTo(this.getTarget()) < 12D && this.getAnimationTick() == 14) {
            this.getTarget().damage(this.getWorld().getDamageSources().mobAttack(this), (float) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue());
        }
        if (this.getAnimation() == ANIMATION_KICK && this.getTarget() != null && this.squaredDistanceTo(this.getTarget()) < 14D && this.getAnimationTick() == 12) {
            this.getTarget().damage(this.getWorld().getDamageSources().mobAttack(this), (float) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue());
            if (this.getTarget() != null)
                this.getTarget().takeKnockback(2, this.getX() - this.getTarget().getX(), this.getZ() - this.getTarget().getZ());

        }
        if (this.getAnimation() != ANIMATION_EATPLAYER && this.getTarget() != null && !this.getPassengerList().isEmpty() && this.getPassengerList().contains(this.getTarget())) {
            this.setAnimation(ANIMATION_EATPLAYER);
        }
        if (this.getAnimation() == NO_ANIMATION && this.getTarget() != null && this.getRandom().nextInt(100) == 0) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() == 14) {
            for (int i1 = 0; i1 < 20; i1++) {
                double motionX = this.getRandom().nextGaussian() * 0.07D;
                double motionY = this.getRandom().nextGaussian() * 0.07D;
                double motionZ = this.getRandom().nextGaussian() * 0.07D;
                float radius = 0.75F * -2F;
                float angle = (0.01745329251F * this.bodyYaw) + i1 * 1F;
                double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * MathHelper.cos(angle);

                BlockState BlockState = this.getWorld().getBlockState(BlockPos.ofFloored(this.getX() + extraX, this.getY() + extraY - 1, this.getZ() + extraZ));
                if (BlockState.isAir()) {
                    if (this.getWorld().isClient) {
                        this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, BlockState), this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ, motionX, motionY, motionZ);
                    }
                }
            }
        }

        AnimationHandler.INSTANCE.updateAnimations(this);

        if (this.eyeEntity == null) {
            this.eyeEntity = new EntityCyclopsEye(this, 0.2F, 0, 7.4F, 1.2F, 0.5F, 1);
            this.eyeEntity.copyPositionAndRotation(this);
        }

        EntityUtil.updatePart(this.eyeEntity, this);

        this.breakBlock();
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        spawnDataIn = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRandom().nextInt(4));
        return spawnDataIn;
    }

    public void breakBlock() {
        if (IafConfig.cyclopsGriefing) {
            for (int a = (int) Math.round(this.getBoundingBox().minX) - 1; a <= (int) Math.round(this.getBoundingBox().maxX) + 1; a++) {
                for (int b = (int) Math.round(this.getBoundingBox().minY) + 1; (b <= (int) Math.round(this.getBoundingBox().maxY) + 2) && (b <= 127); b++) {
                    for (int c = (int) Math.round(this.getBoundingBox().minZ) - 1; c <= (int) Math.round(this.getBoundingBox().maxZ) + 1; c++) {
                        BlockPos pos = new BlockPos(a, b, c);
                        BlockState state = this.getWorld().getBlockState(pos);
                        Block block = state.getBlock();
                        if (!state.isAir() && !state.getOutlineShape(this.getWorld(), pos).isEmpty() && !(block instanceof PlantBlock) && block != Blocks.BEDROCK && (state.getBlock() instanceof LeavesBlock || state.isIn(BlockTags.LOGS))) {
                            this.getVelocity().multiply(0.6D);
                            if (EventBus.post(new GenericGriefEvent(this, a, b, c))) continue;
                            if (block != Blocks.AIR) {
                                if (!this.getWorld().isClient) {
                                    this.getWorld().breakBlock(pos, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.eyeEntity != null) {
            this.eyeEntity.remove(reason);
        }
        super.remove(reason);
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_STOMP, ANIMATION_EATPLAYER, ANIMATION_KICK, ANIMATION_ROAR};
    }

    public boolean isBlinking() {
        return this.age % 50 > 40 && !this.isBlinded();
    }


    public void onHitEye(DamageSource source, float damage) {
        if (!this.isBlinded()) {
            this.setBlinded(true);
            this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(6F);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35D);
            this.setAnimation(ANIMATION_ROAR);
            this.damage(source, damage * 3);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.CYCLOPS_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return IafSoundRegistry.CYCLOPS_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.CYCLOPS_DIE;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return !this.isBlinded();
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

}
