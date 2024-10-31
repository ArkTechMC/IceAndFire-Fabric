package com.iafenvoy.iceandfire.entity;

import com.google.common.base.Predicate;
import com.iafenvoy.iceandfire.entity.ai.DreadAITargetNonDread;
import com.iafenvoy.iceandfire.entity.util.IAnimalFear;
import com.iafenvoy.iceandfire.entity.util.IDreadMob;
import com.iafenvoy.iceandfire.entity.util.IHasArmorVariant;
import com.iafenvoy.iceandfire.entity.util.IVillagerFear;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import com.iafenvoy.uranus.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class EntityDreadThrall extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHasArmorVariant {

    public static final Animation ANIMATION_SPAWN = Animation.create(40);
    private static final TrackedData<Boolean> CUSTOM_ARMOR_HEAD = DataTracker.registerData(EntityDreadThrall.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CUSTOM_ARMOR_CHEST = DataTracker.registerData(EntityDreadThrall.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CUSTOM_ARMOR_LEGS = DataTracker.registerData(EntityDreadThrall.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CUSTOM_ARMOR_FEET = DataTracker.registerData(EntityDreadThrall.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> CUSTOM_ARMOR_INDEX = DataTracker.registerData(EntityDreadThrall.class, TrackedDataHandlerRegistry.INTEGER);
    private int animationTick;
    private Animation currentAnimation;

    public EntityDreadThrall(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 2.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, true));
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
        this.dataTracker.startTracking(CUSTOM_ARMOR_INDEX, 0);
        this.dataTracker.startTracking(CUSTOM_ARMOR_HEAD, Boolean.FALSE);
        this.dataTracker.startTracking(CUSTOM_ARMOR_CHEST, Boolean.FALSE);
        this.dataTracker.startTracking(CUSTOM_ARMOR_LEGS, Boolean.FALSE);
        this.dataTracker.startTracking(CUSTOM_ARMOR_FEET, Boolean.FALSE);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = this.getWorld().getBlockState(this.getBlockPos().down());
            if (belowBlock.getBlock() != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, belowBlock), this.getX() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getBoundingBox().minY, this.getZ() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
                }
            }
            this.setVelocity(0, this.getVelocity().y, 0);
        }
        if (this.getMainHandStack().getItem() == Items.BOW) {
            this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BONE));
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void initEquipment(Random randomSource, LocalDifficulty difficulty) {
        super.initEquipment(randomSource, difficulty);
        if (this.random.nextFloat() < 0.75F) {
            double chance = this.random.nextFloat();
            if (chance < 0.0025F) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(IafItems.DRAGONSTEEL_ICE_SWORD));
            }
            if (chance < 0.01F) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
            }
            if (chance < 0.1F) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            if (chance < 0.75F) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(IafItems.DREAD_SWORD));
            }
        }
        if (this.random.nextFloat() < 0.75F) {
            this.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
            this.setCustomArmorHead(this.random.nextInt(8) != 0);
        }
        if (this.random.nextFloat() < 0.75F) {
            this.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
            this.setCustomArmorChest(this.random.nextInt(8) != 0);
        }
        if (this.random.nextFloat() < 0.75F) {
            this.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
            this.setCustomArmorLegs(this.random.nextInt(8) != 0);
        }
        if (this.random.nextFloat() < 0.75F) {
            this.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
            this.setCustomArmorFeet(this.random.nextInt(8) != 0);
        }
        this.setBodyArmorVariant(this.random.nextInt(8));
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        EntityData data = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.initEquipment(worldIn.getRandom(), difficultyIn);
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
        compound.putInt("ArmorVariant", this.getBodyArmorVariant());
        compound.putBoolean("HasCustomHelmet", this.hasCustomArmorHead());
        compound.putBoolean("HasCustomChestplate", this.hasCustomArmorChest());
        compound.putBoolean("HasCustomLeggings", this.hasCustomArmorLegs());
        compound.putBoolean("HasCustomBoots", this.hasCustomArmorFeet());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setBodyArmorVariant(compound.getInt("ArmorVariant"));
        this.setCustomArmorHead(compound.getBoolean("HasCustomHelmet"));
        this.setCustomArmorChest(compound.getBoolean("HasCustomChestplate"));
        this.setCustomArmorLegs(compound.getBoolean("HasCustomLeggings"));
        this.setCustomArmorFeet(compound.getBoolean("HasCustomBoots"));
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public boolean hasCustomArmorHead() {
        return this.dataTracker.get(CUSTOM_ARMOR_HEAD);
    }

    public void setCustomArmorHead(boolean head) {
        this.dataTracker.set(CUSTOM_ARMOR_HEAD, head);
    }

    public boolean hasCustomArmorChest() {
        return this.dataTracker.get(CUSTOM_ARMOR_CHEST);
    }

    public void setCustomArmorChest(boolean head) {
        this.dataTracker.set(CUSTOM_ARMOR_CHEST, head);
    }

    public boolean hasCustomArmorLegs() {
        return this.dataTracker.get(CUSTOM_ARMOR_LEGS);
    }

    public void setCustomArmorLegs(boolean head) {
        this.dataTracker.set(CUSTOM_ARMOR_LEGS, head);
    }

    public boolean hasCustomArmorFeet() {
        return this.dataTracker.get(CUSTOM_ARMOR_FEET);
    }

    public void setCustomArmorFeet(boolean head) {
        this.dataTracker.set(CUSTOM_ARMOR_FEET, head);
    }

    @Override
    public int getBodyArmorVariant() {
        return this.dataTracker.get(CUSTOM_ARMOR_INDEX);
    }

    @Override
    public void setBodyArmorVariant(int variant) {
        this.dataTracker.set(CUSTOM_ARMOR_INDEX, variant);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_SPAWN};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
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