package com.iafenvoy.iceandfire.entity.block;

import com.iafenvoy.iceandfire.StaticVariables;
import com.iafenvoy.iceandfire.entity.EntityPixie;
import com.iafenvoy.iceandfire.network.ServerNetworkHelper;
import com.iafenvoy.iceandfire.registry.IafBlockEntities;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafParticles;
import com.iafenvoy.iceandfire.registry.IafSounds;
import com.iafenvoy.uranus.ServerHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.UUID;

public class BlockEntityJar extends BlockEntity {

    private static final float PARTICLE_WIDTH = 0.3F;
    private static final float PARTICLE_HEIGHT = 0.6F;
    private final Random rand;
    public boolean hasPixie;
    public boolean prevHasProduced;
    public boolean hasProduced;
    public boolean tamedPixie;
    public UUID pixieOwnerUUID;
    public int pixieType;
    public int ticksExisted;
    public DefaultedList<ItemStack> pixieItems = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public float rotationYaw;
    public float prevRotationYaw;

    public BlockEntityJar(BlockPos pos, BlockState state) {
        super(IafBlockEntities.PIXIE_JAR, pos, state);
        this.rand = new Random();
        this.hasPixie = true;
    }

    public BlockEntityJar(BlockPos pos, BlockState state, boolean empty) {
        super(IafBlockEntities.PIXIE_JAR, pos, state);
        this.rand = new Random();
        this.hasPixie = !empty;
    }

    public static void tick(World level, BlockPos pos, BlockState state, BlockEntityJar entityJar) {
        entityJar.ticksExisted++;
        if (level.isClient && entityJar.hasPixie) {
            level.addParticle(IafParticles.PIXIE_DUST,
                    pos.getX() + 0.5F + (double) (entityJar.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH,
                    pos.getY() + (double) (entityJar.rand.nextFloat() * PARTICLE_HEIGHT),
                    pos.getZ() + 0.5F + (double) (entityJar.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH, EntityPixie.PARTICLE_RGB[entityJar.pixieType][0], EntityPixie.PARTICLE_RGB[entityJar.pixieType][1], EntityPixie.PARTICLE_RGB[entityJar.pixieType][2]);
        }
        if (entityJar.ticksExisted % 24000 == 0 && !entityJar.hasProduced && entityJar.hasPixie) {
            entityJar.hasProduced = true;
            if (!level.isClient) {
                PacketByteBuf buf = PacketByteBufs.create().writeBlockPos(pos);
                buf.writeBoolean(entityJar.hasProduced);
                ServerHelper.sendToAll(StaticVariables.UPDATE_PIXIE_JAR, buf);
            }
        }
        if (entityJar.hasPixie && entityJar.hasProduced != entityJar.prevHasProduced && entityJar.ticksExisted > 5) {
            if (!level.isClient) {
                PacketByteBuf buf = PacketByteBufs.create().writeBlockPos(pos);
                buf.writeBoolean(entityJar.hasProduced);
                ServerHelper.sendToAll(StaticVariables.UPDATE_PIXIE_JAR, buf);
            } else
                level.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSounds.PIXIE_HURT, SoundCategory.BLOCKS, 1, 1, false);
        }
        entityJar.prevRotationYaw = entityJar.rotationYaw;
        if (entityJar.rand.nextInt(30) == 0)
            entityJar.rotationYaw = (entityJar.rand.nextFloat() * 360F) - 180F;
        if (entityJar.hasPixie && entityJar.ticksExisted % 40 == 0 && entityJar.rand.nextInt(6) == 0 && level.isClient)
            level.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSounds.PIXIE_IDLE, SoundCategory.BLOCKS, 1, 1, false);
        entityJar.prevHasProduced = entityJar.hasProduced;
    }

    @Override
    public void writeNbt(NbtCompound compound) {
        compound.putBoolean("HasPixie", this.hasPixie);
        compound.putInt("PixieType", this.pixieType);
        compound.putBoolean("HasProduced", this.hasProduced);
        compound.putBoolean("TamedPixie", this.tamedPixie);
        if (this.pixieOwnerUUID != null)
            compound.putUuid("PixieOwnerUUID", this.pixieOwnerUUID);
        compound.putInt("TicksExisted", this.ticksExisted);
        Inventories.writeNbt(compound, this.pixieItems);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void readNbt(NbtCompound compound) {
        this.hasPixie = compound.getBoolean("HasPixie");
        this.pixieType = compound.getInt("PixieType");
        this.hasProduced = compound.getBoolean("HasProduced");
        this.ticksExisted = compound.getInt("TicksExisted");
        this.tamedPixie = compound.getBoolean("TamedPixie");
        if (compound.containsUuid("PixieOwnerUUID"))
            this.pixieOwnerUUID = compound.getUuid("PixieOwnerUUID");
        this.pixieItems = DefaultedList.ofSize(1, ItemStack.EMPTY);
        Inventories.readNbt(compound, this.pixieItems);
        super.readNbt(compound);
    }

    public void releasePixie() {
        EntityPixie pixie = new EntityPixie(IafEntities.PIXIE, this.world);
        pixie.updatePositionAndAngles(this.pos.getX() + 0.5F, this.pos.getY() + 1F, this.pos.getZ() + 0.5F, new Random().nextInt(360), 0);
        pixie.setStackInHand(Hand.MAIN_HAND, this.pixieItems.get(0));
        pixie.setColor(this.pixieType);
        assert this.world != null;
        this.world.spawnEntity(pixie);
        this.hasPixie = false;
        this.pixieType = 0;
        pixie.ticksUntilHouseAI = 500;
        pixie.setTamed(this.tamedPixie);
        pixie.setOwnerUuid(this.pixieOwnerUUID);

        if (!this.world.isClient) {
            PacketByteBuf buf = PacketByteBufs.create().writeBlockPos(this.pos);
            buf.writeBoolean(false).writeInt(0);
            ServerHelper.sendToAll(StaticVariables.UPDATE_PIXIE_HOUSE, buf);
        }
    }
}
