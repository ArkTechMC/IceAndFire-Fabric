package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityDragonSkull extends AnimalEntity implements IBlacklistedFromStatues, IDeadMob {

    private static final TrackedData<Integer> DRAGON_TYPE = DataTracker.registerData(EntityDragonSkull.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DRAGON_AGE = DataTracker.registerData(EntityDragonSkull.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DRAGON_STAGE = DataTracker.registerData(EntityDragonSkull.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DRAGON_DIRECTION = DataTracker.registerData(EntityDragonSkull.class, TrackedDataHandlerRegistry.FLOAT);

    public final float minSize = 0.3F;
    public final float maxSize = 8.58F;

    public EntityDragonSkull(EntityType type, World worldIn) {
        super(type, worldIn);
        this.ignoreCameraFrustum = true;
        // setScale(this.getDragonAge());
    }

    @Override
    public boolean isBreedingItem(@NotNull ItemStack stack) {
        return false;
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0D);
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getAttacker() != null && super.isInvulnerableTo(i);
    }

    @Override
    public boolean isAiDisabled() {
        return true;
    }

    public boolean isOnWall() {
        return this.getWorld().isAir(this.getBlockPos().down());
    }

    public void onUpdate() {
        this.prevBodyYaw = 0;
        this.prevHeadYaw = 0;
        this.bodyYaw = 0;
        this.headYaw = 0;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(DRAGON_TYPE, 0);
        this.getDataTracker().startTracking(DRAGON_AGE, 0);
        this.getDataTracker().startTracking(DRAGON_STAGE, 0);
        this.getDataTracker().startTracking(DRAGON_DIRECTION, 0F);
    }

    public float getYaw() {
        return this.getDataTracker().get(DRAGON_DIRECTION).floatValue();
    }

    public void setYaw(float var1) {
        this.getDataTracker().set(DRAGON_DIRECTION, var1);
    }

    public int getDragonType() {
        return this.getDataTracker().get(DRAGON_TYPE).intValue();
    }

    public void setDragonType(int var1) {
        this.getDataTracker().set(DRAGON_TYPE, var1);
    }

    public int getStage() {
        return this.getDataTracker().get(DRAGON_STAGE).intValue();
    }

    public void setStage(int var1) {
        this.getDataTracker().set(DRAGON_STAGE, var1);
    }

    public int getDragonAge() {
        return this.getDataTracker().get(DRAGON_AGE).intValue();
    }

    public void setDragonAge(int var1) {
        this.getDataTracker().set(DRAGON_AGE, var1);
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return null;
    }

    @Override
    public boolean damage(@NotNull DamageSource var1, float var2) {
        this.turnIntoItem();
        return super.damage(var1, var2);
    }

    public void turnIntoItem() {
        if (isRemoved())
            return;
        this.remove(RemovalReason.DISCARDED);
        ItemStack stack = new ItemStack(getDragonSkullItem());
        stack.setNbt(new NbtCompound());
        stack.getNbt().putInt("Stage", this.getStage());
        stack.getNbt().putInt("DragonAge", this.getDragonAge());
        if (!this.getWorld().isClient)
            this.dropStack(stack, 0.0F);

    }

    public Item getDragonSkullItem() {
        switch (getDragonType()) {
            case 0:
                return IafItemRegistry.DRAGON_SKULL_FIRE.get();
            case 1:
                return IafItemRegistry.DRAGON_SKULL_ICE.get();
            case 2:
                return IafItemRegistry.DRAGON_SKULL_LIGHTNING.get();
            default:
                return IafItemRegistry.DRAGON_SKULL_FIRE.get();
        }
    }

    @Override
    public PassiveEntity createChild(@NotNull ServerWorld serverWorld, @NotNull PassiveEntity ageable) {
        return null;
    }

    @Override
    public @NotNull ActionResult interactMob(PlayerEntity player, @NotNull Hand hand) {
        if (player.isSneaking()) {
            this.setYaw(player.getYaw());
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        this.setDragonType(compound.getInt("Type"));
        this.setStage(compound.getInt("Stage"));
        this.setDragonAge(compound.getInt("DragonAge"));
        this.setYaw(compound.getFloat("DragonYaw"));
        super.readCustomDataFromNbt(compound);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        compound.putInt("Type", this.getDragonType());
        compound.putInt("Stage", this.getStage());
        compound.putInt("DragonAge", this.getDragonAge());
        compound.putFloat("DragonYaw", this.getYaw());
        super.writeCustomDataToNbt(compound);
    }

    public float getDragonSize() {
        float step;
        step = (minSize - maxSize) / (125);

        if (this.getDragonAge() > 125) {
            return this.minSize + (step * 125);
        }

        return this.minSize + (step * this.getDragonAge());
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    public int getDragonStage() {
        return Math.max(getStage(), 1);
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
