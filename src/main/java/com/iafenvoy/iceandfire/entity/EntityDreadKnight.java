package com.iafenvoy.iceandfire.entity;

import com.google.common.base.Predicate;
import com.iafenvoy.iceandfire.entity.ai.DreadAIRideHorse;
import com.iafenvoy.iceandfire.entity.ai.DreadAITargetNonDread;
import com.iafenvoy.iceandfire.entity.util.IAnimalFear;
import com.iafenvoy.iceandfire.entity.util.IDreadMob;
import com.iafenvoy.iceandfire.entity.util.IVillagerFear;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.registry.IafBannerPatterns;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import com.iafenvoy.uranus.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
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
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class EntityDreadKnight extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    public static final ItemStack SHIELD = generateShield();
    public static final Animation ANIMATION_SPAWN = Animation.create(40);
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(EntityDreadKnight.class, TrackedDataHandlerRegistry.INTEGER);
    private int animationTick;
    private Animation currentAnimation;

    public EntityDreadKnight(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    private static ItemStack generateShield() {
        ItemStack itemstack = new ItemStack(Items.CYAN_BANNER);
        NbtCompound compoundnbt = itemstack.getOrCreateSubNbt("BlockEntityTag");

        NbtList listnbt = new BannerPattern.Patterns().add(BannerPatterns.BASE, DyeColor.CYAN).add(RegistryEntry.of(IafBannerPatterns.PATTERN_DREAD), DyeColor.WHITE).toNbt();
        compoundnbt.put("Patterns", listnbt);
        ItemStack shield = new ItemStack(Items.SHIELD, 1);
        shield.setNbt(itemstack.getNbt());
        return shield;
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 20.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new DreadAIRideHorse(this));
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
        this.dataTracker.startTracking(VARIANT, 0);
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
            this.setVelocity(0, this.getVelocity().y, this.getVelocity().z);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }


    @Override
    protected void initEquipment(Random pRandom, LocalDifficulty pDifficulty) {
        super.initEquipment(pRandom, pDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(IafItems.DREAD_KNIGHT_SWORD));
        if (this.random.nextBoolean()) {
            this.equipStack(EquipmentSlot.OFFHAND, SHIELD.copy());
        }
        this.setArmorVariant(this.random.nextInt(3));
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
        compound.putInt("ArmorVariant", this.getArmorVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setArmorVariant(compound.getInt("ArmorVariant"));
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public int getArmorVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setArmorVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
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
    public double getHeightOffset() {
        return -0.6D;
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

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 0.75F;
    }
}