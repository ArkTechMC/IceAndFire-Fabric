package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.client.particle.IafParticleRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieJar;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import dev.arktechmc.iafextra.network.IafServerNetworkHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.UUID;

public class TileEntityJar extends BlockEntity {

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

    public TileEntityJar(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.PIXIE_JAR.get(), pos, state);
        this.rand = new Random();
        this.hasPixie = true;
    }

    public TileEntityJar(BlockPos pos, BlockState state, boolean empty) {
        super(IafTileEntityRegistry.PIXIE_JAR.get(), pos, state);
        this.rand = new Random();
        this.hasPixie = !empty;
    }

    public static void tick(World level, BlockPos pos, BlockState state, TileEntityJar entityJar) {
        entityJar.ticksExisted++;
        if (level.isClient && entityJar.hasPixie) {
            level.addParticle(IafParticleRegistry.PIXIE_DUST,
                    pos.getX() + 0.5F + (double) (entityJar.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH,
                    pos.getY() + (double) (entityJar.rand.nextFloat() * PARTICLE_HEIGHT),
                    pos.getZ() + 0.5F + (double) (entityJar.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH, EntityPixie.PARTICLE_RGB[entityJar.pixieType][0], EntityPixie.PARTICLE_RGB[entityJar.pixieType][1], EntityPixie.PARTICLE_RGB[entityJar.pixieType][2]);
        }
        if (entityJar.ticksExisted % 24000 == 0 && !entityJar.hasProduced && entityJar.hasPixie) {
            entityJar.hasProduced = true;
            if (!level.isClient) {
                IafServerNetworkHandler.sendToAll(new MessageUpdatePixieJar(pos.asLong(), entityJar.hasProduced));
            }
        }
        if (entityJar.hasPixie && entityJar.hasProduced != entityJar.prevHasProduced && entityJar.ticksExisted > 5) {
            if (!level.isClient) {
                IafServerNetworkHandler.sendToAll(new MessageUpdatePixieJar(pos.asLong(), entityJar.hasProduced));
            } else {
                level.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundCategory.BLOCKS, 1, 1, false);
            }
        }
        entityJar.prevRotationYaw = entityJar.rotationYaw;
        if (entityJar.rand.nextInt(30) == 0) {
            entityJar.rotationYaw = (entityJar.rand.nextFloat() * 360F) - 180F;
        }
        if (entityJar.hasPixie && entityJar.ticksExisted % 40 == 0 && entityJar.rand.nextInt(6) == 0 && level.isClient) {
            level.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_IDLE, SoundCategory.BLOCKS, 1, 1, false);
        }
        entityJar.prevHasProduced = entityJar.hasProduced;
    }

    @Override
    public void writeNbt(NbtCompound compound) {
        compound.putBoolean("HasPixie", this.hasPixie);
        compound.putInt("PixieType", this.pixieType);
        compound.putBoolean("HasProduced", this.hasProduced);
        compound.putBoolean("TamedPixie", this.tamedPixie);
        if (this.pixieOwnerUUID != null) {
            compound.putUuid("PixieOwnerUUID", this.pixieOwnerUUID);
        }
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
        if (compound.containsUuid("PixieOwnerUUID")) {
            this.pixieOwnerUUID = compound.getUuid("PixieOwnerUUID");
        }
        this.pixieItems = DefaultedList.ofSize(1, ItemStack.EMPTY);
        Inventories.readNbt(compound, this.pixieItems);
        super.readNbt(compound);
    }

    public void releasePixie() {
        EntityPixie pixie = new EntityPixie(IafEntityRegistry.PIXIE.get(), this.world);
        pixie.updatePositionAndAngles(this.pos.getX() + 0.5F, this.pos.getY() + 1F, this.pos.getZ() + 0.5F, new Random().nextInt(360), 0);
        pixie.setStackInHand(Hand.MAIN_HAND, this.pixieItems.get(0));
        pixie.setColor(this.pixieType);
        this.world.spawnEntity(pixie);
        this.hasPixie = false;
        this.pixieType = 0;
        pixie.ticksUntilHouseAI = 500;
        pixie.setTamed(this.tamedPixie);
        pixie.setOwnerUuid(this.pixieOwnerUUID);

        if (!this.world.isClient) {
            IafServerNetworkHandler.sendToAll(new MessageUpdatePixieHouse(this.pos.asLong(), false, 0));
        }
    }
}
