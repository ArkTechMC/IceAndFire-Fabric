package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.entity.util.IBlacklistedFromStatues;
import com.iafenvoy.iceandfire.entity.util.IDeadMob;
import com.iafenvoy.iceandfire.enums.IafSkullType;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityMobSkull extends AnimalEntity implements IBlacklistedFromStatues, IDeadMob {

    private static final TrackedData<Float> SKULL_DIRECTION = DataTracker.registerData(EntityMobSkull.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> SKULL_ENUM = DataTracker.registerData(EntityMobSkull.class, TrackedDataHandlerRegistry.INTEGER);

    public EntityMobSkull(EntityType t, World worldIn) {
        super(t, worldIn);
        this.ignoreCameraFrustum = true;
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0D);
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getAttacker() != null;
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
        this.getDataTracker().startTracking(SKULL_DIRECTION, 0F);
        this.getDataTracker().startTracking(SKULL_ENUM, 0);
    }

    @Override
    public float getYaw() {
        return this.getDataTracker().get(SKULL_DIRECTION);
    }

    @Override
    public void setYaw(float var1) {
        this.getDataTracker().set(SKULL_DIRECTION, var1);
    }

    private int getEnumOrdinal() {
        return this.getDataTracker().get(SKULL_ENUM);
    }

    private void setEnumOrdinal(int var1) {
        this.getDataTracker().set(SKULL_ENUM, var1);
    }

    public IafSkullType getSkullType() {
        return IafSkullType.values()[MathHelper.clamp(this.getEnumOrdinal(), 0, IafSkullType.values().length - 1)];
    }

    public void setSkullType(IafSkullType skullType) {
        this.setEnumOrdinal(skullType.ordinal());
    }

    @Override
    public boolean damage(DamageSource var1, float var2) {
        this.turnIntoItem();
        return super.damage(var1, var2);
    }

    public void turnIntoItem() {
        if (this.isRemoved())
            return;
        this.remove(RemovalReason.DISCARDED);
        ItemStack stack = new ItemStack(this.getSkullType().getSkullItem(), 1);
        if (!this.getWorld().isClient)
            this.dropStack(stack, 0.0F);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.isSneaking()) {
            this.setYaw(player.getYaw());
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        this.setYaw(compound.getFloat("SkullYaw"));
        this.setEnumOrdinal(compound.getInt("SkullType"));
        super.readCustomDataFromNbt(compound);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        compound.putFloat("SkullYaw", this.getYaw());
        compound.putInt("SkullType", this.getEnumOrdinal());
        super.writeCustomDataToNbt(compound);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    @Override
    public PassiveEntity createChild(ServerWorld serverWorld, PassiveEntity ageable) {
        return null;
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
