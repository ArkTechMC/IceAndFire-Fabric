package com.iafenvoy.iceandfire.entity;

import com.google.common.base.Predicate;
import com.iafenvoy.citadel.animation.Animation;
import com.iafenvoy.citadel.animation.AnimationHandler;
import com.iafenvoy.citadel.animation.IAnimatedEntity;
import com.iafenvoy.iceandfire.IafConfig;
import com.iafenvoy.iceandfire.entity.ai.DreadAITargetNonDread;
import com.iafenvoy.iceandfire.entity.ai.DreadLichAIStrife;
import com.iafenvoy.iceandfire.entity.util.IAnimalFear;
import com.iafenvoy.iceandfire.entity.util.IDreadMob;
import com.iafenvoy.iceandfire.entity.util.IVillagerFear;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.IafParticles;
import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class EntityDreadLich extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, RangedAttackMob {

    public static final Animation ANIMATION_SPAWN = Animation.create(40);
    public static final Animation ANIMATION_SUMMON = Animation.create(15);
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(EntityDreadLich.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MINION_COUNT = DataTracker.registerData(EntityDreadLich.class, TrackedDataHandlerRegistry.INTEGER);
    private final DreadLichAIStrife aiArrowAttack = new DreadLichAIStrife(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.0D, false);
    private int animationTick;
    private Animation currentAnimation;
    private int fireCooldown = 0;
    private int minionCooldown = 0;

    public EntityDreadLich(EntityType<? extends EntityDreadMob> type, World worldIn) {
        super(type, worldIn);
    }

    public static boolean canLichSpawnOn(EntityType<? extends MobEntity> typeIn, ServerWorldAccess worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
        BlockPos blockpos = pos.down();
        return reason == SpawnReason.SPAWNER || worldIn.getBlockState(blockpos).allowsSpawning(worldIn, blockpos, typeIn) && randomIn.nextInt(IafConfig.lichSpawnChance) == 0;
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 2.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, IDreadMob.class));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, (Predicate<LivingEntity>) DragonUtils::canHostilesTarget));
        this.targetSelector.add(3, new DreadAITargetNonDread(this, LivingEntity.class, false, (Predicate<LivingEntity>) entity -> entity instanceof LivingEntity && DragonUtils.canHostilesTarget(entity)));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
        this.dataTracker.startTracking(MINION_COUNT, 0);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = this.getWorld().getBlockState(this.getBlockPos().down());
            if (belowBlock.getBlock() != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, belowBlock), this.getX() + this.random.nextFloat() * this.getWidth() * 2.0F - this.getWidth(), this.getBoundingBox().minY, this.getZ() + this.random.nextFloat() * this.getWidth() * 2.0F - this.getWidth(), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
                }
            }
            this.setVelocity(0, this.getVelocity().y, this.getVelocity().z);

        }
        if (this.getWorld().isClient && this.getAnimation() == ANIMATION_SUMMON) {
            double d0 = 0;
            double d1 = 0;
            double d2 = 0;
            float f = this.bodyYaw * 0.017453292F + MathHelper.cos(this.age * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            this.getWorld().addParticle(IafParticles.DREAD_TORCH, this.getX() + (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double) f2 * 0.6D, d0, d1, d2);
            this.getWorld().addParticle(IafParticles.DREAD_TORCH, this.getX() - (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double) f2 * 0.6D, d0, d1, d2);
        }
        if (this.fireCooldown > 0) {
            this.fireCooldown--;
        }
        if (this.minionCooldown > 0) {
            this.minionCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void initEquipment(Random pRandom, LocalDifficulty pDifficulty) {
        super.initEquipment(pRandom, pDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(IafItems.LICH_STAFF));
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        EntityData data = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.initEquipment(worldIn.getRandom(), difficultyIn);
        this.setVariant(this.random.nextInt(5));
        this.setCombatTask();
        return data;
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
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("MinionCount", this.getMinionCount());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setMinionCount(compound.getInt("MinionCount"));
        this.setCombatTask();
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    public int getMinionCount() {
        return this.dataTracker.get(MINION_COUNT);
    }

    public void setMinionCount(int minions) {
        this.dataTracker.set(MINION_COUNT, minions);
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
        return new Animation[]{ANIMATION_SPAWN, ANIMATION_SUMMON};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public Entity getCommander() {
        return null;
    }

    @Override
    public void equipStack(EquipmentSlot slotIn, ItemStack stack) {
        super.equipStack(slotIn, stack);

        if (!this.getWorld().isClient && slotIn == EquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }

    public void setCombatTask() {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            this.goalSelector.remove(this.aiAttackOnCollide);
            this.goalSelector.remove(this.aiArrowAttack);
            ItemStack itemstack = this.getMainHandStack();
            if (itemstack.getItem() == IafItems.LICH_STAFF) {
                int i = 100;
                this.aiArrowAttack.setAttackCooldown(i);
                this.goalSelector.add(4, this.aiArrowAttack);
            } else {
                this.goalSelector.add(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void attack(LivingEntity target, float distanceFactor) {
        boolean flag = false;
        if (this.getMinionCount() < 5 && this.minionCooldown == 0) {
            this.setAnimation(ANIMATION_SUMMON);
            this.playSound(IafSounds.DREAD_LICH_SUMMON, this.getSoundVolume(), this.getSoundPitch());
            MobEntity minion = this.getRandomNewMinion();
            int x = (int) (this.getX()) - 5 + this.random.nextInt(10);
            int z = (int) (this.getZ()) - 5 + this.random.nextInt(10);
            double y = this.getHeightFromXZ(x, z);
            minion.refreshPositionAndAngles(x + 0.5D, y, z + 0.5D, this.getYaw(), this.getPitch());
            minion.setTarget(target);
            World currentLevel = this.getWorld();
            if (currentLevel instanceof ServerWorldAccess) {
                minion.initialize((ServerWorldAccess) currentLevel, currentLevel.getLocalDifficulty(this.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            }
            if (minion instanceof EntityDreadMob) {
                ((EntityDreadMob) minion).setCommanderId(this.getUuid());
            }
            if (!currentLevel.isClient) {
                currentLevel.spawnEntity(minion);
            }
            this.minionCooldown = 100;
            this.setMinionCount(this.getMinionCount() + 1);
            flag = true;
        }
        if (this.fireCooldown == 0 && !flag) {
            this.swingHand(Hand.MAIN_HAND);
            this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, this.getSoundVolume(), this.getSoundPitch());
            EntityDreadLichSkull skull = new EntityDreadLichSkull(IafEntities.DREAD_LICH_SKULL, this.getWorld(), this,
                    6);
            double d0 = target.getX() - this.getX();
            double d1 = target.getBoundingBox().minY + target.getHeight() * 2 - skull.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt((float) (d0 * d0 + d2 * d2));
            skull.setVelocity(d0, d1 + d3 * 0.20000000298023224D, d2, 0.0F, 14 - this.getWorld().getDifficulty().getId() * 4);
            this.getWorld().spawnEntity(skull);
            this.fireCooldown = 100;
        }
    }

    private MobEntity getRandomNewMinion() {
        float chance = this.random.nextFloat();
        if (chance > 0.5F) {
            return new EntityDreadThrall(IafEntities.DREAD_THRALL, this.getWorld());
        } else if (chance > 0.35F) {
            return new EntityDreadGhoul(IafEntities.DREAD_GHOUL, this.getWorld());
        } else if (chance > 0.15F) {
            return new EntityDreadBeast(IafEntities.DREAD_BEAST, this.getWorld());
        } else {
            return new EntityDreadScuttler(IafEntities.DREAD_SCUTTLER, this.getWorld());
        }
    }

    private double getHeightFromXZ(int x, int z) {
        BlockPos thisPos = new BlockPos(x, (int) (this.getY() + 7), z);
        while (this.getWorld().isAir(thisPos) && thisPos.getY() > 2) {
            thisPos = thisPos.down();
        }
        return thisPos.getY() + 1.0D;
    }

    @Override
    public boolean isTeammate(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isTeammate(entityIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_STRAY_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_STRAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_STRAY_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_STRAY_STEP, 0.15F, 1.0F);
    }

}