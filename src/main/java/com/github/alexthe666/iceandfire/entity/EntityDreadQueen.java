package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class EntityDreadQueen extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    public static final Animation ANIMATION_SPAWN = Animation.create(40);
    private final ServerBossBar bossInfo = (new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS));
    private int animationTick;
    private Animation currentAnimation;

    public EntityDreadQueen(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, IafConfig.dreadQueenMaxHealth)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                //ATTACK
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0D)
                //FOLLOW RANGE
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 256.0D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 30.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, (Predicate<LivingEntity>) DragonUtils::canHostilesTarget));
        this.targetSelector.add(3, new DreadAITargetNonDread(this, LivingEntity.class, false, (Predicate<LivingEntity>) entity -> entity instanceof LivingEntity && DragonUtils.canHostilesTarget(entity)));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);

        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void setCustomName(Text name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }


    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossInfo.removePlayer(player);
    }


    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        EntityData data = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.initEquipment(worldIn.getRandom(), difficultyIn);
        return data;
    }

    @Override
    protected void initEquipment(Random pRandom, LocalDifficulty pDifficulty) {
        super.initEquipment(pRandom, pDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.DREAD_QUEEN_SWORD.get()));
        this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(IafItemRegistry.DREAD_QUEEN_STAFF.get()));
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
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
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
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }
}
