package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.base.Predicate;
import dev.arktechmc.iafextra.event.EventBus;
import net.minecraft.block.BlockState;
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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexQueen extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final Animation ANIMATION_EGG = Animation.create(20);
    public static final Animation ANIMATION_DIGNEST = Animation.create(45);
    public static final Identifier DESERT_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_queen_desert");
    public static final Identifier JUNGLE_LOOT = new Identifier(IceAndFire.MOD_ID, "entities/myrmex_queen_jungle");
    private static final Identifier TEXTURE_DESERT = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_desert_queen.png");
    private static final Identifier TEXTURE_JUNGLE = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_jungle_queen.png");
    private static final TrackedData<Boolean> HASMADEHOME = DataTracker.registerData(EntityMyrmexQueen.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int eggTicks = 0;

    public EntityMyrmexQueen(EntityType<EntityMyrmexQueen> t, World worldIn) {
        super(t, worldIn);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 120D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 3.5D)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 15.0D);
    }

    @Override
    protected Identifier getLootTableId() {
        return this.isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    public int getXpToDrop() {
        return 20;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HASMADEHOME, Boolean.TRUE);
    }

    @Override
    protected TradeOffers.Factory[] getLevel1Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_QUEEN.get(1) : MyrmexTrades.DESERT_QUEEN.get(1);
    }

    @Override
    protected TradeOffers.Factory[] getLevel2Trades() {
        return this.isJungle() ? MyrmexTrades.JUNGLE_QUEEN.get(2) : MyrmexTrades.DESERT_QUEEN.get(2);
    }

    @Override
    public void setCustomName(Text name) {
        if (this.getHive() != null) {
            if (!this.getHive().colonyName.equals(name.getContent())) {
                this.getHive().colonyName = name.getString();
            }
        }
        super.setCustomName(name);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("EggTicks", this.eggTicks);
        tag.putBoolean("MadeHome", this.hasMadeHome());

    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.eggTicks = tag.getInt("EggTicks");
        this.setMadeHome(tag.getBoolean("MadeHome"));
    }

    public boolean hasMadeHome() {
        return this.dataTracker.get(HASMADEHOME);
    }

    public void setMadeHome(boolean madeHome) {
        this.dataTracker.set(HASMADEHOME, madeHome);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getAnimation() == ANIMATION_DIGNEST) {
            this.spawnGroundEffects(3);
        }
        if (this.getHive() != null) {
            this.getHive().tick(0, this.getWorld());
        }

        if (this.hasMadeHome() && this.getGrowthStage() >= 2 && !this.canSeeSky()) {
            this.eggTicks++;
        } else if (this.canSeeSky()) {
            this.setAnimation(ANIMATION_DIGNEST);
            if (this.getAnimationTick() == 42) {
                int down = Math.max(15, this.getBlockPos().getY() - 20 + this.getRandom().nextInt(10));
                BlockPos genPos = new BlockPos(this.getBlockX(), down, this.getBlockZ());
                if (!EventBus.post(new GenericGriefEvent(this, genPos.getX(), genPos.getY(), genPos.getZ()))) {
                    WorldGenMyrmexHive hiveGen = new WorldGenMyrmexHive(true, this.isJungle(), DefaultFeatureConfig.CODEC);
                    if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld) {
                        hiveGen.placeSmallGen((ServerWorld) this.getWorld(), this.getRandom(), genPos);
                    }
                    this.setMadeHome(true);
                    this.refreshPositionAndAngles(genPos.getX(), down, genPos.getZ(), 0, 0);
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 30));
                    this.setHive(hiveGen.hive);
                    for (int i = 0; i < 3; i++) {
                        EntityMyrmexWorker worker = new EntityMyrmexWorker(IafEntityRegistry.MYRMEX_WORKER.get(),
                                this.getWorld());
                        worker.copyPositionAndRotation(this);
                        worker.setHive(this.getHive());
                        worker.setJungleVariant(this.isJungle());
                        if (!this.getWorld().isClient) {
                            this.getWorld().spawnEntity(worker);
                        }
                    }
                    return;
                }
            }
        }
        if (!this.getWorld().isClient && this.eggTicks > IafConfig.myrmexPregnantTicks && this.getHive() == null || !this.getWorld().isClient && this.getHive() != null && this.getHive().repopulate() && this.eggTicks > IafConfig.myrmexPregnantTicks) {
            float radius = -5.25F;
            float angle = (0.01745329251F * this.bodyYaw);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            BlockPos eggPos = BlockPos.ofFloored(this.getX() + extraX, this.getY() + 0.75F, this.getZ() + extraZ);
            if (this.getWorld().isAir(eggPos)) {
                this.setAnimation(ANIMATION_EGG);
                if (this.getAnimationTick() == 10) {
                    EntityMyrmexEgg egg = new EntityMyrmexEgg(IafEntityRegistry.MYRMEX_EGG.get(), this.getWorld());
                    egg.setJungle(this.isJungle());
                    int caste = getRandomCaste(this.getWorld(), this.getRandom(), this.getHive() == null || this.getHive().reproduces);
                    egg.setMyrmexCaste(caste);
                    egg.refreshPositionAndAngles(this.getX() + extraX, this.getY() + 0.75F, this.getZ() + extraZ, 0, 0);
                    if (this.getHive() != null) {
                        egg.hiveUUID = this.getHive().hiveUUID;
                    }
                    if (!this.getWorld().isClient) {
                        this.getWorld().spawnEntity(egg);
                    }
                    this.eggTicks = 0;
                }

            }


        }
        if (this.getAnimation() == ANIMATION_BITE && this.getTarget() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(this.getTarget().getBoundingBox())) {
                this.getTarget().damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAnimationTick() == 0) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && this.getTarget() != null && this.getAnimationTick() == 6) {
            if (this.getAttackBounds().intersects(this.getTarget().getBoundingBox())) {
                LivingEntity attackTarget = this.getTarget();
                attackTarget.damage(this.getWorld().getDamageSources().mobAttack(this), ((int) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() * 2));
                attackTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 2));
                attackTarget.velocityDirty = true;
                float f = MathHelper.sqrt((float) (0.5 * 0.5 + 0.5 * 0.5));
                attackTarget.setVelocity(attackTarget.getVelocity().multiply(0.5D, 1, 0.5D));
                attackTarget.setVelocity(attackTarget.getVelocity().add(-0.5 / f * 4, 1, -0.5 / f * 4));

                if (attackTarget.isOnGround()) {
                    attackTarget.setVelocity(attackTarget.getVelocity().add(0, 0.4, 0));
                }
            }
        }

    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return super.isInvulnerableTo(source) || this.getAnimation() == ANIMATION_DIGNEST;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new MyrmexAITradePlayer(this));
        this.goalSelector.add(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.add(1, new MyrmexAIAttackMelee(this, 1.0D, true));
        this.goalSelector.add(3, new MyrmexAIReEnterHive(this, 1.0D));
        this.goalSelector.add(4, new MyrmexAIWanderHiveCenter(this, 1.0D));
        this.goalSelector.add(5, new MyrmexQueenAIWander(this, 1D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new MyrmexAIDefendHive(this));
        this.targetSelector.add(2, new RevengeGoal(this));
        this.targetSelector.add(3, new MyrmexAIAttackPlayers(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, true, (Predicate<LivingEntity>) entity -> entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexQueen.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof Monster)));

    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public boolean isInHive() {
        if (this.getHive() != null) {
            for (BlockPos pos : this.getHive().getAllRooms()) {
                if (this.isCloseEnoughToTarget(MyrmexHive.getGroundedPos(this.getWorld(), pos), 300))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldMoveThroughHive() {
        return false;
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(IafConfig.myrmexBaseAttackStrength * 3.5D);
    }

    @Override
    public Identifier getAdultTexture() {
        return this.isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 1.75F;
    }

    @Override
    public int getCasteImportance() {
        return 3;
    }

    @Override
    public boolean shouldLeaveHive() {
        return false;
    }

    @Override
    public boolean shouldEnterHive() {
        return true;
    }

    @Override
    public boolean tryAttack(@NotNull Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
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
    public boolean canMove() {
        return super.canMove() && this.hasMadeHome();
    }

    public void spawnGroundEffects(float size) {
        for (int i = 0; i < size * 3; i++) {
            for (int i1 = 0; i1 < 10; i1++) {
                double motionX = this.getRandom().nextGaussian() * 0.07D;
                double motionY = this.getRandom().nextGaussian() * 0.07D;
                double motionZ = this.getRandom().nextGaussian() * 0.07D;
                float radius = size * this.random.nextFloat();
                float angle = (0.01745329251F * this.bodyYaw) * 3.14F * this.random.nextFloat();
                double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * MathHelper.cos(angle);

                BlockState BlockState = this.getWorld().getBlockState(BlockPos.ofFloored(this.getBlockX() + extraX, this.getBlockY() + extraY - 1, this.getBlockZ() + extraZ));
                if (BlockState.isAir()) {
                    if (this.getWorld().isClient) {
                        this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, BlockState), true, this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ, motionX, motionY, motionZ);
                    }
                }
            }
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING, ANIMATION_EGG, ANIMATION_DIGNEST};
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
        return false;
    }
}
