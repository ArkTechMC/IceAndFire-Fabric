package com.iafenvoy.iceandfire.entity;

import com.google.common.base.Predicate;
import com.iafenvoy.citadel.animation.Animation;
import com.iafenvoy.citadel.animation.AnimationHandler;
import com.iafenvoy.citadel.animation.IAnimatedEntity;
import com.iafenvoy.iceandfire.IafConfig;
import com.iafenvoy.iceandfire.entity.ai.GhostAICharge;
import com.iafenvoy.iceandfire.entity.ai.GhostPathNavigator;
import com.iafenvoy.iceandfire.entity.util.*;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.IafParticles;
import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class EntityGhost extends HostileEntity implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid, IBlacklistedFromStatues, IHasCustomizableAttributes {

    private static final TrackedData<Integer> COLOR = DataTracker.registerData(EntityGhost.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(EntityGhost.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_DAYTIME_MODE = DataTracker.registerData(EntityGhost.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> WAS_FROM_CHEST = DataTracker.registerData(EntityGhost.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> DAYTIME_COUNTER = DataTracker.registerData(EntityGhost.class, TrackedDataHandlerRegistry.INTEGER);
    public static Animation ANIMATION_SCARE;
    public static Animation ANIMATION_HIT;
    private int animationTick;
    private Animation currentAnimation;


    public EntityGhost(EntityType<EntityGhost> type, World worldIn) {
        super(type, worldIn);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
        this.moveControl = new MoveHelper(this);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, IafConfig.ghostMaxHealth)
                //FOLLOW_RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, IafConfig.ghostAttackStrength)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 1D);
    }

    @Override
    protected Identifier getLootTableId() {
        return this.wasFromChest() ? LootTables.EMPTY : this.getType().getLootTableId();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return IafSounds.GHOST_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSounds.GHOST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return IafSounds.GHOST_DIE;
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(IafConfig.ghostMaxHealth);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(IafConfig.ghostAttackStrength);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance potioneffectIn) {
        return potioneffectIn.getEffectType() != StatusEffects.POISON && potioneffectIn.getEffectType() != StatusEffects.WITHER && super.canHaveStatusEffect(potioneffectIn);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.isIn(DamageTypeTags.IS_FIRE) || source.isOf(DamageTypes.IN_WALL) || source.isOf(DamageTypes.CACTUS)
                || source.isOf(DamageTypes.DROWN) || source.isOf(DamageTypes.FALLING_BLOCK) || source.isOf(DamageTypes.FALLING_ANVIL) || source.isOf(DamageTypes.SWEET_BERRY_BUSH);
    }

    @Override
    protected EntityNavigation createNavigation(World worldIn) {
        return new GhostPathNavigator(this, worldIn);
    }

    public boolean isCharging() {
        return this.dataTracker.get(CHARGING);
    }

    public void setCharging(boolean moving) {
        this.dataTracker.set(CHARGING, moving);
    }

    public boolean isDaytimeMode() {
        return this.dataTracker.get(IS_DAYTIME_MODE);
    }

    public void setDaytimeMode(boolean moving) {
        this.dataTracker.set(IS_DAYTIME_MODE, moving);
    }

    public boolean wasFromChest() {
        return this.dataTracker.get(WAS_FROM_CHEST);
    }

    public void setFromChest(boolean moving) {
        this.dataTracker.set(WAS_FROM_CHEST, moving);
    }


    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(3, new EscapeSunlightGoal(this, 1.0D));
        this.goalSelector.add(3, new GhostAICharge(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F, 1.0F) {
            @Override
            public boolean shouldContinue() {
                if (this.target != null && this.target instanceof PlayerEntity && ((PlayerEntity) this.target).isCreative()) {
                    return false;
                }
                return super.shouldContinue();
            }
        });
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.6D) {
            @Override
            public boolean canStart() {
                this.chance = 60;
                return super.canStart();
            }
        });
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal(this, PlayerEntity.class, 10, false, false, (Predicate<Entity>) Entity::isAlive));
        this.targetSelector.add(3, new ActiveTargetGoal(this, LivingEntity.class, 10, false, false, (Predicate<Entity>) entity -> entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) && DragonUtils.isVillager(entity)));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.noClip = true;
        if (!this.getWorld().isClient) {
            boolean day = this.isAffectedByDaylight() && !this.wasFromChest();
            if (day) {
                if (!this.isDaytimeMode()) {
                    this.setAnimation(ANIMATION_SCARE);
                }
                this.setDaytimeMode(true);
            } else {
                this.setDaytimeMode(false);
                this.setDaytimeCounter(0);
            }
            if (this.isDaytimeMode()) {
                this.setVelocity(Vec3d.ZERO);
                this.setDaytimeCounter(this.getDaytimeCounter() + 1);
                if (this.getDaytimeCounter() >= 100) {
                    this.setInvisible(true);
                }
            } else {
                this.setInvisible(this.hasStatusEffect(StatusEffects.INVISIBILITY));
                this.setDaytimeCounter(0);
            }
        } else {
            if (this.getAnimation() == ANIMATION_SCARE && this.getAnimationTick() == 3 && !this.isHauntedShoppingList() && this.random.nextInt(3) == 0) {
                this.playSound(IafSounds.GHOST_JUMPSCARE, this.getSoundVolume(), this.getSoundPitch());
                if (this.getWorld().isClient) {
                    this.getWorld().addParticle(IafParticles.GHOST_APPEARANCE, this.getX(), this.getY(), this.getZ(), this.getId(), 0, 0);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_HIT && this.getTarget() != null) {
            if (this.distanceTo(this.getTarget()) < 1.4D && this.getAnimationTick() >= 4 && this.getAnimationTick() < 6) {
                this.playSound(IafSounds.GHOST_ATTACK, this.getSoundVolume(), this.getSoundPitch());
                this.tryAttack(this.getTarget());
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean isAiDisabled() {
        return this.isDaytimeMode() || super.isAiDisabled();
    }

    @Override
    public boolean isSilent() {
        return this.isDaytimeMode() || super.isSilent();
    }

    @Override
    protected boolean isAffectedByDaylight() {
        if (this.getWorld().isDay() && !this.getWorld().isClient) {
            float f = this.getWorld().getLightLevel(LightType.BLOCK, this.getBlockPos());
            BlockPos blockpos = this.getVehicle() instanceof BoatEntity ? (new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ())).up() : new BlockPos(this.getBlockX(), this.getBlockY() + 4, this.getBlockZ());
            return f > 0.5F && this.getWorld().isSkyVisible(blockpos);
        }

        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        if (itemstack != null && itemstack.getItem() == IafItems.MANUSCRIPT && !this.isHauntedShoppingList()) {
            this.setColor(-1);
            this.playSound(IafSounds.BESTIARY_PAGE, 1, 1);
            if (!player.isCreative()) {
                itemstack.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void travel(Vec3d vec) {
        float f4;
        if (this.isDaytimeMode()) {
            super.travel(Vec3d.ZERO);
            return;
        }
        super.travel(vec);
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        spawnDataIn = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColor(this.random.nextInt(3));
        if (this.random.nextInt(200) == 0) {
            this.setColor(-1);
        }

        return spawnDataIn;
    }


    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(COLOR, 0);
        this.getDataTracker().startTracking(CHARGING, false);
        this.getDataTracker().startTracking(IS_DAYTIME_MODE, false);
        this.getDataTracker().startTracking(WAS_FROM_CHEST, false);
        this.getDataTracker().startTracking(DAYTIME_COUNTER, 0);
    }

    public int getColor() {
        return MathHelper.clamp(this.getDataTracker().get(COLOR), -1, 2);
    }

    public void setColor(int color) {
        this.getDataTracker().set(COLOR, color);
    }

    public int getDaytimeCounter() {
        return this.getDataTracker().get(DAYTIME_COUNTER);
    }

    public void setDaytimeCounter(int counter) {
        this.getDataTracker().set(DAYTIME_COUNTER, counter);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setColor(compound.getInt("Color"));
        this.setDaytimeMode(compound.getBoolean("DaytimeMode"));
        this.setDaytimeCounter(compound.getInt("DaytimeCounter"));
        this.setFromChest(compound.getBoolean("FromChest"));

        this.setConfigurableAttributes();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("Color", this.getColor());
        compound.putBoolean("DaytimeMode", this.isDaytimeMode());
        compound.putInt("DaytimeCounter", this.getDaytimeCounter());
        compound.putBoolean("FromChest", this.wasFromChest());

    }

    public boolean isHauntedShoppingList() {
        return this.getColor() == -1;
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
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_SCARE, ANIMATION_HIT};
    }


    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return false;
    }

    static class MoveHelper extends MoveControl {
        final EntityGhost ghost;

        public MoveHelper(EntityGhost ghost) {
            super(ghost);
            this.ghost = ghost;
        }

        @Override
        public void tick() {
            if (this.state == State.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.getTargetX() - this.ghost.getX(), this.getTargetY() - this.ghost.getY(), this.getTargetZ() - this.ghost.getZ());
                double d0 = vec3d.length();
                double edgeLength = this.ghost.getBoundingBox().getAverageSideLength();
                if (d0 < edgeLength) {
                    this.state = State.WAIT;
                    this.ghost.setVelocity(this.ghost.getVelocity().multiply(0.5D));
                } else {
                    this.ghost.setVelocity(this.ghost.getVelocity().add(vec3d.multiply(this.speed * 0.5D * 0.05D / d0)));
                    if (this.ghost.getTarget() == null) {
                        Vec3d vec3d1 = this.ghost.getVelocity();
                        this.ghost.setYaw(-((float) MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float) Math.PI));
                        this.ghost.bodyYaw = this.ghost.getYaw();
                    } else {
                        double d4 = this.ghost.getTarget().getX() - this.ghost.getX();
                        double d5 = this.ghost.getTarget().getZ() - this.ghost.getZ();
                        this.ghost.setYaw(-((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI));
                        this.ghost.bodyYaw = this.ghost.getYaw();
                    }
                }
            }
        }
    }
}
