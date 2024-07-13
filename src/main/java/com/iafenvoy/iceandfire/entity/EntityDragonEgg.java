package com.iafenvoy.iceandfire.entity;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.block.BlockEntityEggInIce;
import com.iafenvoy.iceandfire.entity.util.IBlacklistedFromStatues;
import com.iafenvoy.iceandfire.entity.util.IDeadMob;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonType;
import com.iafenvoy.iceandfire.enums.EnumDragonColor;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.IafSounds;
import com.iafenvoy.uranus.util.BlockUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class EntityDragonEgg extends LivingEntity implements IBlacklistedFromStatues, IDeadMob {

    protected static final TrackedData<Optional<UUID>> OWNER_UNIQUE_ID = DataTracker.registerData(EntityDragonEgg.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Integer> DRAGON_TYPE = DataTracker.registerData(EntityDragonEgg.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DRAGON_AGE = DataTracker.registerData(EntityDragonEgg.class, TrackedDataHandlerRegistry.INTEGER);

    public EntityDragonEgg(EntityType<EntityDragonEgg> type, World worldIn) {
        super(type, worldIn);
    }

    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return MobEntity.createMobAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0D);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("Color", (byte) this.getEggType().ordinal());
        tag.putInt("DragonAge", this.getDragonAge());
        try {
            if (this.getOwnerId() == null) {
                tag.putString("OwnerUUID", "");
            } else {
                tag.putString("OwnerUUID", this.getOwnerId().toString());
            }
        } catch (Exception e) {
            IceAndFire.LOGGER.error("An error occurred while trying to read the NBT data of a dragon egg", e);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setEggType(EnumDragonColor.values()[tag.getInt("Color")]);
        this.setDragonAge(tag.getInt("DragonAge"));
        String s;

        if (tag.contains("OwnerUUID", 8)) {
            s = tag.getString("OwnerUUID");
        } else {
            String s1 = tag.getString("Owner");
            UUID converedUUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), s1);
            s = converedUUID == null ? s1 : converedUUID.toString();
        }
        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(DRAGON_TYPE, 0);
        this.getDataTracker().startTracking(DRAGON_AGE, 0);
        this.getDataTracker().startTracking(OWNER_UNIQUE_ID, Optional.empty());
    }

    public UUID getOwnerId() {
        return this.dataTracker.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(UUID p_184754_1_) {
        this.dataTracker.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public EnumDragonColor getEggType() {
        return EnumDragonColor.values()[this.getDataTracker().get(DRAGON_TYPE)];
    }

    public void setEggType(EnumDragonColor newtype) {
        this.getDataTracker().set(DRAGON_TYPE, newtype.ordinal());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getAttacker() != null && super.isInvulnerableTo(i);
    }

    public int getDragonAge() {
        return this.getDataTracker().get(DRAGON_AGE);
    }

    public void setDragonAge(int i) {
        this.getDataTracker().set(DRAGON_AGE, i);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.setAir(200);
            this.updateEggCondition();
        }
    }

    public void updateEggCondition() {
        DragonType dragonType = this.getEggType().dragonType;

        if (dragonType == DragonType.FIRE) {
            if (BlockUtil.isBurning(this.getWorld().getBlockState(this.getBlockPos()))) {
                this.setDragonAge(this.getDragonAge() + 1);
            }
        } else if (dragonType == DragonType.ICE) {
            BlockState state = this.getWorld().getBlockState(this.getBlockPos());
            if (state.isOf(Blocks.WATER) && this.getRandom().nextInt(500) == 0) {
                this.getWorld().setBlockState(this.getBlockPos(), IafBlocks.EGG_IN_ICE.getDefaultState());
                this.getWorld().playSound(this.getX(), this.getY() + this.getStandingEyeHeight(), this.getZ(), SoundEvents.BLOCK_GLASS_BREAK, this.getSoundCategory(), 2.5F, 1.0F, false);
                if (this.getWorld().getBlockEntity(this.getBlockPos()) instanceof BlockEntityEggInIce eggInIce) {
                    eggInIce.type = this.getEggType();
                    eggInIce.ownerUUID = this.getOwnerId();
                }
                this.remove(RemovalReason.DISCARDED);
            }
        } else if (dragonType == DragonType.LIGHTNING) {
            BlockPos.Mutable mutablePosition = new BlockPos.Mutable(this.getX(), this.getY(), this.getZ());
            boolean isRainingAt = this.getWorld().hasRain(mutablePosition) || this.getWorld().hasRain(mutablePosition.set(this.getX(), this.getY() + (double) this.getHeight(), this.getZ()));

            if (this.getWorld().isSkyVisible(this.getBlockPos().up()) && isRainingAt) {
                this.setDragonAge(this.getDragonAge() + 1);
            }
        }

        if (this.getDragonAge() > IafConfig.getInstance().dragon.eggBornTime) {
            this.getWorld().setBlockState(this.getBlockPos(), Blocks.AIR.getDefaultState());
            EntityDragonBase dragon = dragonType.getEntity().create(this.getWorld());

            if (this.hasCustomName()) {
                assert dragon != null;
                dragon.setCustomName(this.getCustomName());
            }

            if (dragonType == DragonType.LIGHTNING) {
                assert dragon != null;
                dragon.setVariant(this.getEggType().ordinal() - 8);
            } else {
                assert dragon != null;
                dragon.setVariant(this.getEggType().ordinal());
            }

            dragon.setGender(this.getRandom().nextBoolean());
            dragon.setPosition(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1, this.getBlockPos().getZ() + 0.5);
            dragon.setHunger(50);

            if (!this.getWorld().isClient()) {
                this.getWorld().spawnEntity(dragon);
            }

            if (this.hasCustomName()) { // FIXME :: Why do this again?
                dragon.setCustomName(this.getCustomName());
            }

            dragon.setTamed(true);
            dragon.setOwnerUuid(this.getOwnerId());

            if (dragonType == DragonType.LIGHTNING) {
                LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(this.getWorld());
                assert bolt != null;
                bolt.setPosition(this.getX(), this.getY(), this.getZ());
                bolt.setCosmetic(true);

                if (!this.getWorld().isClient()) {
                    this.getWorld().spawnEntity(bolt);
                }

                this.getWorld().playSound(this.getX(), this.getY() + this.getStandingEyeHeight(), this.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, this.getSoundCategory(), 2.5F, 1.0F, false);
            } else if (dragonType == DragonType.FIRE) {
                this.getWorld().playSound(this.getX(), this.getY() + this.getStandingEyeHeight(), this.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, this.getSoundCategory(), 2.5F, 1.0F, false);
            }

            this.getWorld().playSound(this.getX(), this.getY() + this.getStandingEyeHeight(), this.getZ(), IafSounds.EGG_HATCH, this.getSoundCategory(), 2.5F, 1.0F, false);
            this.remove(RemovalReason.DISCARDED);
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
    public boolean damage(DamageSource var1, float var2) {
        if (var1.isIn(DamageTypeTags.IS_FIRE) && this.getEggType().dragonType == DragonType.FIRE)
            return false;
        if (!this.getWorld().isClient && !var1.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) && !this.isRemoved()) {
            this.dropItem(this.getItem().getItem(), 1);
        }
        this.remove(RemovalReason.KILLED);
        return true;
    }

    private ItemStack getItem() {
        return switch (this.getEggType().ordinal()) {
            default -> new ItemStack(IafItems.DRAGONEGG_RED);
            case 1 -> new ItemStack(IafItems.DRAGONEGG_GREEN);
            case 2 -> new ItemStack(IafItems.DRAGONEGG_BRONZE);
            case 3 -> new ItemStack(IafItems.DRAGONEGG_GRAY);
            case 4 -> new ItemStack(IafItems.DRAGONEGG_BLUE);
            case 5 -> new ItemStack(IafItems.DRAGONEGG_WHITE);
            case 6 -> new ItemStack(IafItems.DRAGONEGG_SAPPHIRE);
            case 7 -> new ItemStack(IafItems.DRAGONEGG_SILVER);
            case 8 -> new ItemStack(IafItems.DRAGONEGG_ELECTRIC);
            case 9 -> new ItemStack(IafItems.DRAGONEGG_amethyst);
            case 10 -> new ItemStack(IafItems.DRAGONEGG_COPPER);
            case 11 -> new ItemStack(IafItems.DRAGONEGG_BLACK);
        };
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(PlayerEntity player) {
        this.setOwnerId(player.getUuid());
    }

    @Override
    public boolean isMobDead() {
        return true;
    }
}
