package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexSoldier extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final Identifier DESERT_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_soldier_desert");
    public static final Identifier JUNGLE_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_soldier_jungle");
    private static final Identifier TEXTURE_DESERT = new Identifier(IceAndFire.MOD_ID,"textures/models/myrmex/myrmex_desert_soldier.png");
    private static final Identifier TEXTURE_JUNGLE = new Identifier(IceAndFire.MOD_ID,"textures/models/myrmex/myrmex_jungle_soldier.png");
    public EntityMyrmexBase guardingEntity = null;

    public EntityMyrmexSoldier(EntityType<EntityMyrmexSoldier> t, World worldIn) {
        super(t, worldIn);
    }

    @Override
    protected TradeOffers.Factory[] getLevel1Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_SOLDIER.get(1) : MyrmexTrades.DESERT_SOLDIER.get(1);
    }

    @Override
    protected TradeOffers.Factory[] getLevel2Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_SOLDIER.get(2) : MyrmexTrades.DESERT_SOLDIER.get(2);
    }

    @Override
    protected Identifier getLootTableId() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public int getXpToDrop() {
        return 5;
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
                this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.POISON, 200, 2));
            }
        }*/
        if (this.guardingEntity != null) {
            this.guardingEntity.isBeingGuarded = true;
            this.isEnteringHive = this.guardingEntity.isEnteringHive;
            if (!this.guardingEntity.isAlive()) {
                this.guardingEntity.isBeingGuarded = false;
                this.guardingEntity = null;
            }
        }

    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new MyrmexAITradePlayer(this));
        this.goalSelector.add(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.add(1, new MyrmexAIAttackMelee(this, 1.0D, true));
        this.goalSelector.add(2, new MyrmexAIEscortEntity(this, 1.0D));
        this.goalSelector.add(2, new MyrmexAIReEnterHive(this, 1.0D));
        this.goalSelector.add(4, new MyrmexAILeaveHive(this, 1.0D));
        this.goalSelector.add(5, new MyrmexAIMoveThroughHive(this, 1.0D));
        this.goalSelector.add(6, new MyrmexAIWander(this, 1D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new MyrmexAIDefendHive(this));
        this.targetSelector.add(2, new MyrmexAIFindGaurdingEntity(this));
        this.targetSelector.add(3, new RevengeGoal(this));
        this.targetSelector.add(4, new MyrmexAIAttackPlayers(this));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(LivingEntity entity) {
                return entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexSoldier.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Monster);
            }
        }));
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
            //SPEED
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35D)
            //ATTACK
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 2D)
            //FOLLOW RANGE
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
            //ARMOR
            .add(EntityAttributes.GENERIC_ARMOR, 6.0D);
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
        return 0.8F;
    }

    @Override
    public int getCasteImportance() {
        return 1;
    }

    @Override
    public boolean shouldLeaveHive() {
        return false;
    }

    @Override
    public boolean shouldEnterHive() {
        return this.guardingEntity == null || !this.guardingEntity.canSeeSky() || this.guardingEntity.shouldEnterHive();
    }

    @Override
    public boolean tryAttack(@NotNull Entity entityIn) {
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

    @Override
    public boolean needsGaurding() {
        return false;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING};
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
