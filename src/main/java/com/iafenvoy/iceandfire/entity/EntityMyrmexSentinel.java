package com.iafenvoy.iceandfire.entity;

import com.google.common.base.Predicate;
import com.iafenvoy.citadel.animation.Animation;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.ai.*;
import com.iafenvoy.iceandfire.entity.util.MyrmexTrades;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;

public class EntityMyrmexSentinel extends EntityMyrmexBase {

    public static final Animation ANIMATION_GRAB = Animation.create(15);
    public static final Animation ANIMATION_NIBBLE = Animation.create(10);
    public static final Animation ANIMATION_STING = Animation.create(25);
    public static final Animation ANIMATION_SLASH = Animation.create(25);
    public static final Identifier DESERT_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_sentinel_desert");
    public static final Identifier JUNGLE_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_sentinel_jungle");
    private static final Identifier TEXTURE_DESERT = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_desert_sentinel.png");
    private static final Identifier TEXTURE_JUNGLE = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_jungle_sentinel.png");
    private static final Identifier TEXTURE_DESERT_HIDDEN = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_desert_sentinel_hidden.png");
    private static final Identifier TEXTURE_JUNGLE_HIDDEN = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_jungle_sentinel_hidden.png");
    private static final TrackedData<Boolean> HIDING = DataTracker.registerData(EntityMyrmexSentinel.class, TrackedDataHandlerRegistry.BOOLEAN);
    public float holdingProgress;
    public float hidingProgress;
    public int visibleTicks = 0;
    public int daylightTicks = 0;

    public EntityMyrmexSentinel(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, IafConfig.getInstance().myrmexBaseAttackStrength * 3D)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 12.0D);
    }

    @Override
    protected TradeOffers.Factory[] getLevel1Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_SENTINEL.get(1) : MyrmexTrades.DESERT_SENTINEL.get(1);
    }

    @Override
    protected TradeOffers.Factory[] getLevel2Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_SENTINEL.get(2) : MyrmexTrades.DESERT_SENTINEL.get(2);
    }

    @Override
    protected Identifier getLootTableId() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public int getXpToDrop() {
        return 8;
    }

    public Entity getHeldEntity() {
        return this.getPassengerList().isEmpty() ? null : this.getPassengerList().get(0);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        LivingEntity attackTarget = this.getTarget();
        if (this.visibleTicks > 0) {
            this.visibleTicks--;
        } else {
            this.visibleTicks = 0;
        }
        if (attackTarget != null) {
            this.visibleTicks = 100;
        }
        if (this.canSeeSky()) {
            this.daylightTicks++;
        } else {
            this.daylightTicks = 0;
        }
        boolean holding = this.getHeldEntity() != null;
        boolean hiding = this.isHiding() && !this.hasCustomer();
        if ((holding || this.isOnResin() || attackTarget != null) || this.visibleTicks > 0) {
            this.setHiding(false);
        }
        if (holding && this.holdingProgress < 20.0F) {
            this.holdingProgress += 1.0F;
        } else if (!holding && this.holdingProgress > 0.0F) {
            this.holdingProgress -= 1.0F;
        }
        if (hiding) {
            this.setYaw(this.prevYaw);
        }
        if (hiding && this.hidingProgress < 20.0F) {
            this.hidingProgress += 1.0F;
        } else if (!hiding && this.hidingProgress > 0.0F) {
            this.hidingProgress -= 1.0F;
        }
        if (this.getHeldEntity() != null) {
            this.setAnimation(ANIMATION_NIBBLE);
            if (this.getAnimationTick() == 5) {
                this.playBiteSound();
                this.getHeldEntity().damage(this.getWorld().getDamageSources().mobAttack(this), ((float) (int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() / 6));
            }
        }
        if (this.getAnimation() == ANIMATION_GRAB && attackTarget != null && this.getAnimationTick() == 7) {
            this.playStingSound();
            if (this.getAttackBounds().intersects(attackTarget.getBoundingBox())) {
                attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), ((float) (int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() / 2));
                //Make sure it doesn't grab a dead dragon
                if (attackTarget instanceof EntityDragonBase) {
                    if (!((EntityDragonBase) attackTarget).isMobDead()) {
                        attackTarget.startRiding(this);
                    }
                } else {
                    attackTarget.startRiding(this);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_SLASH && attackTarget != null && this.getAnimationTick() % 5 == 0 && this.getAnimationTick() <= 20) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(attackTarget.getBoundingBox())) {
                attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), (float) ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue()) / 4);
            }
        }
        if (this.getAnimation() == ANIMATION_STING && (this.getAnimationTick() == 0 || this.getAnimationTick() == 10)) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && attackTarget != null && (this.getAnimationTick() == 6 || this.getAnimationTick() == 16)) {
            double dist = this.squaredDistanceTo(attackTarget);
            if (dist < 18) {
                attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue()));
                attackTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 3));
            }
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new MyrmexAIFindHidingSpot(this));
        this.goalSelector.add(0, new MyrmexAITradePlayer(this));
        this.goalSelector.add(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.add(1, new MyrmexAIAttackMelee(this, 1.0D, true));
        this.goalSelector.add(3, new MyrmexAILeaveHive(this, 1.0D));
        this.goalSelector.add(5, new MyrmexAIWander(this, 1D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new MyrmexAIDefendHive(this));
        this.targetSelector.add(3, new RevengeGoal(this));
        this.targetSelector.add(4, new MyrmexAIAttackPlayers(this));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, LivingEntity.class, 4, true, true, (Predicate<LivingEntity>) entity -> entity != null && !haveSameHive(EntityMyrmexSentinel.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Monster)));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HIDING, Boolean.FALSE);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(IafConfig.getInstance().myrmexBaseAttackStrength * 3D);
    }

    @Override
    public Identifier getAdultTexture() {
        if (this.isHiding()) {
            return this.isJungle() ? TEXTURE_JUNGLE_HIDDEN : TEXTURE_DESERT_HIDDEN;

        } else {
            return this.isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
        }
    }

    @Override
    public float getModelScale() {
        return 0.8F;
    }

    @Override
    public int getCasteImportance() {
        return 2;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("Hiding", this.isHiding());
        tag.putInt("DaylightTicks", this.daylightTicks);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setHiding(tag.getBoolean("Hiding"));
        this.daylightTicks = tag.getInt("DaylightTicks");
    }

    @Override
    public boolean shouldLeaveHive() {
        return true;
    }

    @Override
    public boolean shouldEnterHive() {
        return false;
    }

    @Override
    public void updatePassengerPosition(Entity passenger, PositionUpdater callback) {
        super.updatePassengerPosition(passenger, callback);
        if (this.hasPassenger(passenger)) {
            this.bodyYaw = this.getYaw();
            float radius = 1.25F;
            float extraY = 0.35F;
            if (this.getAnimation() == ANIMATION_GRAB) {
                int modTick = MathHelper.clamp(this.getAnimationTick(), 0, 10);
                radius = 3.25F - modTick * 0.2F;
                extraY = modTick * 0.035F;
            }
            float angle = (0.01745329251F * this.bodyYaw);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            if (passenger.getHeight() >= 1.75F) {
                extraY = passenger.getHeight() - 2F;
            }
            passenger.setPosition(this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (amount >= 1.0D && !this.getPassengerList().isEmpty() && this.random.nextInt(2) == 0) {
            for (Entity entity : this.getPassengerList()) {
                entity.stopRiding();
            }
        }
        this.visibleTicks = 300;
        this.setHiding(false);
        return super.damage(source, amount);
    }

    @Override
    public boolean tryAttack(Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_SLASH && this.getAnimation() != ANIMATION_GRAB && this.getHeldEntity() == null) {
            if (this.getRandom().nextInt(2) == 0 && entityIn.getWidth() < 2F) {
                this.setAnimation(ANIMATION_GRAB);
            } else {
                this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_STING : ANIMATION_SLASH);
            }
            this.visibleTicks = 300;
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
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_SLASH, ANIMATION_STING, ANIMATION_GRAB, ANIMATION_NIBBLE};
    }

    @Override
    public boolean canMove() {
        return super.canMove() && this.getHeldEntity() == null && !this.isHiding();
    }


    public boolean isHiding() {
        return this.dataTracker.get(HIDING);
    }

    public void setHiding(boolean hiding) {
        this.dataTracker.set(HIDING, hiding);
    }

    @Override
    public int getExperience() {
        return 4;
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
