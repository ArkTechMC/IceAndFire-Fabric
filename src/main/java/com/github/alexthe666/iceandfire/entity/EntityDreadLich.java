package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.ai.DreadLichAIStrife;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
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
import org.jetbrains.annotations.NotNull;

public class EntityDreadLich extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, RangedAttackMob {

    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(EntityDreadLich.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MINION_COUNT = DataTracker.registerData(EntityDreadLich.class, TrackedDataHandlerRegistry.INTEGER);
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    public static Animation ANIMATION_SUMMON = Animation.create(15);
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

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, IDreadMob.class));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(LivingEntity entity) {
                return DragonUtils.canHostilesTarget(entity);
            }
        }));
        this.targetSelector.add(3, new DreadAITargetNonDread(this, LivingEntity.class, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(LivingEntity entity) {
                return entity instanceof LivingEntity && DragonUtils.canHostilesTarget(entity);
            }
        }));
    }


    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
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
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
        this.dataTracker.startTracking(MINION_COUNT, 0);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = getWorld().getBlockState(this.getBlockPos().down());
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
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, this.getX() + (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double) f2 * 0.6D, d0, d1, d2);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, this.getX() - (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double) f2 * 0.6D, d0, d1, d2);
        }
        if (fireCooldown > 0) {
            fireCooldown--;
        }
        if (minionCooldown > 0) {
            minionCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void initEquipment(Random pRandom, LocalDifficulty pDifficulty) {
        super.initEquipment(pRandom, pDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.LICH_STAFF.get()));
    }

    @Override
    public EntityData initialize(@NotNull ServerWorldAccess worldIn, @NotNull LocalDifficulty difficultyIn, @NotNull SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        EntityData data = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.initEquipment(worldIn.getRandom(), difficultyIn);
        this.setVariant(random.nextInt(5));
        this.setCombatTask();
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
        return this.dataTracker.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    public int getMinionCount() {
        return this.dataTracker.get(MINION_COUNT).intValue();
    }

    public void setMinionCount(int minions) {
        this.dataTracker.set(MINION_COUNT, minions);
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
        return new Animation[]{ANIMATION_SPAWN, ANIMATION_SUMMON};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean shouldFear() {
        return true;
    }

    @Override
    public Entity getCommander() {
        return null;
    }

    @Override
    public void equipStack(@NotNull EquipmentSlot slotIn, @NotNull ItemStack stack) {
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
            if (itemstack.getItem() == IafItemRegistry.LICH_STAFF.get()) {
                int i = 100;
                this.aiArrowAttack.setAttackCooldown(i);
                this.goalSelector.add(4, this.aiArrowAttack);
            } else {
                this.goalSelector.add(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void attack(@NotNull LivingEntity target, float distanceFactor) {
        boolean flag = false;
        if (this.getMinionCount() < 5 && minionCooldown == 0) {
            this.setAnimation(ANIMATION_SUMMON);
            this.playSound(IafSoundRegistry.DREAD_LICH_SUMMON, this.getSoundVolume(), this.getSoundPitch());
            MobEntity minion = getRandomNewMinion();
            int x = (int) (this.getX()) - 5 + random.nextInt(10);
            int z = (int) (this.getZ()) - 5 + random.nextInt(10);
            double y = getHeightFromXZ(x, z);
            minion.refreshPositionAndAngles(x + 0.5D, y, z + 0.5D, this.getYaw(), this.getPitch());
            minion.setTarget(target);
            World currentLevel = getWorld();
            if (currentLevel instanceof ServerWorldAccess) {
                minion.initialize((ServerWorldAccess) currentLevel, currentLevel.getLocalDifficulty(this.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            }
            if (minion instanceof EntityDreadMob) {
                ((EntityDreadMob) minion).setCommanderId(this.getUuid());
            }
            if (!currentLevel.isClient) {
                currentLevel.spawnEntity(minion);
            }
            minionCooldown = 100;
            this.setMinionCount(this.getMinionCount() + 1);
            flag = true;
        }
        if (fireCooldown == 0 && !flag) {
            this.swingHand(Hand.MAIN_HAND);
            this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, this.getSoundVolume(), this.getSoundPitch());
            EntityDreadLichSkull skull = new EntityDreadLichSkull(IafEntityRegistry.DREAD_LICH_SKULL.get(), getWorld(), this,
                    6);
            double d0 = target.getX() - this.getX();
            double d1 = target.getBoundingBox().minY + target.getHeight() * 2 - skull.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt((float) (d0 * d0 + d2 * d2));
            skull.setVelocity(d0, d1 + d3 * 0.20000000298023224D, d2, 0.0F, 14 - this.getWorld().getDifficulty().getId() * 4);
            this.getWorld().spawnEntity(skull);
            fireCooldown = 100;
        }
    }

    private MobEntity getRandomNewMinion() {
        float chance = random.nextFloat();
        if (chance > 0.5F) {
            return new EntityDreadThrall(IafEntityRegistry.DREAD_THRALL.get(), getWorld());
        } else if (chance > 0.35F) {
            return new EntityDreadGhoul(IafEntityRegistry.DREAD_GHOUL.get(), getWorld());
        } else if (chance > 0.15F) {
            return new EntityDreadBeast(IafEntityRegistry.DREAD_BEAST.get(), getWorld());
        } else {
            return new EntityDreadScuttler(IafEntityRegistry.DREAD_SCUTTLER.get(), getWorld());
        }
    }

    private double getHeightFromXZ(int x, int z) {
        BlockPos thisPos = new BlockPos(x, (int) (this.getY() + 7), z);
        while (getWorld().isAir(thisPos) && thisPos.getY() > 2) {
            thisPos = thisPos.down();
        }
        double height = thisPos.getY() + 1.0D;
        return height;
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
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
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