package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.IAnimatedEntity;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.api.IafEvents;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonType;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.entity.util.dragon.IafDragonAttacks;
import com.iafenvoy.iceandfire.entity.util.dragon.IafDragonDestructionManager;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.IafSounds;
import com.iafenvoy.iceandfire.registry.tag.IafEntityTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Random;

public class EntityLightningDragon extends EntityDragonBase {
    public static final Identifier FEMALE_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/dragon/lightning_dragon_female");
    public static final Identifier MALE_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/dragon/lightning_dragon_male");
    public static final Identifier SKELETON_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/dragon/lightning_dragon_skeleton");
    private static final TrackedData<Boolean> HAS_LIGHTNING_TARGET = DataTracker.registerData(EntityLightningDragon.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> LIGHTNING_TARGET_X = DataTracker.registerData(EntityLightningDragon.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> LIGHTNING_TARGET_Y = DataTracker.registerData(EntityLightningDragon.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> LIGHTNING_TARGET_Z = DataTracker.registerData(EntityLightningDragon.class, TrackedDataHandlerRegistry.FLOAT);

    public EntityLightningDragon(World worldIn) {
        this(IafEntities.LIGHTNING_DRAGON, worldIn);
    }

    public EntityLightningDragon(EntityType<?> t, World worldIn) {
        super(t, worldIn, DragonType.LIGHTNING, 1, 1 + IafConfig.getInstance().dragon.behaviour.attackDamage, IafConfig.getInstance().dragon.maxHealth * 0.04, IafConfig.getInstance().dragon.maxHealth, 0.15F, 0.4F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        ANIMATION_SPEAK = Animation.create(20);
        ANIMATION_BITE = Animation.create(35);
        ANIMATION_SHAKEPREY = Animation.create(65);
        ANIMATION_TAILWHACK = Animation.create(40);
        ANIMATION_FIRECHARGE = Animation.create(30);
        ANIMATION_WINGBLAST = Animation.create(50);
        ANIMATION_ROAR = Animation.create(40);
        ANIMATION_EPIC_ROAR = Animation.create(60);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HAS_LIGHTNING_TARGET, false);
        this.dataTracker.startTracking(LIGHTNING_TARGET_X, 0.0F);
        this.dataTracker.startTracking(LIGHTNING_TARGET_Y, 0.0F);
        this.dataTracker.startTracking(LIGHTNING_TARGET_Z, 0.0F);
    }

    @Override
    public int getStartMetaForType() {
        return 8;
    }

    @Override
    protected boolean shouldTarget(Entity entity) {
        if (entity instanceof EntityDragonBase && !this.isTamed())
            return entity.getType() != this.getType() && this.getWidth() >= entity.getWidth() && !((EntityDragonBase) entity).isMobDead();
        return entity instanceof PlayerEntity || DragonUtils.isDragonTargetable(entity, IafEntityTags.LIGHTNING_DRAGON_TARGETS) || !this.isTamed() && DragonUtils.isVillager(entity);
    }

    @Override
    public boolean isTimeToWake() {
        return !this.getWorld().isDay() || this.getCommand() == 2;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
    }

    @Override
    public String getVariantName(int variant) {
        return switch (variant) {
            default -> "electric_";
            case 1 -> "amethyst_";
            case 2 -> "copper_";
            case 3 -> "black_";
        };
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        if (i.getName().equals(this.getWorld().getDamageSources().lightningBolt().getName())) {
            this.heal(15F);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20, 1));
            return true;
        }
        return super.isInvulnerableTo(i);
    }

    @Override
    public Item getVariantScale(int variant) {
        return switch (variant) {
            default -> IafItems.DRAGONSCALES_ELECTRIC;
            case 1 -> IafItems.DRAGONSCALES_amethyst;
            case 2 -> IafItems.DRAGONSCALES_COPPER;
            case 3 -> IafItems.DRAGONSCALES_BLACK;
        };
    }

    @Override
    public Item getVariantEgg(int variant) {
        return switch (variant) {
            default -> IafItems.DRAGONEGG_ELECTRIC;
            case 1 -> IafItems.DRAGONEGG_amethyst;
            case 2 -> IafItems.DRAGONEGG_COPPER;
            case 3 -> IafItems.DRAGONEGG_BLACK;
        };
    }

    public void setHasLightningTarget(boolean lightning_target) {
        this.dataTracker.set(HAS_LIGHTNING_TARGET, lightning_target);
    }

    public boolean hasLightningTarget() {
        return this.dataTracker.get(HAS_LIGHTNING_TARGET);
    }

    public void setLightningTargetVec(float x, float y, float z) {
        this.dataTracker.set(LIGHTNING_TARGET_X, x);
        this.dataTracker.set(LIGHTNING_TARGET_Y, y);
        this.dataTracker.set(LIGHTNING_TARGET_Z, z);
    }

    public float getLightningTargetX() {
        return this.dataTracker.get(LIGHTNING_TARGET_X);
    }

    public float getLightningTargetY() {
        return this.dataTracker.get(LIGHTNING_TARGET_Y);
    }

    public float getLightningTargetZ() {
        return this.dataTracker.get(LIGHTNING_TARGET_Z);
    }

    @Override
    public Item getSummoningCrystal() {
        return IafItems.SUMMONING_CRYSTAL_LIGHTNING;
    }

/*    @Override
    public boolean canBeControlledByRider() {
        return true;
    }*/

    @Override
    public boolean tryAttack(Entity entityIn) {
        this.getLookControl().lookAt(entityIn, 30.0F, 30.0F);
        if (!this.isPlayingAttackAnimation()) {
            switch (this.groundAttack) {
                case BITE -> this.setAnimation(ANIMATION_BITE);
                case TAIL_WHIP -> this.setAnimation(ANIMATION_TAILWHACK);
                case SHAKE_PREY -> {
                    boolean flag = false;
                    if (new Random().nextInt(2) == 0 && this.isDirectPathBetweenPoints(this, this.getPos().add(0, this.getHeight() / 2, 0), entityIn.getPos().add(0, entityIn.getHeight() / 2, 0)) &&
                            entityIn.getWidth() < this.getWidth() * 0.5F && this.getControllingPassenger() == null && this.getDragonStage() > 1 && !(entityIn instanceof EntityDragonBase) && !DragonUtils.isAnimaniaMob(entityIn)) {
                        this.setAnimation(ANIMATION_SHAKEPREY);
                        flag = true;
                        entityIn.startRiding(this);
                    }
                    if (!flag) {
                        this.groundAttack = IafDragonAttacks.Ground.BITE;
                        this.setAnimation(ANIMATION_BITE);
                    }
                }
                case WING_BLAST -> this.setAnimation(ANIMATION_WINGBLAST);
            }
        }
        return false;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        LivingEntity attackTarget = this.getTarget();
        if (!this.getWorld().isClient && attackTarget != null) {
            if (this.getBoundingBox().expand(2.5F + this.getRenderSize() * 0.33F, 2.5F + this.getRenderSize() * 0.33F, 2.5F + this.getRenderSize() * 0.33F).intersects(attackTarget.getBoundingBox()))
                this.tryAttack(attackTarget);
            if (this.groundAttack == IafDragonAttacks.Ground.FIRE && (this.usingGroundAttack || this.isOnGround()))
                this.shootFireAtMob(attackTarget);
            if (this.airAttack == IafDragonAttacks.Air.TACKLE && !this.usingGroundAttack && this.squaredDistanceTo(attackTarget) < 100) {
                double difX = attackTarget.getX() - this.getX();
                double difY = attackTarget.getY() + attackTarget.getHeight() - this.getY();
                double difZ = attackTarget.getZ() - this.getZ();
                this.setVelocity(this.getVelocity().add(difX * 0.1D, difY * 0.1D, difZ * 0.1D));
                if (this.getBoundingBox().expand(1 + this.getRenderSize() * 0.5F, 1 + this.getRenderSize() * 0.5F, 1 + this.getRenderSize() * 0.5F).intersects(attackTarget.getBoundingBox())) {
                    this.tryAttack(attackTarget);
                    this.usingGroundAttack = true;
                    this.randomizeAttacks();
                    this.setFlying(false);
                    this.setHovering(false);
                }
            }
        }
        if (!this.isBreathingFire())
            this.setHasLightningTarget(false);
    }


    @Override
    protected void breathFireAtPos(BlockPos burningTarget) {
        if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                this.setYaw(this.bodyYaw);
                if (this.fireBreathTicks % 7 == 0)
                    this.playSound(IafSounds.LIGHTNINGDRAGON_BREATH, 4, 1);
                this.stimulateFire(burningTarget.getX() + 0.5F, burningTarget.getY() + 0.5F, burningTarget.getZ() + 0.5F, 1);
            }
        } else
            this.setBreathingFire(true);
    }

    @Override
    public void riderShootFire(Entity controller) {
        if (this.getRandom().nextInt(5) == 0 && !this.isBaby()) {
            if (this.getAnimation() != ANIMATION_FIRECHARGE)
                this.setAnimation(ANIMATION_FIRECHARGE);
            else if (this.getAnimationTick() == 20) {
                this.setYaw(this.bodyYaw);
                Vec3d headVec = this.getHeadPosition();
                this.playSound(IafSounds.LIGHTNINGDRAGON_BREATH_CRACKLE, 4, 1);
                double d2 = controller.getRotationVector().x;
                double d3 = controller.getRotationVector().y;
                double d4 = controller.getRotationVector().z;
                float inaccuracy = 1.0F;
                d2 = d2 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                d3 = d3 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                d4 = d4 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                EntityDragonLightningCharge entitylargefireball = new EntityDragonLightningCharge(IafEntities.LIGHTNING_DRAGON_CHARGE, this.getWorld(), this, d2, d3, d4);
                entitylargefireball.setPosition(headVec.x, headVec.y, headVec.z);
                if (!this.getWorld().isClient)
                    this.getWorld().spawnEntity(entitylargefireball);
            }
        } else {
            if (this.isBreathingFire()) {
                if (this.isActuallyBreathingFire()) {
                    this.setYaw(this.bodyYaw);
                    if (this.fireBreathTicks % 7 == 0)
                        this.playSound(IafSounds.LIGHTNINGDRAGON_BREATH, 4, 1);
                    HitResult mop = this.rayTraceRider(controller, 10 * this.getDragonStage(), 1.0F);
                    if (mop != null)
                        this.stimulateFire(mop.getPos().x, mop.getPos().y, mop.getPos().z, 1);
                }
            } else
                this.setBreathingFire(true);
        }
    }

    @Override
    public Item getBloodItem() {
        return IafItems.LIGHTNING_DRAGON_BLOOD;
    }

    @Override
    public Item getFleshItem() {
        return IafItems.LIGHTNING_DRAGON_FLESH;
    }

    @Override
    public ItemConvertible getHeartItem() {
        return IafItems.LIGHTNING_DRAGON_HEART;
    }

    @Override
    public Identifier getDeadLootTable() {
        if (this.getDeathStage() >= (this.getAgeInDays() / 5) / 2)
            return SKELETON_LOOT;
        else
            return this.isMale() ? MALE_LOOT : FEMALE_LOOT;
    }

    private void shootFireAtMob(LivingEntity entity) {
        if (this.usingGroundAttack && this.groundAttack == IafDragonAttacks.Ground.FIRE || !this.usingGroundAttack && (this.airAttack == IafDragonAttacks.Air.SCORCH_STREAM || this.airAttack == IafDragonAttacks.Air.HOVER_BLAST)) {
            if (this.usingGroundAttack && this.getRandom().nextInt(5) == 0 || !this.usingGroundAttack && this.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                if (this.getAnimation() != ANIMATION_FIRECHARGE)
                    this.setAnimation(ANIMATION_FIRECHARGE);
                else if (this.getAnimationTick() == 20) {
                    this.setYaw(this.bodyYaw);
                    Vec3d headVec = this.getHeadPosition();
                    double d2 = entity.getX() - headVec.x;
                    double d3 = entity.getY() - headVec.y;
                    double d4 = entity.getZ() - headVec.z;
                    float inaccuracy = 1.0F;
                    d2 = d2 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    d3 = d3 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    d4 = d4 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    this.playSound(IafSounds.LIGHTNINGDRAGON_BREATH, 4, 1);
                    EntityDragonLightningCharge entitylargefireball = new EntityDragonLightningCharge(IafEntities.LIGHTNING_DRAGON_CHARGE, this.getWorld(), this, d2, d3, d4);
                    entitylargefireball.setPosition(headVec.x, headVec.y, headVec.z);
                    if (!this.getWorld().isClient) this.getWorld().spawnEntity(entitylargefireball);
                    if (!entity.isAlive()) this.setBreathingFire(false);
                    this.randomizeAttacks();
                }
            } else {
                if (this.isBreathingFire()) {
                    if (this.isActuallyBreathingFire()) {
                        this.setYaw(this.bodyYaw);
                        if (this.age % 5 == 0)
                            this.playSound(IafSounds.LIGHTNINGDRAGON_BREATH, 4, 1);
                        this.stimulateFire(entity.getX(), entity.getY(), entity.getZ(), 1);
                        if (!entity.isAlive()) {
                            this.setBreathingFire(false);
                            this.randomizeAttacks();
                        }
                    }
                } else
                    this.setBreathingFire(true);
            }
        }
        this.lookAtEntity(entity, 360, 360);
    }

    @Override
    public void stimulateFire(double burnX, double burnY, double burnZ, int syncType) {
        if (IafEvents.ON_DRAGON_FIRE_BLOCK.invoker().onFireBlock(this, burnX, burnY, burnZ)) return;
        if (syncType > 2 && syncType < 6) {
            if (this.getAnimation() != ANIMATION_FIRECHARGE)
                this.setAnimation(ANIMATION_FIRECHARGE);
            else if (this.getAnimationTick() == 20) {
                this.setYaw(this.bodyYaw);
                Vec3d headVec = this.getHeadPosition();
                double d2 = burnX - headVec.x;
                double d3 = burnY - headVec.y;
                double d4 = burnZ - headVec.z;
                float inaccuracy = 1.0F;
                d2 = d2 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                d3 = d3 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                d4 = d4 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                this.playSound(IafSounds.LIGHTNINGDRAGON_BREATH_CRACKLE, 4, 1);
                EntityDragonLightningCharge entitylargefireball = new EntityDragonLightningCharge(
                        IafEntities.LIGHTNING_DRAGON_CHARGE, this.getWorld(), this, d2, d3, d4);
                entitylargefireball.setPosition(headVec.x, headVec.y, headVec.z);
                if (!this.getWorld().isClient)
                    this.getWorld().spawnEntity(entitylargefireball);
            }
            return;
        }
        this.burnParticleX = burnX;
        this.burnParticleY = burnY;
        this.burnParticleZ = burnZ;
        Vec3d headPos = this.getHeadPosition();
        double d2 = burnX - headPos.x;
        double d3 = burnY - headPos.y;
        double d4 = burnZ - headPos.z;
        double distance = Math.max(2.5F * Math.sqrt(this.squaredDistanceTo(burnX, burnY, burnZ)), 0);
        double conqueredDistance = this.burnProgress / 40D * distance;
        int increment = (int) Math.ceil(conqueredDistance / 100);
        for (int i = 0; i < conqueredDistance; i += increment) {
            double progressX = headPos.x + d2 * (i / (float) distance);
            double progressY = headPos.y + d3 * (i / (float) distance);
            double progressZ = headPos.z + d4 * (i / (float) distance);
            if (this.canPositionBeSeen(progressX, progressY, progressZ)) {
                this.setHasLightningTarget(true);
                this.setLightningTargetVec((float) burnX, (float) burnY, (float) burnZ);
            } else {
                if (!this.getWorld().isClient) {
                    HitResult result = this.getWorld().raycast(new RaycastContext(
                            new Vec3d(this.getX(), this.getY() + this.getStandingEyeHeight(), this.getZ()),
                            new Vec3d(progressX, progressY, progressZ), RaycastContext.ShapeType.COLLIDER,
                            RaycastContext.FluidHandling.NONE, this));
                    Vec3d vec3 = result.getPos();
                    BlockPos pos = BlockPos.ofFloored(vec3);
                    IafDragonDestructionManager.destroyAreaBreath(this.getWorld(), pos, this);
                    this.setHasLightningTarget(true);
                    this.setLightningTargetVec((float) result.getPos().x, (float) result.getPos().y, (float) result.getPos().z);
                }
            }
        }
        if (this.burnProgress >= 40D && this.canPositionBeSeen(burnX, burnY, burnZ)) {
            double spawnX = burnX + (this.random.nextFloat() * 3.0) - 1.5;
            double spawnY = burnY + (this.random.nextFloat() * 3.0) - 1.5;
            double spawnZ = burnZ + (this.random.nextFloat() * 3.0) - 1.5;
            this.setHasLightningTarget(true);
            this.setLightningTargetVec((float) spawnX, (float) spawnY, (float) spawnZ);
            if (!this.getWorld().isClient)
                IafDragonDestructionManager.destroyAreaBreath(this.getWorld(), BlockPos.ofFloored(spawnX, spawnY, spawnZ), this);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTeen() ? IafSounds.LIGHTNINGDRAGON_TEEN_IDLE : this.shouldDropLoot() ? IafSounds.LIGHTNINGDRAGON_ADULT_IDLE : IafSounds.LIGHTNINGDRAGON_CHILD_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isTeen() ? IafSounds.LIGHTNINGDRAGON_TEEN_HURT : this.shouldDropLoot() ? IafSounds.LIGHTNINGDRAGON_ADULT_HURT : IafSounds.LIGHTNINGDRAGON_CHILD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTeen() ? IafSounds.LIGHTNINGDRAGON_TEEN_DEATH : this.shouldDropLoot() ? IafSounds.LIGHTNINGDRAGON_ADULT_DEATH : IafSounds.LIGHTNINGDRAGON_CHILD_DEATH;
    }

    @Override
    public SoundEvent getRoarSound() {
        return this.isTeen() ? IafSounds.LIGHTNINGDRAGON_TEEN_ROAR : this.shouldDropLoot() ? IafSounds.LIGHTNINGDRAGON_ADULT_ROAR : IafSounds.LIGHTNINGDRAGON_CHILD_ROAR;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, ANIMATION_EAT, ANIMATION_SPEAK, ANIMATION_BITE, ANIMATION_SHAKEPREY, ANIMATION_TAILWHACK, ANIMATION_FIRECHARGE, ANIMATION_WINGBLAST, ANIMATION_ROAR, ANIMATION_EPIC_ROAR};
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItems.LIGHTNING_STEW;
    }

    @Override
    protected void spawnDeathParticles() {
        for (int k = 0; k < 3; ++k) {
            double d2 = this.random.nextGaussian() * 0.02D;
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            if (this.getWorld().isClient) {
                this.getWorld().addParticle(ParticleTypes.RAIN,
                        this.getX() + this.random.nextFloat() * this.getWidth() * 2.0F - this.getWidth(),
                        this.getY() + this.random.nextFloat() * this.getHeight(),
                        this.getZ() + this.random.nextFloat() * this.getWidth() * 2.0F - this.getWidth(), d2, d0, d1);
            }
        }
    }

    @Override
    public void spawnBabyParticles() {
        for (int i = 0; i < 5; i++) {
            float radiusAdd = i * 0.15F;
            float headPosX = (float) (this.getX() + 1.8F * this.getRenderSize() * (0.3F + radiusAdd) * MathHelper.cos((float) ((this.getYaw() + 90) * Math.PI / 180)));
            float headPosZ = (float) (this.getY() + 1.8F * this.getRenderSize() * (0.3F + radiusAdd) * MathHelper.sin((float) ((this.getYaw() + 90) * Math.PI / 180)));
            float headPosY = (float) (this.getZ() + 0.5 * this.getRenderSize() * 0.3F);
            this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, headPosX, headPosY, headPosZ, 0, 0, 0);
        }
    }

    @Override
    public ItemStack getSkull() {
        return new ItemStack(IafItems.DRAGON_SKULL_LIGHTNING);
    }

    /* FIXME :: Check -> why is this the only dragon overriding this?
    @Override
    public Vec3 getHeadPosition() {
        //this.setDragonPitch(this.ticksExisted % 180 - 90);
        float sitProg = this.sitProgress * 0.005F;
        float deadProg = this.modelDeadProgress * -0.02F;
        float hoverProg = this.hoverProgress * 0.03F;
        float flyProg = Math.max(0, this.flyProgress * 0.01F);
        int tick = 0;
        if (this.getAnimationTick() < 10) {
            tick = this.getAnimationTick();
        } else if (this.getAnimationTick() > 50) {
            tick = 60 - this.getAnimationTick();
        } else {
            tick = 10;
        }
        float epicRoarProg = this.getAnimation() == ANIMATION_EPIC_ROAR && !this.isOrderedToSit() ? tick * 0.1F : 0;
        float sleepProg = this.sleepProgress * 0.025F;
        float pitchY = 0;
        float pitchAdjustment = 0;
        float pitchMinus = 0;
        float dragonPitch = -getDragonPitch();// -90 = down, 0 = straight, 90 = up
        if (this.isFlying() || this.isHovering()) {
            if (dragonPitch > 0) {
                pitchY = (dragonPitch / 90F) * 1.2F;
            } else {
                pitchY = (dragonPitch / 90F) * 3F;
            }
        }
        float flightXz = 1.0F + flyProg + hoverProg;
        float absPitch = Math.abs(dragonPitch) / 90F;//1 down/up, 0 straight
        float minXZ = dragonPitch > 20 ? (dragonPitch - 20) * 0.009F : 0;
        float xzMod = (0.58F - hoverProg * 0.45F + flyProg * 0.2F + absPitch * 0.3F - sitProg) * flightXz * getRenderSize();
        float xzModSine = xzMod * (Math.max(0.25F, Mth.cos((float) Math.toRadians(dragonPitch))) - minXZ);
        float headPosX = (float) (getX() + (xzModSine) * Mth.cos((float) ((yBodyRot + 90) * Math.PI / 180)));
        float headPosY = (float) (getY() + (0.7F + (sitProg * 5F) + hoverProg + deadProg + epicRoarProg + sleepProg + flyProg + pitchY) * getRenderSize() * 0.3F);
        float headPosZ = (float) (getZ() + (xzModSine) * Mth.sin((float) ((yBodyRot + 90) * Math.PI / 180)));
        return new Vec3(headPosX, headPosY, headPosZ);
    }
    */
}
