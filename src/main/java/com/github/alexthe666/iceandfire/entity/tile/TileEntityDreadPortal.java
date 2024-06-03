package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TileEntityDreadPortal extends BlockEntity {
    private long age;
    private BlockPos exitPortal;
    private boolean exactTeleport;

    public TileEntityDreadPortal(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DREAD_PORTAL.get(), pos, state);
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
        super.writeNbt(compound);
        compound.putLong("Age", this.age);

        if (this.exitPortal != null) {
            //   compound.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));
        }

        if (this.exactTeleport) {
            compound.putBoolean("ExactTeleport", true);
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);
        this.age = compound.getLong("Age");

        if (compound.contains("ExitPortal", 10)) {
            this.exitPortal = BlockPos.ORIGIN;
        }

        this.exactTeleport = compound.getBoolean("ExactTeleport");
    }

    public static void tick(World level, BlockPos pos, BlockState state, TileEntityDreadPortal dreadPortal) {
        ++dreadPortal.age;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public boolean shouldRenderFace(Direction face) {
        return true;
    }
}
