package com.iafenvoy.iceandfire.entity;

import com.google.common.base.Predicate;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.ai.*;
import com.iafenvoy.iceandfire.entity.util.MyrmexTrades;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.item.ItemMyrmexEgg;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.uranus.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;

public class EntityMyrmexWorker extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final Identifier DESERT_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_worker_desert");
    public static final Identifier JUNGLE_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_worker_jungle");
    private static final Identifier TEXTURE_DESERT = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_desert_worker.png");
    private static final Identifier TEXTURE_JUNGLE = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_jungle_worker.png");
    public boolean keepSearching = true;

    public EntityMyrmexWorker(EntityType<EntityMyrmexWorker> t, World worldIn) {
        super(t, worldIn);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, IafCommonConfig.INSTANCE.myrmex.baseAttackDamage.getValue())
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 4D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(IafCommonConfig.INSTANCE.myrmex.baseAttackDamage.getValue());
    }

    @Override
    protected Identifier getLootTableId() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (!this.getWorld().isClient && !this.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            this.dropStack(this.getStackInHand(Hand.MAIN_HAND), 0);
            this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.onDeath(cause);
    }

    @Override
    public int getXpToDrop() {
        return 3;
    }

    @Override
    public boolean isSmallerThanBlock() {
        return true;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        /*if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(this.getAttackTarget().getBoundingBox())) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAnimationTick() == 0) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            if (this.getAttackBounds().intersects(this.getAttackTarget().getBoundingBox())) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 2));
                this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.POISON, 60, 1));
            }
        }*/
        if (!this.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            if (this.getStackInHand(Hand.MAIN_HAND).getItem() instanceof ItemMyrmexEgg) {
                boolean isJungle = this.getStackInHand(Hand.MAIN_HAND).getItem() == IafItems.MYRMEX_JUNGLE_EGG;
                NbtCompound tag = this.getStackInHand(Hand.MAIN_HAND).getNbt();
                int metadata = 0;
                if (tag != null) {
                    metadata = tag.getInt("EggOrdinal");
                }
                EntityMyrmexEgg egg = new EntityMyrmexEgg(IafEntities.MYRMEX_EGG, this.getWorld());
                egg.copyPositionAndRotation(this);
                egg.setJungle(isJungle);
                egg.setMyrmexCaste(metadata);
                if (!this.getWorld().isClient) {
                    this.getWorld().spawnEntity(egg);
                }
                egg.startRiding(this);
                this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            }
        }
        if (!this.getPassengerList().isEmpty()) {
            for (Entity entity : this.getPassengerList()) {
                if (entity instanceof EntityMyrmexBase && ((EntityMyrmexBase) entity).getGrowthStage() >= 2) {
                    entity.stopRiding();
                }
            }
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new MyrmexAITradePlayer(this));
        this.goalSelector.add(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.add(1, new MyrmexAIAttackMelee(this, 1.0D, true));
        this.goalSelector.add(2, new MyrmexAIStoreBabies(this, 1.0D));
        this.goalSelector.add(3, new MyrmexAIStoreItems(this, 1.0D));
        this.goalSelector.add(4, new MyrmexAIReEnterHive(this, 1.0D));
        this.goalSelector.add(4, new MyrmexAILeaveHive(this, 1.0D));
        this.goalSelector.add(6, new MyrmexAIForage(this, 2));
        this.goalSelector.add(7, new MyrmexAIMoveThroughHive(this, 1.0D));
        this.goalSelector.add(8, new MyrmexAIWander(this, 1D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new MyrmexAIDefendHive(this));
        this.targetSelector.add(2, new MyrmexAIForageForItems(this));
        this.targetSelector.add(3, new MyrmexAIPickupBabies<>(this));
        this.targetSelector.add(4, new RevengeGoal(this));
        this.targetSelector.add(4, new MyrmexAIAttackPlayers(this));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, true, (Predicate<LivingEntity>) entity -> EntityMyrmexWorker.this.getMainHandStack().isEmpty() && entity != null && !haveSameHive(EntityMyrmexWorker.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Monster)));


    }

    @Override
    public boolean shouldWander() {
        return super.shouldWander() && this.canSeeSky();
    }

    @Override
    protected TradeOffers.Factory[] getLevel1Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_WORKER.get(1) : MyrmexTrades.DESERT_WORKER.get(1);
    }

    @Override
    protected TradeOffers.Factory[] getLevel2Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_WORKER.get(2) : MyrmexTrades.DESERT_WORKER.get(2);
    }

    @Override
    public Identifier getAdultTexture() {
        return this.isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 0.6F;
    }

    @Override
    public boolean shouldLeaveHive() {
        return !this.holdingSomething();
    }

    @Override
    public boolean shouldEnterHive() {
        if (this.holdingSomething()) return true;
        if (this.getWorld().isDay()) return false;
        return !IafCommonConfig.INSTANCE.myrmex.hiveIgnoreDaytime.getValue();
    }

    @Override
    public boolean shouldMoveThroughHive() {
        return !this.shouldLeaveHive() && !this.holdingSomething();
    }

    @Override
    public boolean tryAttack(Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }

        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
            float f = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            this.onAttacking(entityIn);
            boolean flag = entityIn.damage(this.getWorld().getDamageSources().mobAttack(this), f);
            if (this.getAnimation() == ANIMATION_STING && flag) {
                this.playStingSound();
                if (entityIn instanceof LivingEntity) {
                    ((LivingEntity) entityIn).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 2));
                    this.setTarget((LivingEntity) entityIn);
                }
            } else {
                this.playBiteSound();
            }
            if (!this.getWorld().isClient && this.getRandom().nextInt(3) == 0 && this.getStackInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
                this.dropStack(this.getStackInHand(Hand.MAIN_HAND), 0);
                this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            }
            if (!this.getPassengerList().isEmpty()) {
                for (Entity entity : this.getPassengerList()) {
                    entity.stopRiding();
                }
            }
            return true;
        }
        return false;
    }


    public boolean holdingSomething() {
        return this.getHeldEntity() != null || !this.getStackInHand(Hand.MAIN_HAND).isEmpty() || this.getTarget() != null;
    }

    public boolean holdingBaby() {
        return this.getHeldEntity() != null && (this.getHeldEntity() instanceof EntityMyrmexBase || this.getHeldEntity() instanceof EntityMyrmexEgg);
    }

    @Override
    public int getCasteImportance() {
        return 0;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING};
    }

    @Override
    public void updatePassengerPosition(Entity passenger, PositionUpdater callback) {
        super.updatePassengerPosition(passenger, callback);
        if (this.hasPassenger(passenger)) {
            this.bodyYaw = this.getYaw();
            float radius = 1.05F;
            float angle = (0.01745329251F * this.bodyYaw);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            passenger.setPosition(this.getX() + extraX, this.getY() + 0.25F, this.getZ() + extraZ);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (amount >= 1.0D && !this.getWorld().isClient && this.getRandom().nextInt(3) == 0 && this.getStackInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
            this.dropStack(this.getStackInHand(Hand.MAIN_HAND), 0);
            this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
        if (amount >= 1.0D && !this.getPassengerList().isEmpty()) {
            for (Entity entity : this.getPassengerList()) {
                entity.stopRiding();
            }
        }
        return super.damage(source, amount);
    }

    public Entity getHeldEntity() {
        return this.getPassengerList().isEmpty() ? null : this.getPassengerList().get(0);
    }

    public void onPickupItem(ItemEntity itemEntity) {
        Item item = itemEntity.getStack().getItem();
        if (item == IafItems.MYRMEX_JUNGLE_RESIN && this.isJungle() || item == IafItems.MYRMEX_DESERT_RESIN && !this.isJungle()) {

            PlayerEntity owner = null;
            try {
                if (itemEntity.getOwner() != null) {
                    owner = (PlayerEntity) itemEntity.getOwner();
                }
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Myrmex picked up resin that wasn't thrown!");
            }
            if (owner != null && this.getHive() != null) {
                this.getHive().modifyPlayerReputation(owner.getUuid(), 5);
                this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1, 1);
                if (!this.getWorld().isClient) {
                    this.getWorld().spawnEntity(new ExperienceOrbEntity(this.getWorld(), owner.getX(), owner.getY(), owner.getZ(), 1 + this.random.nextInt(3)));
                }
            }
        }
    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }
}
