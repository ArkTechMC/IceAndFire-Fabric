package com.iafenvoy.iceandfire.entity;

import com.google.common.base.Predicate;
import com.iafenvoy.citadel.animation.Animation;
import com.iafenvoy.citadel.animation.AnimationHandler;
import com.iafenvoy.citadel.animation.IAnimatedEntity;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.ai.*;
import com.iafenvoy.iceandfire.entity.util.HomePosition;
import com.iafenvoy.iceandfire.entity.util.IBlacklistedFromStatues;
import com.iafenvoy.iceandfire.entity.util.IHasCustomizableAttributes;
import com.iafenvoy.iceandfire.entity.util.IVillagerFear;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.registry.IafSounds;
import com.iafenvoy.iceandfire.registry.tag.IafEntityTags;
import com.iafenvoy.iceandfire.registry.tag.IafItemTags;
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
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;

import java.util.List;

public class EntityCockatrice extends TameableEntity implements IAnimatedEntity, IBlacklistedFromStatues, IVillagerFear, IHasCustomizableAttributes {

    public static final Animation ANIMATION_JUMPAT = Animation.create(30);
    public static final Animation ANIMATION_WATTLESHAKE = Animation.create(20);
    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SPEAK = Animation.create(10);
    public static final Animation ANIMATION_EAT = Animation.create(20);
    public static final float VIEW_RADIUS = 0.6F;
    private static final TrackedData<Boolean> HEN = DataTracker.registerData(EntityCockatrice.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> STARING = DataTracker.registerData(EntityCockatrice.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> TARGET_ENTITY = DataTracker.registerData(EntityCockatrice.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TAMING_PLAYER = DataTracker.registerData(EntityCockatrice.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TAMING_LEVEL = DataTracker.registerData(EntityCockatrice.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> COMMAND = DataTracker.registerData(EntityCockatrice.class, TrackedDataHandlerRegistry.INTEGER);
    private final CockatriceAIStareAttack aiStare;
    private final MeleeAttackGoal aiMelee;
    public float sitProgress;
    public float stareProgress;
    public int ticksStaring = 0;
    public HomePosition homePos;
    public boolean hasHomePosition = false;
    private int animationTick;
    private Animation currentAnimation;
    private boolean isSitting;
    private boolean isStaring;
    private boolean isMeleeMode = false;
    private LivingEntity targetedEntity;
    private int clientSideAttackTime;

    public EntityCockatrice(EntityType<EntityCockatrice> type, World worldIn) {
        super(type, worldIn);
        // Fix for some mods causing weird crashes
        this.lookControl = new IAFLookHelper(this);
        this.aiStare = new CockatriceAIStareAttack(this, 1.0D, 0, 15.0F);
        this.aiMelee = new EntityAIAttackMeleeNoCooldown(this, 1.5D, false);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, IafConfig.getInstance().cockatrice.maxHealth)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0D)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 2.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(IafConfig.getInstance().cockatrice.maxHealth);
    }

    @Override
    public int getXpToDrop() {
        return 10;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new CockatriceAIFollowOwner(this, 1.0D, 7.0F, 2.0F));
        this.goalSelector.add(3, new SitGoal(this));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, LivingEntity.class, 14.0F, 1.0D, 1.0D, (Predicate<LivingEntity>) entity -> {
            if (entity instanceof PlayerEntity player) return !player.isCreative() && !entity.isSpectator();
            else return entity.getType().isIn(IafEntityTags.SCARES_COCKATRICES) && !entity.getType().isIn(IafEntityTags.CHICKENS);
        }));
        this.goalSelector.add(4, new CockatriceAIWander(this, 1.0D));
        this.goalSelector.add(5, new CockatriceAIAggroLook(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new CockatriceAITargetItems<>(this, false));
        this.targetSelector.add(2, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(3, new AttackWithOwnerGoal(this));
        this.targetSelector.add(4, new RevengeGoal(this));
        this.targetSelector.add(5, new CockatriceAITarget(this, LivingEntity.class, true, (Predicate<Entity>) entity -> {
            if (entity instanceof PlayerEntity player) return !player.isCreative() && !entity.isSpectator();
            else
                return (entity instanceof Monster) && EntityCockatrice.this.isTamed() && !(entity instanceof CreeperEntity) && !(entity instanceof ZombifiedPiglinEntity) && !(entity instanceof EndermanEntity) || entity.getType().isIn(IafEntityTags.COCKATRICE_TARGETS) && (!entity.getType().isIn(IafEntityTags.CHICKENS));
        }));
    }

    @Override
    public boolean hasPositionTarget() {
        return this.hasHomePosition &&
                this.getCommand() == 3 &&
                this.getHomeDimensionName().equals(DragonUtils.getDimensionName(this.getWorld()))
                || super.hasPositionTarget();
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    public BlockPos getPositionTarget() {
        return this.hasHomePosition && this.getCommand() == 3 && this.homePos != null ? this.homePos.getPosition() : super.getPositionTarget();
    }

    @Override
    public float getPositionTargetRange() {
        return 30.0F;
    }

    public String getHomeDimensionName() {
        return this.homePos == null ? "" : this.homePos.getDimension();
    }

    @Override
    public boolean isTeammate(Entity entityIn) {
        if (entityIn.getType().isIn(IafEntityTags.CHICKENS))
            return true;
        if (this.isTamed()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity)
                return true;
            if (entityIn instanceof TameableEntity tameable)
                return tameable.isOwner(livingentity);
            if (livingentity != null)
                return livingentity.isTeammate(entityIn);
        }

        return super.isTeammate(entityIn);
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        if (source.getAttacker() != null) {
            Entity entity = source.getAttacker();
            if (entity.getType().isIn(IafEntityTags.SCARES_COCKATRICES))
                damage *= 5;
        }
        if (source == this.getWorld().getDamageSources().inWall())
            return false;
        return super.damage(source, damage);
    }

    private boolean canUseStareOn(Entity entity) {
        return (!(entity instanceof IBlacklistedFromStatues statues) || statues.canBeTurnedToStone()) && !entity.getType().isIn(IafEntityTags.COCKATRICE_TARGETS);
    }

    private void switchAI(boolean melee) {
        if (melee) {
            this.goalSelector.remove(this.aiStare);
            if (this.aiMelee != null)
                this.goalSelector.add(2, this.aiMelee);
            this.isMeleeMode = true;
        } else {
            this.goalSelector.remove(this.aiMelee);
            if (this.aiStare != null)
                this.goalSelector.add(2, this.aiStare);
            this.isMeleeMode = false;
        }
    }

    @Override
    public boolean tryAttack(Entity entityIn) {
        if (this.isStaring())
            return false;
        if (this.getRandom().nextBoolean()) {
            if (this.getAnimation() != ANIMATION_JUMPAT && this.getAnimation() != ANIMATION_BITE)
                this.setAnimation(ANIMATION_JUMPAT);
        } else {
            if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_JUMPAT)
                this.setAnimation(ANIMATION_BITE);
        }
        return false;
    }

    public boolean canMove() {
        return !this.isSitting() && !(this.getAnimation() == ANIMATION_JUMPAT && this.getAnimationTick() < 7);
    }


    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HEN, Boolean.FALSE);
        this.dataTracker.startTracking(STARING, Boolean.FALSE);
        this.dataTracker.startTracking(TARGET_ENTITY, 0);
        this.dataTracker.startTracking(TAMING_PLAYER, 0);
        this.dataTracker.startTracking(TAMING_LEVEL, 0);
        this.dataTracker.startTracking(COMMAND, 0);
    }

    public boolean hasTargetedEntity() {
        return this.dataTracker.get(TARGET_ENTITY) != 0;
    }

    public boolean hasTamingPlayer() {
        return this.dataTracker.get(TAMING_PLAYER) != 0;
    }

    public Entity getTamingPlayer() {
        if (!this.hasTamingPlayer())
            return null;
        else if (this.getWorld().isClient) {
            if (this.targetedEntity != null)
                return this.targetedEntity;
            else {
                Entity entity = this.getWorld().getEntityById(this.dataTracker.get(TAMING_PLAYER));
                if (entity instanceof LivingEntity livingEntity) return this.targetedEntity = livingEntity;
                else return null;
            }
        } else return this.getWorld().getEntityById(this.dataTracker.get(TAMING_PLAYER));
    }

    public void setTamingPlayer(int entityId) {
        this.dataTracker.set(TAMING_PLAYER, entityId);
    }

    public LivingEntity getTargetedEntity() {
        boolean blindness = this.hasStatusEffect(StatusEffects.BLINDNESS) || this.getTarget() != null && this.getTarget().hasStatusEffect(StatusEffects.BLINDNESS) || EntityGorgon.isBlindfolded(this.getTarget());
        if (blindness) return null;
        if (!this.hasTargetedEntity())
            return null;
        else if (this.getWorld().isClient) {
            if (this.targetedEntity != null)
                return this.targetedEntity;
            else {
                Entity entity = this.getWorld().getEntityById(this.dataTracker.get(TARGET_ENTITY));
                if (entity instanceof LivingEntity livingEntity) return this.targetedEntity = livingEntity;
                else return null;
            }
        } else return this.getTarget();
    }

    public void setTargetedEntity(int entityId) {
        this.dataTracker.set(TARGET_ENTITY, entityId);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> key) {
        super.onTrackedDataSet(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("Hen", this.isHen());
        tag.putBoolean("Staring", this.isStaring());
        tag.putInt("TamingLevel", this.getTamingLevel());
        tag.putInt("TamingPlayer", this.dataTracker.get(TAMING_PLAYER));
        tag.putInt("Command", this.getCommand());
        tag.putBoolean("HasHomePosition", this.hasHomePosition);
        if (this.homePos != null && this.hasHomePosition) {
            this.homePos.write(tag);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setHen(tag.getBoolean("Hen"));
        this.setStaring(tag.getBoolean("Staring"));
        this.setTamingLevel(tag.getInt("TamingLevel"));
        this.setTamingPlayer(tag.getInt("TamingPlayer"));
        this.setCommand(tag.getInt("Command"));
        this.hasHomePosition = tag.getBoolean("HasHomePosition");
        if (this.hasHomePosition && tag.getInt("HomeAreaX") != 0 && tag.getInt("HomeAreaY") != 0 && tag.getInt("HomeAreaZ") != 0)
            this.homePos = new HomePosition(tag, this.getWorld());
        this.setConfigurableAttributes();
    }

    @Override
    public boolean isSitting() {
        if (this.getWorld().isClient) {
            boolean isSitting = (this.dataTracker.get(TAMEABLE_FLAGS) & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        }
        return this.isSitting;
    }

    @Override
    public void setSitting(boolean sitting) {
        super.setSwimming(sitting);
        if (!this.getWorld().isClient)
            this.isSitting = sitting;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        spawnDataIn = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHen(this.getRandom().nextBoolean());
        return spawnDataIn;
    }


    public boolean isHen() {
        return this.dataTracker.get(HEN);
    }

    public void setHen(boolean hen) {
        this.dataTracker.set(HEN, hen);
    }

    public int getTamingLevel() {
        return this.dataTracker.get(TAMING_LEVEL);
    }

    public void setTamingLevel(int level) {
        this.dataTracker.set(TAMING_LEVEL, level);
    }

    public int getCommand() {
        return this.dataTracker.get(COMMAND);
    }

    public void setCommand(int command) {
        this.dataTracker.set(COMMAND, command);
        this.setSitting(command == 1);
    }

    public boolean isStaring() {
        if (this.getWorld().isClient)
            return this.isStaring = this.dataTracker.get(STARING);
        return this.isStaring;
    }

    public void setStaring(boolean staring) {
        this.dataTracker.set(STARING, staring);
        if (!this.getWorld().isClient)
            this.isStaring = staring;
    }

    public void forcePreyToLook(MobEntity mob) {
        mob.getLookControl().lookAt(this.getX(), this.getY() + (double) this.getStandingEyeHeight(), this.getZ(), (float) mob.getMaxHeadRotation(), (float) mob.getMaxLookPitchChange());
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);
        Item itemInHand = stackInHand.getItem();

        if (stackInHand.getItem() == Items.NAME_TAG || itemInHand == Items.LEAD || itemInHand == Items.POISONOUS_POTATO)
            return super.interactMob(player, hand);

        if (this.isTamed() && this.isOwner(player)) {
            if (stackInHand.isIn(IafItemTags.HEAL_COCKATRICE)) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(8);
                    this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
                    stackInHand.decrement(1);
                }
                return ActionResult.SUCCESS;
            } else if (stackInHand.isEmpty()) {
                if (player.isSneaking()) {
                    if (this.hasHomePosition) {
                        this.hasHomePosition = false;
                        player.sendMessage(Text.translatable("cockatrice.command.remove_home"), true);
                    } else {
                        BlockPos pos = this.getBlockPos();
                        this.homePos = new HomePosition(pos, this.getWorld());
                        this.hasHomePosition = true;
                        player.sendMessage(Text.translatable("cockatrice.command.new_home", pos.getX(), pos.getY(), pos.getZ(), this.homePos.getDimension()), true);
                    }
                } else {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() > 3)
                        this.setCommand(0);
                    player.sendMessage(Text.translatable("cockatrice.command." + this.getCommand()), true);
                    this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
                }
                return ActionResult.SUCCESS;
            }

        }
        return ActionResult.FAIL;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        LivingEntity attackTarget = this.getTarget();
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && attackTarget instanceof PlayerEntity)
            this.setTarget(null);
        if (this.isSitting() && this.getCommand() != 1)
            this.setSitting(false);
        if (this.isSitting() && attackTarget != null)
            this.setTarget(null);
        if (attackTarget != null && this.isTeammate(attackTarget))
            this.setTarget(null);
        if (!this.getWorld().isClient)
            if (attackTarget == null || !attackTarget.isAlive())
                this.setTargetedEntity(0);
            else if (this.isStaring() || this.shouldStareAttack(attackTarget))
                this.setTargetedEntity(attackTarget.getId());

        if (this.getAnimation() == ANIMATION_BITE && attackTarget != null && this.getAnimationTick() == 7) {
            double dist = this.squaredDistanceTo(attackTarget);
            if (dist < 8)
                attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue()));
        }
        if (this.getAnimation() == ANIMATION_JUMPAT && attackTarget != null) {
            double dist = this.squaredDistanceTo(attackTarget);
            double d0 = attackTarget.getX() - this.getX();
            double d1 = attackTarget.getZ() - this.getZ();
            float leap = MathHelper.sqrt((float) (d0 * d0 + d1 * d1));
            if (dist < 4 && this.getAnimationTick() > 10) {
                attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue()));
                if ((double) leap >= 1.0E-4D)
                    attackTarget.setVelocity(attackTarget.getVelocity().add(d0 / (double) leap * 0.800000011920929D + this.getVelocity().x * 0.20000000298023224D, 0, d1 / (double) leap * 0.800000011920929D + this.getVelocity().z * 0.20000000298023224D));
            }
        }
        boolean sitting = this.isSitting();
        if (sitting && this.sitProgress < 20.0F)
            this.sitProgress += 0.5F;
        else if (!sitting && this.sitProgress > 0.0F)
            this.sitProgress -= 0.5F;

        boolean staring = this.isStaring();
        if (staring && this.stareProgress < 20.0F)
            this.stareProgress += 0.5F;
        else if (!staring && this.stareProgress > 0.0F)
            this.stareProgress -= 0.5F;
        if (!this.getWorld().isClient) {
            if (staring) this.ticksStaring++;
            else this.ticksStaring = 0;
        }
        if (!this.getWorld().isClient && staring && (attackTarget == null || this.shouldMelee()))
            this.setStaring(false);
        if (attackTarget != null) {
            this.getLookControl().lookAt(attackTarget.getX(), attackTarget.getY() + (double) attackTarget.getStandingEyeHeight(), attackTarget.getZ(), (float) this.getMaxHeadRotation(), (float) this.getMaxLookPitchChange());
            if (!this.shouldMelee() && attackTarget instanceof MobEntity mob)
                this.forcePreyToLook(mob);
        }
        boolean blindness = this.hasStatusEffect(StatusEffects.BLINDNESS) || attackTarget != null && attackTarget.hasStatusEffect(StatusEffects.BLINDNESS);
        if (blindness) this.setStaring(false);
        if (!this.getWorld().isClient && !blindness && attackTarget != null && EntityGorgon.isEntityLookingAt(this, attackTarget, VIEW_RADIUS) && EntityGorgon.isEntityLookingAt(attackTarget, this, VIEW_RADIUS) && !EntityGorgon.isBlindfolded(attackTarget)) {
            if (!this.shouldMelee()) {
                if (!this.isStaring())
                    this.setStaring(true);
                else {
                    int attackStrength = this.getFriendsCount(attackTarget);
                    if (this.getWorld().getDifficulty() == Difficulty.HARD)
                        attackStrength++;
                    attackTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 10, 2 + Math.min(1, attackStrength)));
                    attackTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, Math.min(4, attackStrength)));
                    attackTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                    if (attackStrength >= 2 && attackTarget.age % 40 == 0)
                        attackTarget.damage(this.getWorld().getDamageSources().wither(), attackStrength - 1);
                    attackTarget.setAttacker(this);
                    if (!this.isTamed() && attackTarget instanceof PlayerEntity) {
                        this.setTamingPlayer(attackTarget.getId());
                        this.setTamingLevel(this.getTamingLevel() + 1);
                        if (this.getTamingLevel() % 100 == 0)
                            this.getWorld().sendEntityStatus(this, (byte) 46);
                        if (this.getTamingLevel() >= 1000) {
                            this.getWorld().sendEntityStatus(this, (byte) 45);
                            if (this.getTamingPlayer() instanceof PlayerEntity player)
                                this.setOwner(player);
                            this.setTarget(null);
                            this.setTamingPlayer(0);
                            this.setTargetedEntity(0);
                        }
                    }
                }
            }
        }
        if (!this.getWorld().isClient && attackTarget == null && this.getRandom().nextInt(300) == 0 && this.getAnimation() == NO_ANIMATION)
            this.setAnimation(ANIMATION_WATTLESHAKE);
        if (!this.getWorld().isClient) {
            if (this.shouldMelee() && !this.isMeleeMode)
                this.switchAI(true);
            if (!this.shouldMelee() && this.isMeleeMode)
                this.switchAI(false);
        }

        if (this.getWorld().isClient && this.getTargetedEntity() != null && EntityGorgon.isEntityLookingAt(this, this.getTargetedEntity(), VIEW_RADIUS) && EntityGorgon.isEntityLookingAt(this.getTargetedEntity(), this, VIEW_RADIUS) && this.isStaring()) {
            if (this.hasTargetedEntity()) {
                if (this.clientSideAttackTime < this.getAttackDuration())
                    ++this.clientSideAttackTime;

                LivingEntity livingEntity = this.getTargetedEntity();

                if (livingEntity != null) {
                    this.getLookControl().lookAt(livingEntity, 90.0F, 90.0F);
                    this.getLookControl().tick();
                    double d5 = this.getAttackAnimationScale(0.0F);
                    double d0 = livingEntity.getX() - this.getX();
                    double d1 = livingEntity.getY() + (double) (livingEntity.getHeight() * 0.5F) - (this.getY() + (double) this.getStandingEyeHeight());
                    double d2 = livingEntity.getZ() - this.getZ();
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 = d0 / d3;
                    d1 = d1 / d3;
                    d2 = d2 / d3;
                    double d4 = this.random.nextDouble();

                    while (d4 < d3) {
                        d4 += 1.8D - d5 + this.random.nextDouble() * (1.7D - d5);
                        this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + d0 * d4, this.getY() + d1 * d4 + (double) this.getStandingEyeHeight(), this.getZ() + d2 * d4, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private int getFriendsCount(LivingEntity attackTarget) {
        if (this.getTarget() == null) return 0;
        float dist = IafConfig.getInstance().cockatrice.chickenSearchLength;
        List<EntityCockatrice> list = this.getWorld().getNonSpectatingEntities(EntityCockatrice.class, this.getBoundingBox().stretch(dist, dist, dist));
        int i = 0;
        for (EntityCockatrice cockatrice : list)
            if (!cockatrice.isPartOf(this) && cockatrice.getTarget() != null && cockatrice.getTarget() == this.getTarget()) {
                boolean bothLooking = EntityGorgon.isEntityLookingAt(cockatrice, cockatrice.getTarget(), VIEW_RADIUS) && EntityGorgon.isEntityLookingAt(cockatrice.getTarget(), cockatrice, VIEW_RADIUS);
                if (bothLooking)
                    i++;
            }
        return i;
    }

    public float getAttackAnimationScale(float f) {
        return ((float) this.clientSideAttackTime + f) / (float) this.getAttackDuration();
    }

    public boolean shouldStareAttack(Entity entity) {
        return this.distanceTo(entity) > 5;
    }

    public int getAttackDuration() {
        return 80;
    }

    private boolean shouldMelee() {
        boolean blindness = this.hasStatusEffect(StatusEffects.BLINDNESS) || this.getTarget() != null && this.getTarget().hasStatusEffect(StatusEffects.BLINDNESS);
        if (this.getTarget() != null) {
            if (this.distanceTo(this.getTarget()) < 4D) return true;
            Entity entity = this.getTarget();
            return entity.getType().isIn(IafEntityTags.COCKATRICE_TARGETS) || blindness || !this.canUseStareOn(this.getTarget());
        }
        return false;
    }

    @Override
    public void travel(Vec3d motionVec) {
        if (!this.canMove() && !this.hasPassengers())
            motionVec = motionVec.multiply(0, 1, 0);
        super.travel(motionVec);
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == this.NO_ANIMATION)
            this.setAnimation(ANIMATION_SPEAK);
        super.playAmbientSound();
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION)
            this.setAnimation(ANIMATION_SPEAK);
        super.playHurtSound(source);
    }

    @Override
    public PassiveEntity createChild(ServerWorld serverWorld, PassiveEntity ageable) {
        return null;
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
        return new Animation[]{NO_ANIMATION, ANIMATION_JUMPAT, ANIMATION_WATTLESHAKE, ANIMATION_BITE, ANIMATION_SPEAK, ANIMATION_EAT};
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public boolean isTargetBlocked(Vec3d target) {
        Vec3d Vector3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
        return this.getWorld().raycast(new RaycastContext(Vector3d, target, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return IafSounds.COCKATRICE_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSounds.COCKATRICE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return IafSounds.COCKATRICE_DIE;
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 45) this.playEffect(true);
        else if (id == 46) this.playEffect(false);
        else super.handleStatus(id);
    }

    protected void playEffect(boolean play) {
        ParticleEffect enumparticletypes = ParticleTypes.HEART;

        if (!play) enumparticletypes = ParticleTypes.DAMAGE_INDICATOR;

        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.getWorld().addParticle(enumparticletypes, this.getX() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
        }
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
    public EntityView method_48926() {
        return this.getWorld();
    }
}
