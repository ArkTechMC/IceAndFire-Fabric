package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetInWater;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetOutOfWater;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIFindWaterTarget;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIWander;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageSirenSong;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.AmphibiousSwimNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntitySiren extends HostileEntity implements IAnimatedEntity, IVillagerFear, IHasCustomizableAttributes {

    public static final int SEARCH_RANGE = 32;
    public static final Predicate<Entity> SIREN_PREY = p_apply_1_ -> (p_apply_1_ instanceof PlayerEntity && !((PlayerEntity) p_apply_1_).isCreative() && !p_apply_1_.isSpectator()) || p_apply_1_ instanceof MerchantEntity || p_apply_1_ instanceof IHearsSiren;
    private static final TrackedData<Integer> HAIR_COLOR = DataTracker.registerData(EntitySiren.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> AGGRESSIVE = DataTracker.registerData(EntitySiren.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SING_POSE = DataTracker.registerData(EntitySiren.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> SINGING = DataTracker.registerData(EntitySiren.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SWIMMING = DataTracker.registerData(EntitySiren.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CHARMED = DataTracker.registerData(EntitySiren.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Byte> CLIMBING = DataTracker.registerData(EntitySiren.class, TrackedDataHandlerRegistry.BYTE);
    public static Animation ANIMATION_BITE = Animation.create(20);
    public static Animation ANIMATION_PULL = Animation.create(20);
    public ChainBuffer tail_buffer;
    public float singProgress;
    public float swimProgress;
    public int singCooldown;
    private int animationTick;
    private Animation currentAnimation;
    private boolean isSinging;
    private boolean isSwimming;
    private boolean isLandNavigator;
    private int ticksAgressive;

    public EntitySiren(EntityType<EntitySiren> t, World worldIn) {
        super(t, worldIn);
        this.switchNavigator(true);
        if (worldIn.isClient) {
            tail_buffer = new ChainBuffer();
        }
        this.setStepHeight(1F);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SirenAIFindWaterTarget(this));
        this.goalSelector.add(1, new AquaticAIGetInWater(this, 1.0D));
        this.goalSelector.add(1, new AquaticAIGetOutOfWater(this, 1.0D));
        this.goalSelector.add(2, new SirenAIWander(this, 1));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F, 1.0F));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(4, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, new Predicate<PlayerEntity>() {
            @Override
            public boolean apply(PlayerEntity entity) {
                return EntitySiren.this.isAgressive() && !(entity.isCreative() || entity.isSpectator());
            }
        }));
        this.targetSelector.add(4, new ActiveTargetGoal(this, MerchantEntity.class, 10, true, false, new Predicate<MerchantEntity>() {
            @Override
            public boolean apply(MerchantEntity entity) {
                return EntitySiren.this.isAgressive();
            }
        }));
    }

    public static boolean isWearingEarplugs(LivingEntity entity) {
        ItemStack helmet = entity.getEquippedStack(EquipmentSlot.HEAD);
        return helmet.getItem() == IafItemRegistry.EARPLUGS.get() || helmet != ItemStack.EMPTY && helmet.getItem().getTranslationKey().contains("earmuff");
    }

    @Override
    public int getXpToDrop() {
        return 8;
    }

    @Override
    public float getPathfindingFavor(@NotNull BlockPos pos) {
        return getWorld().getBlockState(pos).isOf(Blocks.WATER) ? 10F : super.getPathfindingFavor(pos);
    }

    @Override
    public boolean tryAttack(@NotNull Entity entityIn) {
        if (this.getRandom().nextInt(2) == 0) {
            if (this.getAnimation() != ANIMATION_PULL) {
                this.setAnimation(ANIMATION_PULL);
                this.playSound(IafSoundRegistry.NAGA_ATTACK, 1, 1);
            }
        } else {
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
                this.playSound(IafSoundRegistry.NAGA_ATTACK, 1, 1);
            }
        }
        return true;
    }

    public boolean isDirectPathBetweenPoints(Vec3d vec1, Vec3d pos) {
        Vec3d Vector3d1 = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.getWorld().raycast(new RaycastContext(vec1, Vector3d1, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    public float getPathfindingPenalty(@NotNull PathNodeType nodeType) {
        return nodeType == PathNodeType.WATER ? 0F : super.getPathfindingPenalty(nodeType);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = new AmphibiousSwimNavigation(this, getWorld());
            this.isLandNavigator = true;
        } else {
            this.moveControl = new SwimmingMoveHelper();
            this.navigation = new SwimNavigation(this, getWorld());
            this.isLandNavigator = false;
        }
    }

    private boolean isPathOnHighGround() {
        if (this.navigation != null && this.navigation.getCurrentPath() != null && this.navigation.getCurrentPath().getEnd() != null) {
            BlockPos target = new BlockPos(this.navigation.getCurrentPath().getEnd().x, this.navigation.getCurrentPath().getEnd().y, this.navigation.getCurrentPath().getEnd().z);
            BlockPos siren = this.getBlockPos();
            return getWorld().isAir(siren.up()) && getWorld().isAir(target.up()) && target.getY() >= siren.getY();
        }
        return false;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        bodyYaw = getYaw();

        LivingEntity attackTarget = this.getTarget();
        if (singCooldown > 0) {
            singCooldown--;
            this.setSinging(false);
        }
        if (!getWorld().isClient && attackTarget == null && !this.isAgressive()) {
            this.setSinging(true);
        }
        if (this.getAnimation() == ANIMATION_BITE && attackTarget != null && this.squaredDistanceTo(attackTarget) < 7D && this.getAnimationTick() == 5) {
            attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), (float) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue());
        }
        if (this.getAnimation() == ANIMATION_PULL && attackTarget != null && this.squaredDistanceTo(attackTarget) < 16D && this.getAnimationTick() == 5) {
            attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), (float) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue());
            double attackmotionX = (Math.signum(this.getX() - attackTarget.getX()) * 0.5D - attackTarget.getVelocity().z) * 0.100000000372529 * 5;
            double attackmotionY = (Math.signum(this.getY() - attackTarget.getY() + 1) * 0.5D - attackTarget.getVelocity().y) * 0.100000000372529 * 5;
            double attackmotionZ = (Math.signum(this.getZ() - attackTarget.getZ()) * 0.5D - attackTarget.getVelocity().z) * 0.100000000372529 * 5;

            attackTarget.setVelocity(attackTarget.getVelocity().add(attackmotionX, attackmotionY, attackmotionZ));
            // float angle = (float) (Math.atan2(attackTarget.getDeltaMovement().z, attackTarget.getDeltaMovement().x) * 180.0D / Math.PI) - 90.0F;
            // entity.moveForward = 0.5F;
            double d0 = this.getX() - attackTarget.getX();
            double d2 = this.getZ() - attackTarget.getZ();
            double d1 = this.getY() - 1 - attackTarget.getY();
            double d3 = Math.sqrt((float) (d0 * d0 + d2 * d2));
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
            attackTarget.setPitch(updateRotation(attackTarget.getPitch(), f1, 30F));
            attackTarget.setYaw(updateRotation(attackTarget.getYaw(), f, 30F));
        }
        if (getWorld().isClient) {
            tail_buffer.calculateChainSwingBuffer(40, 10, 2.5F, this);
        }
        if (this.isAgressive()) {
            ticksAgressive++;
        } else {
            ticksAgressive = 0;
        }
        if (ticksAgressive > 300 && this.isAgressive() && attackTarget == null && !getWorld().isClient) {
            this.setAttacking(false);
            this.ticksAgressive = 0;
            this.setSinging(false);
        }

        if (this.isTouchingWater() && !this.isSwimming()) {
            this.setSwimming(true);
        }
        if (!this.isTouchingWater() && this.isSwimming()) {
            this.setSwimming(false);
        }
        LivingEntity target = getTarget();
        boolean pathOnHighGround = this.isPathOnHighGround() || !getWorld().isClient && target != null && !target.isTouchingWater();
        if (target == null || !target.isTouchingWater() && !target.isTouchingWater()) {
            if (pathOnHighGround && this.isTouchingWater()) {
                jump();
                onSwimmingStart();
            }
        }
        if ((this.isTouchingWater() && !pathOnHighGround) && this.isLandNavigator) {
            switchNavigator(false);
        }
        if ((!this.isTouchingWater() || pathOnHighGround) && !this.isLandNavigator) {
            switchNavigator(true);
        }
        if (target instanceof PlayerEntity && ((PlayerEntity) target).isCreative()) {
            this.setTarget(null);
            this.setAttacking(false);
        }
        if (target != null && !this.isAgressive()) {
            this.setAttacking(true);
        }
        boolean singing = isActuallySinging() && !this.isAgressive() && !this.isTouchingWater() && isOnGround();
        if (singing && singProgress < 20.0F) {
            singProgress += 1F;
        } else if (!singing && singProgress > 0.0F) {
            singProgress -= 1F;
        }
        boolean swimming = isSwimming();
        if (swimming && swimProgress < 20.0F) {
            swimProgress += 1F;
        } else if (!swimming && swimProgress > 0.0F) {
            swimProgress -= 0.5F;
        }
        if (!getWorld().isClient && !EntityGorgon.isStoneMob(this) && this.isActuallySinging()) {
            updateLure();
            checkForPrey();

        }
        if (!getWorld().isClient && EntityGorgon.isStoneMob(this) && this.isSinging()) {
            this.setSinging(false);
        }
        if (isActuallySinging() && !this.isTouchingWater()) {
            if (this.getRandom().nextInt(3) == 0) {
                bodyYaw = getYaw();
                if (this.getWorld().isClient) {
                    float radius = -0.9F;
                    float angle = (0.01745329251F * this.bodyYaw) - 3F;
                    double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                    double extraY = 1.2F;
                    double extraZ = radius * MathHelper.cos(angle);
                    IceAndFire.PROXY.spawnParticle(EnumParticles.Siren_Music, this.getX() + extraX + this.random.nextFloat() - 0.5, this.getY() + extraY + this.random.nextFloat() - 0.5, this.getZ() + extraZ + this.random.nextFloat() - 0.5, 0, 0, 0);
                }
            }
        }
        if (this.isActuallySinging() && !this.isTouchingWater() && this.age % 200 == 0) {
            this.playSound(IafSoundRegistry.SIREN_SONG, 2, 1);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void checkForPrey() {
        this.setSinging(true);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity) {
            this.triggerOtherSirens((LivingEntity) source.getAttacker());
        }
        return super.damage(source, amount);
    }

    public void triggerOtherSirens(LivingEntity aggressor) {
        List<Entity> entities = getWorld().getOtherEntities(this, this.getBoundingBox().expand(12, 12, 12));
        for (Entity entity : entities) {
            if (entity instanceof EntitySiren) {
                ((EntitySiren) entity).setTarget(aggressor);
                ((EntitySiren) entity).setAttacking(true);
                ((EntitySiren) entity).setSinging(false);

            }
        }
    }

    public void updateLure() {
        if (this.age % 20 == 0) {
            List<LivingEntity> entities = getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(50, 12, 50), SIREN_PREY);

            for (LivingEntity entity : entities) {
                if (isWearingEarplugs(entity)) {
                    continue;
                }

                EntityDataProvider.getCapability(entity).ifPresent(data -> {
                    if (data.sirenData.isCharmed || data.sirenData.charmedBy == null) {
                        data.sirenData.setCharmed(this);
                    }
                });
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(@NotNull NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("HairColor", this.getHairColor());
        tag.putBoolean("Aggressive", this.isAgressive());
        tag.putInt("SingingPose", this.getSingingPose());
        tag.putBoolean("Singing", this.isSinging());
        tag.putBoolean("Swimming", this.isSwimming());
        tag.putBoolean("Passive", this.isCharmed());

    }

    @Override
    public void readCustomDataFromNbt(@NotNull NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setHairColor(tag.getInt("HairColor"));
        this.setAttacking(tag.getBoolean("Aggressive"));
        this.setSingingPose(tag.getInt("SingingPose"));
        this.setSinging(tag.getBoolean("Singing"));
        this.setSwimming(tag.getBoolean("Swimming"));
        this.setCharmed(tag.getBoolean("Passive"));
        this.setConfigurableAttributes();
    }

    public boolean isSinging() {
        if (getWorld().isClient) {
            return this.isSinging = this.dataTracker.get(SINGING).booleanValue();
        }
        return isSinging;
    }

    public void setSinging(boolean singing) {
        if (singCooldown > 0) {
            singing = false;
        }
        this.dataTracker.set(SINGING, singing);
        if (!getWorld().isClient) {
            this.isSinging = singing;
            IceAndFire.sendMSGToAll(new MessageSirenSong(this.getId(), singing));
        }
    }

    public boolean wantsToSing() {
        return this.isSinging() && this.isTouchingWater() && !this.isAgressive();
    }

    public boolean isActuallySinging() {
        return isSinging() && !wantsToSing();
    }

    @Override
    public boolean isSwimming() {
        if (getWorld().isClient) {
            return this.isSwimming = this.dataTracker.get(SWIMMING).booleanValue();
        }
        return isSwimming;
    }

    @Override
    public void setSwimming(boolean swimming) {
        this.dataTracker.set(SWIMMING, swimming);
        if (!getWorld().isClient) {
            this.isSwimming = swimming;
        }
    }

    @Override
    public void setAttacking(boolean aggressive) {
        this.dataTracker.set(AGGRESSIVE, aggressive);
    }

    public boolean isAgressive() {
        return this.dataTracker.get(AGGRESSIVE).booleanValue();
    }

    public boolean isCharmed() {
        return this.dataTracker.get(CHARMED).booleanValue();
    }

    public void setCharmed(boolean aggressive) {
        this.dataTracker.set(CHARMED, aggressive);
    }

    public int getHairColor() {
        return this.dataTracker.get(HAIR_COLOR).intValue();
    }

    public void setHairColor(int hairColor) {
        this.dataTracker.set(HAIR_COLOR, hairColor);
    }

    public int getSingingPose() {
        return this.dataTracker.get(SING_POSE).intValue();
    }

    public void setSingingPose(int pose) {
        this.dataTracker.set(SING_POSE, MathHelper.clamp(pose, 0, 2));
    }


    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, IafConfig.sirenMaxHealth)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(IafConfig.sirenMaxHealth);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HAIR_COLOR, 0);
        this.dataTracker.startTracking(SING_POSE, 0);
        this.dataTracker.startTracking(AGGRESSIVE, Boolean.FALSE);
        this.dataTracker.startTracking(SINGING, Boolean.FALSE);
        this.dataTracker.startTracking(SWIMMING, Boolean.FALSE);
        this.dataTracker.startTracking(CHARMED, Boolean.FALSE);
        this.dataTracker.startTracking(CLIMBING, (byte) 0);
    }

    @Override
    public EntityData initialize(@NotNull ServerWorldAccess worldIn, @NotNull LocalDifficulty difficultyIn, @NotNull SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        spawnDataIn = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHairColor(this.getRandom().nextInt(3));
        this.setSingingPose(this.getRandom().nextInt(3));
        return spawnDataIn;
    }

    public static float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
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
        return new Animation[]{NO_ANIMATION, ANIMATION_BITE, ANIMATION_PULL};
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAgressive() ? IafSoundRegistry.NAGA_IDLE : IafSoundRegistry.MERMAID_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return this.isAgressive() ? IafSoundRegistry.NAGA_HURT : IafSoundRegistry.MERMAID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isAgressive() ? IafSoundRegistry.NAGA_DIE : IafSoundRegistry.MERMAID_DIE;
    }

    @Override
    public void travel(@NotNull Vec3d motion) {
        super.travel(motion);
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean shouldFear() {
        return isAgressive();
    }

    class SwimmingMoveHelper extends MoveControl {
        private final EntitySiren siren = EntitySiren.this;

        public SwimmingMoveHelper() {
            super(EntitySiren.this);
        }

        @Override
        public void tick() {
            if (this.state == State.MOVE_TO) {
                double distanceX = this.targetX - siren.getX();
                double distanceY = this.targetY - siren.getY();
                double distanceZ = this.targetZ - siren.getZ();
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY = distanceY / distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                siren.setYaw(this.wrapDegrees(siren.getYaw(), angle, 30.0F));
                siren.setMovementSpeed(1F);
                float f1 = 0;
                float f2 = 0;
                if (distance < (double) Math.max(1.0F, siren.getWidth())) {
                    float f = siren.getYaw() * 0.017453292F;
                    f1 -= (double) (MathHelper.sin(f) * 0.35F);
                    f2 += (double) (MathHelper.cos(f) * 0.35F);
                }
                siren.setVelocity(siren.getVelocity().add(f1, siren.getMovementSpeed() * distanceY * 0.1D, f2));
            } else if (this.state == State.JUMPING) {
                siren.setMovementSpeed((float) (this.speed * siren.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue()));
                if (siren.isOnGround()) {
                    this.state = State.WAIT;
                }
            } else {
                siren.setMovementSpeed(0.0F);
            }
        }
    }
}
