package com.iafenvoy.iceandfire.entity.block;

import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.EntityDragonEgg;
import com.iafenvoy.iceandfire.entity.EntityIceDragon;
import com.iafenvoy.iceandfire.enums.EnumDragonEgg;
import com.iafenvoy.iceandfire.registry.IafBlockEntities;
import com.iafenvoy.iceandfire.registry.IafEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEntityEggInIce extends BlockEntity {
    public EnumDragonEgg type;
    public int age;
    public int ticksExisted;
    public UUID ownerUUID;
    // boolean to prevent time in a bottle shenanigans
    private boolean spawned;

    public BlockEntityEggInIce(BlockPos pos, BlockState state) {
        super(IafBlockEntities.EGG_IN_ICE, pos, state);
    }

    public static void tickEgg(World level, BlockPos pos, BlockState state, BlockEntityEggInIce entityEggInIce) {
        entityEggInIce.age++;
        if (entityEggInIce.age >= IafConfig.getInstance().dragonEggTime && entityEggInIce.type != null && !entityEggInIce.spawned)
            if (!level.isClient) {
                EntityIceDragon dragon = new EntityIceDragon(level);
                dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                dragon.setVariant(entityEggInIce.type.ordinal() - 4);
                dragon.setGender(ThreadLocalRandom.current().nextBoolean());
                dragon.setTamed(true);
                dragon.setHunger(50);
                dragon.setOwnerUuid(entityEggInIce.ownerUUID);
                level.spawnEntity(dragon);
                entityEggInIce.spawned = true;
                level.breakBlock(pos, false);
                level.setBlockState(pos, Blocks.WATER.getDefaultState());
            }
        entityEggInIce.ticksExisted++;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        if (this.type != null) tag.putByte("Color", (byte) this.type.ordinal());
        else tag.putByte("Color", (byte) 0);
        tag.putInt("Age", this.age);
        if (this.ownerUUID == null) tag.putString("OwnerUUID", "");
        else tag.putUuid("OwnerUUID", this.ownerUUID);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.type = EnumDragonEgg.values()[tag.getByte("Color")];
        this.age = tag.getInt("Age");
        UUID s = null;
        if (tag.containsUuid("OwnerUUID"))
            s = tag.getUuid("OwnerUUID");
        else
            try {
                String s1 = tag.getString("OwnerUUID");
                assert this.world != null;
                s = ServerConfigHandler.getPlayerUuidByName(this.world.getServer(), s1);
            } catch (Exception ignored) {
            }
        if (s != null) this.ownerUUID = s;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtTagCompound = new NbtCompound();
        this.writeNbt(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        NbtCompound nbtTagCompound = new NbtCompound();
        this.writeNbt(nbtTagCompound);
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void spawnEgg() {
        if (this.type != null) {
            EntityDragonEgg egg = new EntityDragonEgg(IafEntities.DRAGON_EGG, this.world);
            egg.setEggType(this.type);
            egg.setPosition(this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5);
            egg.setOwnerId(this.ownerUUID);
            assert this.world != null;
            if (!this.world.isClient)
                this.world.spawnEntity(egg);
        }
    }
}
