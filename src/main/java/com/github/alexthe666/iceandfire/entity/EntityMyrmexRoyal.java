package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class EntityMyrmexRoyal extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final Identifier DESERT_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_royal_desert");
    public static final Identifier JUNGLE_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_royal_jungle");
    private static final Identifier TEXTURE_DESERT = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_desert_royal.png");
    private static final Identifier TEXTURE_JUNGLE = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_jungle_royal.png");
    private static final TrackedData<Boolean> FLYING = DataTracker.registerData(EntityMyrmexRoyal.class, TrackedDataHandlerRegistry.BOOLEAN);
    public int releaseTicks = 0;
    public int daylightTicks = 0;
    public float flyProgress;
    public EntityMyrmexRoyal mate;
    private int hiveTicks = 0;
    private int breedingTicks = 0;
    private boolean isFlying;
    private boolean isLandNavigator;
    private boolean isMating = false;

    public EntityMyrmexRoyal(EntityType<EntityMyrmexRoyal> t, World worldIn) {
        super(t, worldIn);
        this.switchNavigator(true);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, World world, double x, double z, Random rand) {
        BlockPos pos = BlockPos.ofFloored(x, entity.getBlockY(), z);
        for (int yDown = 0; yDown < 10; yDown++) {
            if (!world.isAir(pos.down(yDown))) {
                return pos.up(yDown);
            }
        }
        return pos;
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 2D)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 9.0D);
    }

    @Override
    protected TradeOffers.Factory[] getLevel1Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_ROYAL.get(1) : MyrmexTrades.DESERT_ROYAL.get(1);
    }

    @Override
    protected TradeOffers.Factory[] getLevel2Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_ROYAL.get(2) : MyrmexTrades.DESERT_ROYAL.get(2);
    }

    @Override
    protected Identifier getLootTableId() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public int getXpToDrop() {
        return 10;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FLYING, Boolean.FALSE);
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = this.createNavigator(this.getWorld(), AdvancedPathNavigate.MovementType.CLIMBING);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new FlyMoveHelper(this);
            this.navigation = this.createNavigator(this.getWorld(), AdvancedPathNavigate.MovementType.FLYING);
            this.isLandNavigator = false;
        }
    }

    public boolean isFlying() {
        if (this.getWorld().isClient) {
            return this.isFlying = this.dataTracker.get(FLYING);
        }
        return this.isFlying;
    }

    public void setFlying(boolean flying) {
        this.dataTracker.set(FLYING, flying);
        if (!this.getWorld().isClient) {
            this.isFlying = flying;
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("HiveTicks", this.hiveTicks);
        tag.putInt("ReleaseTicks", this.releaseTicks);
        tag.putBoolean("Flying", this.isFlying());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.hiveTicks = tag.getInt("HiveTicks");
        this.releaseTicks = tag.getInt("ReleaseTicks");
        this.setFlying(tag.getBoolean("Flying"));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        boolean flying = this.isFlying() && !this.isOnGround();
        LivingEntity attackTarget = this.getTarget();
        if (flying && this.flyProgress < 20.0F) {
            this.flyProgress += 1F;
        } else if (!flying && this.flyProgress > 0.0F) {
            this.flyProgress -= 1F;
        }
        if (flying) {
            double up = this.isTouchingWater() ? 0.16D : 0.08D;
            this.setVelocity(this.getVelocity().add(0, up, 0));
        }
        if (flying && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!flying && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (this.canSeeSky()) {
            this.daylightTicks++;
        } else {
            this.daylightTicks = 0;
        }
        if (flying && this.canSeeSky() && this.isBreedingSeason()) {
            this.releaseTicks++;
        }
        if (!flying && this.canSeeSky() && this.daylightTicks > 300 && this.isBreedingSeason() && attackTarget == null && this.canMove() && this.isOnGround() && !this.isMating) {
            this.setFlying(true);
            this.setVelocity(this.getVelocity().add(0, 0.42D, 0));
        }
        if (this.getGrowthStage() >= 2) {
            this.hiveTicks++;
        }
        if (this.getAnimation() == ANIMATION_BITE && attackTarget != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(attackTarget.getBoundingBox())) {
                attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && attackTarget != null && this.getAnimationTick() == 6) {
            this.playStingSound();
            if (this.getAttackBounds().intersects(attackTarget.getBoundingBox())) {
                attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() * 2));
                attackTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 70, 1));
            }
        }
        if (this.mate != null) {
            this.getWorld().sendEntityStatus(this, (byte) 77);
            if (this.distanceTo(this.mate) < 10) {
                this.setFlying(false);
                this.mate.setFlying(false);
                this.isMating = true;
                if (this.isOnGround() && this.mate.isOnGround()) {
                    this.breedingTicks++;
                    if (this.breedingTicks > 100) {
                        if (this.isAlive()) {
                            this.mate.remove(RemovalReason.KILLED);
                            this.remove(RemovalReason.KILLED);
                            EntityMyrmexQueen queen = new EntityMyrmexQueen(IafEntityRegistry.MYRMEX_QUEEN.get(),
                                    this.getWorld());
                            queen.copyPositionAndRotation(this);
                            queen.setJungleVariant(this.isJungle());
                            queen.setMadeHome(false);
                            if (!this.getWorld().isClient) {
                                this.getWorld().spawnEntity(queen);
                            }
                        }
                        this.isMating = false;
                    }
                }
            }
            this.mate.mate = this;
            if (!this.mate.isAlive()) {
                this.mate.mate = null;
                this.mate = null;
            }
        }
    }

    protected double attackDistance() {
        return 8;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new MyrmexAITradePlayer(this));
        this.goalSelector.add(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.add(0, new MyrmexAIMoveToMate(this, 1.0D));
        this.goalSelector.add(1, new AIFlyAtTarget());
        this.goalSelector.add(2, new AIFlyRandom());
        this.goalSelector.add(3, new MyrmexAIAttackMelee(this, 1.0D, true));
        this.goalSelector.add(4, new MyrmexAILeaveHive(this, 1.0D));
        this.goalSelector.add(4, new MyrmexAIReEnterHive(this, 1.0D));
        this.goalSelector.add(5, new MyrmexAIMoveThroughHive(this, 1.0D));
        this.goalSelector.add(5, new MyrmexAIWanderHiveCenter(this, 1.0D));
        this.goalSelector.add(6, new MyrmexAIWander(this, 1D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new MyrmexAIDefendHive(this));
        this.targetSelector.add(2, new MyrmexAIFindMate<>(this));
        this.targetSelector.add(3, new RevengeGoal(this));
        this.targetSelector.add(4, new MyrmexAIAttackPlayers(this));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, true, (Predicate<LivingEntity>) entity -> {
            if (entity instanceof EntityMyrmexBase && EntityMyrmexRoyal.this.isBreedingSeason() || entity instanceof EntityMyrmexRoyal) {
                return false;
            }
            return entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexRoyal.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Monster);
        }));

    }

    @Override
    public boolean canBreedWith(AnimalEntity otherAnimal) {
        if (otherAnimal == this || otherAnimal == null) {
            return false;
        } else if (otherAnimal.getClass() != this.getClass()) {
            return false;
        } else {
            if (otherAnimal instanceof EntityMyrmexBase) {
                if (((EntityMyrmexBase) otherAnimal).getHive() != null && this.getHive() != null) {
                    return !this.getHive().equals(((EntityMyrmexBase) otherAnimal).getHive());
                } else {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean shouldMoveThroughHive() {
        return false;
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(IafConfig.myrmexBaseAttackStrength * 2D);
    }

    @Override
    public Identifier getAdultTexture() {
        return this.isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 1.25F;
    }

    @Override
    public int getCasteImportance() {
        return 2;
    }

    @Override
    public boolean shouldLeaveHive() {
        return this.isBreedingSeason();
    }

    @Override
    public boolean shouldEnterHive() {
        return !this.isBreedingSeason();
    }

    @Override
    public boolean tryAttack(Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
            return true;
        }
        return false;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING};
    }

    public boolean isBreedingSeason() {
        return this.getGrowthStage() >= 2 && (this.getHive() == null || this.getHive().reproduces);
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 76) {
            this.playEffect(20);
        } else if (id == 77) {
            this.playEffect(7);
        } else {
            super.handleStatus(id);
        }
    }

    protected void playEffect(int hearts) {
        for (int i = 0; i < hearts; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.getWorld().addParticle(ParticleTypes.HEART, this.getX() + this.random.nextFloat() * this.getWidth() * 2.0F - this.getWidth(), this.getY() + 0.5D + this.random.nextFloat() * this.getHeight(), this.getZ() + this.random.nextFloat() * this.getWidth() * 2.0F - this.getWidth(), d0, d1, d2);
        }
    }

    @Override
    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {

    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
        Vec3d vector3d = Vec3d.ofCenter(posVec31);
        Vec3d vector3d1 = Vec3d.ofCenter(posVec32);
        return this.getWorld().raycast(new RaycastContext(vector3d, vector3d1, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }

    class FlyMoveHelper extends MoveControl {
        public FlyMoveHelper(EntityMyrmexRoyal pixie) {
            super(pixie);
            this.speed = 1.75F;
        }

        @Override
        public void tick() {
            if (this.state == State.MOVE_TO) {
                if (EntityMyrmexRoyal.this.horizontalCollision) {
                    EntityMyrmexRoyal.this.setYaw(EntityMyrmexRoyal.this.getYaw() + 180.0F);
                    this.speed = 0.1F;
                    BlockPos target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.getWorld(), EntityMyrmexRoyal.this.getX() + EntityMyrmexRoyal.this.random.nextInt(15) - 7, EntityMyrmexRoyal.this.getZ() + EntityMyrmexRoyal.this.random.nextInt(15) - 7, EntityMyrmexRoyal.this.random);
                    this.targetX = target.getX();
                    this.targetY = target.getY();
                    this.targetZ = target.getZ();
                }
                double d0 = this.targetX - EntityMyrmexRoyal.this.getX();
                double d1 = this.targetY - EntityMyrmexRoyal.this.getY();
                double d2 = this.targetZ - EntityMyrmexRoyal.this.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = Math.sqrt(d3);

                if (d3 < EntityMyrmexRoyal.this.getBoundingBox().getAverageSideLength()) {
                    this.state = State.WAIT;
                    EntityMyrmexRoyal.this.setVelocity(EntityMyrmexRoyal.this.getVelocity().multiply(0.5D, 0.5D, 0.5D));
                } else {
                    EntityMyrmexRoyal.this.setVelocity(EntityMyrmexRoyal.this.getVelocity().add(d0 / d3 * 0.1D * this.speed, d1 / d3 * 0.1D * this.speed, d2 / d3 * 0.1D * this.speed));

                    if (EntityMyrmexRoyal.this.getTarget() == null) {
                        EntityMyrmexRoyal.this.setYaw(-((float) MathHelper.atan2(EntityMyrmexRoyal.this.getVelocity().x, EntityMyrmexRoyal.this.getVelocity().z)) * (180F / (float) Math.PI));
                        EntityMyrmexRoyal.this.bodyYaw = EntityMyrmexRoyal.this.getYaw();
                    } else {
                        double d4 = EntityMyrmexRoyal.this.getTarget().getX() - EntityMyrmexRoyal.this.getX();
                        double d5 = EntityMyrmexRoyal.this.getTarget().getZ() - EntityMyrmexRoyal.this.getZ();
                        EntityMyrmexRoyal.this.setYaw(-((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI));
                        EntityMyrmexRoyal.this.bodyYaw = EntityMyrmexRoyal.this.getYaw();
                    }
                }
            }
        }
    }

    class AIFlyRandom extends Goal {
        BlockPos target;

        public AIFlyRandom() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (EntityMyrmexRoyal.this.isFlying() && EntityMyrmexRoyal.this.getTarget() == null) {
                if (EntityMyrmexRoyal.this instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) EntityMyrmexRoyal.this).getSummoner() != null) {
                    Entity summon = ((EntityMyrmexSwarmer) EntityMyrmexRoyal.this).getSummoner();
                    this.target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.getWorld(), summon.getX() + EntityMyrmexRoyal.this.random.nextInt(10) - 5, summon.getZ() + EntityMyrmexRoyal.this.random.nextInt(10) - 5, EntityMyrmexRoyal.this.random);
                } else {
                    this.target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.getWorld(), EntityMyrmexRoyal.this.getX() + EntityMyrmexRoyal.this.random.nextInt(30) - 15, EntityMyrmexRoyal.this.getZ() + EntityMyrmexRoyal.this.random.nextInt(30) - 15, EntityMyrmexRoyal.this.random);
                }
                return EntityMyrmexRoyal.this.isDirectPathBetweenPoints(EntityMyrmexRoyal.this.getBlockPos(), this.target) && !EntityMyrmexRoyal.this.getMoveControl().isMoving() && EntityMyrmexRoyal.this.random.nextInt(2) == 0;
            } else {
                return false;
            }
        }


        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void tick() {
            if (!EntityMyrmexRoyal.this.isDirectPathBetweenPoints(EntityMyrmexRoyal.this.getBlockPos(), this.target)) {
                if (EntityMyrmexRoyal.this instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) EntityMyrmexRoyal.this).getSummoner() != null) {
                    Entity summon = ((EntityMyrmexSwarmer) EntityMyrmexRoyal.this).getSummoner();
                    this.target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.getWorld(), summon.getX() + EntityMyrmexRoyal.this.random.nextInt(10) - 5, summon.getZ() + EntityMyrmexRoyal.this.random.nextInt(10) - 5, EntityMyrmexRoyal.this.random);
                } else {
                    this.target = EntityMyrmexRoyal.getPositionRelativetoGround(EntityMyrmexRoyal.this, EntityMyrmexRoyal.this.getWorld(), EntityMyrmexRoyal.this.getX() + EntityMyrmexRoyal.this.random.nextInt(30) - 15, EntityMyrmexRoyal.this.getZ() + EntityMyrmexRoyal.this.random.nextInt(30) - 15, EntityMyrmexRoyal.this.random);
                }
            }
            if (EntityMyrmexRoyal.this.getWorld().isAir(this.target)) {
                EntityMyrmexRoyal.this.moveControl.moveTo(this.target.getX() + 0.5D, this.target.getY() + 0.5D, this.target.getZ() + 0.5D, 0.25D);
                if (EntityMyrmexRoyal.this.getTarget() == null) {
                    EntityMyrmexRoyal.this.getLookControl().lookAt(this.target.getX() + 0.5D, this.target.getY() + 0.5D, this.target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AIFlyAtTarget extends Goal {
        public AIFlyAtTarget() {
        }

        @Override
        public boolean canStart() {
            if (EntityMyrmexRoyal.this.getTarget() != null && !EntityMyrmexRoyal.this.getMoveControl().isMoving() && EntityMyrmexRoyal.this.random.nextInt(7) == 0) {
                return EntityMyrmexRoyal.this.squaredDistanceTo(EntityMyrmexRoyal.this.getTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue() {
            return EntityMyrmexRoyal.this.getMoveControl().isMoving() && EntityMyrmexRoyal.this.getTarget() != null && EntityMyrmexRoyal.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            LivingEntity LivingEntity = EntityMyrmexRoyal.this.getTarget();
            Vec3d Vector3d = LivingEntity.getCameraPosVec(1.0F);
            EntityMyrmexRoyal.this.moveControl.moveTo(Vector3d.x, Vector3d.y, Vector3d.z, 1.0D);
        }

        @Override
        public void stop() {

        }

        @Override
        public void tick() {
            LivingEntity LivingEntity = EntityMyrmexRoyal.this.getTarget();
            if (LivingEntity != null) {
                if (EntityMyrmexRoyal.this.getBoundingBox().intersects(LivingEntity.getBoundingBox())) {
                    EntityMyrmexRoyal.this.tryAttack(LivingEntity);
                } else {
                    double d0 = EntityMyrmexRoyal.this.squaredDistanceTo(LivingEntity);

                    if (d0 < 9.0D) {
                        Vec3d Vector3d = LivingEntity.getCameraPosVec(1.0F);
                        EntityMyrmexRoyal.this.moveControl.moveTo(Vector3d.x, Vector3d.y, Vector3d.z, 1.0D);
                    }
                }
            }

        }
    }
}
