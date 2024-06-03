package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntityEggInIce extends BlockEntity {
    public EnumDragonEgg type;
    public int age;
    public int ticksExisted;
    public UUID ownerUUID;
    // boolean to prevent time in a bottle shenanigans
    private boolean spawned;

    public TileEntityEggInIce(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.EGG_IN_ICE.get(), pos, state);
    }

    public static void tickEgg(World level, BlockPos pos, BlockState state, TileEntityEggInIce entityEggInIce) {
        entityEggInIce.age++;
        if (entityEggInIce.age >= IafConfig.dragonEggTime && entityEggInIce.type != null && !entityEggInIce.spawned) {
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

        }
        entityEggInIce.ticksExisted++;
    }

    @Override
    public void writeNbt(@NotNull NbtCompound tag) {
        if (type != null) {
            tag.putByte("Color", (byte) type.ordinal());
        } else {
            tag.putByte("Color", (byte) 0);
        }
        tag.putInt("Age", age);
        if (ownerUUID == null) {
            tag.putString("OwnerUUID", "");
        } else {
            tag.putUuid("OwnerUUID", ownerUUID);
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound tag) {
        super.readNbt(tag);
        type = EnumDragonEgg.values()[tag.getByte("Color")];
        age = tag.getInt("Age");
        UUID s = null;

        if (tag.containsUuid("OwnerUUID")) {
            s = tag.getUuid("OwnerUUID");
        } else {
            try {
                String s1 = tag.getString("OwnerUUID");
                s = ServerConfigHandler.getPlayerUuidByName(this.world.getServer(), s1);
            } catch (Exception ignored) {
            }
        }
        if (s != null) {
            ownerUUID = s;
        }
    }

    @Override
    public void handleUpdateTag(NbtCompound parentNBTTagCompound) {
        this.readNbt(parentNBTTagCompound);
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtTagCompound = new NbtCompound();
        writeNbt(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        NbtCompound nbtTagCompound = new NbtCompound();
        writeNbt(nbtTagCompound);
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        readNbt(pkt.getNbt());   // read from the nbt in the packet
    }

    public void spawnEgg() {
        if (type != null) {
            EntityDragonEgg egg = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), world);
            egg.setEggType(type);
            egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            egg.setOwnerId(this.ownerUUID);
            if (!world.isClient) {
                world.spawnEntity(egg);
            }
        }
    }
}
