package com.iafenvoy.iceandfire.entity;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.util.IBlacklistedFromStatues;
import com.iafenvoy.iceandfire.entity.util.IDeadMob;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.IafSounds;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import com.iafenvoy.iceandfire.world.structure.MyrmexHiveStructure;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityMyrmexEgg extends LivingEntity implements IBlacklistedFromStatues, IDeadMob {

    private static final TrackedData<Boolean> MYRMEX_TYPE = DataTracker.registerData(EntityMyrmexEgg.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> MYRMEX_AGE = DataTracker.registerData(EntityMyrmexEgg.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MYRMEX_CASTE = DataTracker.registerData(EntityMyrmexEgg.class, TrackedDataHandlerRegistry.INTEGER);
    public UUID hiveUUID;

    public EntityMyrmexEgg(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0D);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("Jungle", this.isJungle());
        tag.putInt("MyrmexAge", this.getMyrmexAge());
        tag.putInt("MyrmexCaste", this.getMyrmexCaste());
        tag.putUuid("HiveUUID", this.hiveUUID == null ? this.hiveUUID = UUID.randomUUID() : this.hiveUUID);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setJungle(tag.getBoolean("Jungle"));
        this.setMyrmexAge(tag.getInt("MyrmexAge"));
        this.setMyrmexCaste(tag.getInt("MyrmexCaste"));
        this.hiveUUID = tag.getUuid("HiveUUID");
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(MYRMEX_TYPE, false);
        this.getDataTracker().startTracking(MYRMEX_AGE, 0);
        this.getDataTracker().startTracking(MYRMEX_CASTE, 0);
    }


    public boolean isJungle() {
        return this.getDataTracker().get(MYRMEX_TYPE);
    }

    public void setJungle(boolean jungle) {
        this.getDataTracker().set(MYRMEX_TYPE, jungle);
    }

    public int getMyrmexAge() {
        return this.getDataTracker().get(MYRMEX_AGE);
    }

    public void setMyrmexAge(int i) {
        this.getDataTracker().set(MYRMEX_AGE, i);
    }

    public int getMyrmexCaste() {
        return this.getDataTracker().get(MYRMEX_CASTE);
    }

    public void setMyrmexCaste(int i) {
        this.getDataTracker().set(MYRMEX_CASTE, i);
    }

    public boolean canSeeSky() {
        return this.getWorld().isSkyVisibleAllowingSea(this.getBlockPos());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.canSeeSky()) {
            this.setMyrmexAge(this.getMyrmexAge() + 1);
        }
        if (this.getMyrmexAge() > IafCommonConfig.INSTANCE.myrmex.eggTicks.getValue()) {
            this.remove(RemovalReason.DISCARDED);
            EntityMyrmexBase myrmex = switch (this.getMyrmexCaste()) {
                case 1 -> new EntityMyrmexSoldier(IafEntities.MYRMEX_SOLDIER, this.getWorld());
                case 2 -> new EntityMyrmexRoyal(IafEntities.MYRMEX_ROYAL, this.getWorld());
                case 3 -> new EntityMyrmexSentinel(IafEntities.MYRMEX_SENTINEL, this.getWorld());
                case 4 -> new EntityMyrmexQueen(IafEntities.MYRMEX_QUEEN, this.getWorld());
                default -> new EntityMyrmexWorker(IafEntities.MYRMEX_WORKER, this.getWorld());
            };
            myrmex.setJungleVariant(this.isJungle());
            myrmex.setGrowthStage(0);
            myrmex.updatePositionAndAngles(this.getX(), this.getY(), this.getZ(), 0, 0);
            if (myrmex instanceof EntityMyrmexQueen) {
                MyrmexHive hive = new MyrmexHive(this.getWorld(), this.getBlockPos(), 100);
                PlayerEntity player = this.getWorld().getClosestPlayer(this, 30);
                if (player != null) {
                    hive.hasOwner = true;
                    hive.ownerUUID = player.getUuid();
                    if (!this.getWorld().isClient) {
                        hive.modifyPlayerReputation(player.getUuid(), 100);
                    }
                }
                MyrmexWorldData.addHive(this.getWorld(), hive);
                myrmex.setHive(hive);


            } else {
                if (MyrmexWorldData.get(this.getWorld()) != null) {
                    MyrmexHive hive;
                    if (this.hiveUUID == null) {
                        hive = MyrmexWorldData.get(this.getWorld()).getNearestHive(this.getBlockPos(), 400);
                    } else {
                        hive = MyrmexWorldData.get(this.getWorld()).getHiveFromUUID(this.hiveUUID);
                    }
                    if (!this.getWorld().isClient && hive != null && Math.sqrt(this.squaredDistanceTo(hive.getCenter().getX(), hive.getCenter().getY(), hive.getCenter().getZ())) < 2000) {
                        myrmex.setHive(hive);
                    }
                }
            }

            if (!this.getWorld().isClient) {
                this.getWorld().spawnEntity(myrmex);
            }
            this.getWorld().playSound(this.getX(), this.getY() + this.getStandingEyeHeight(), this.getZ(), IafSounds.EGG_HATCH, this.getSoundCategory(), 2.5F, 1.0F, false);
        }
    }

    @Override
    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slotIn, ItemStack stack) {

    }

    @Override
    public boolean damage(DamageSource dmg, float var2) {
        if (dmg.isOf(DamageTypes.IN_WALL) || dmg.isOf(DamageTypes.FALL)) {
            return false;
        }
        if (!this.getWorld().isClient && !dmg.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.dropStack(this.getItem(), 0);
        }
        this.remove(RemovalReason.KILLED);
        return super.damage(dmg, var2);
    }

    private ItemStack getItem() {
        ItemStack egg = new ItemStack(this.isJungle() ? IafItems.MYRMEX_JUNGLE_EGG : IafItems.MYRMEX_DESERT_EGG, 1);
        NbtCompound newTag = new NbtCompound();
        newTag.putInt("EggOrdinal", this.getMyrmexCaste());
        egg.setNbt(newTag);
        return egg;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(PlayerEntity player) {
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    public boolean isInNursery() {
        MyrmexHive hive = MyrmexWorldData.get(this.getWorld()).getNearestHive(this.getBlockPos(), 100);
        if (hive != null && hive.getRooms(MyrmexHiveStructure.RoomType.NURSERY).isEmpty() && hive.getRandomRoom(MyrmexHiveStructure.RoomType.NURSERY, this.getRandom(), this.getBlockPos()) != null) {
            return false;
        }
        if (hive != null) {
            BlockPos nursery = hive.getRandomRoom(MyrmexHiveStructure.RoomType.NURSERY, this.getRandom(), this.getBlockPos());
            return this.squaredDistanceTo(nursery.getX(), nursery.getY(), nursery.getZ()) < 2025;
        }
        return false;
    }
}
