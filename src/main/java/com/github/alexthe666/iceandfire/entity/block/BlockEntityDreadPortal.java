package com.github.alexthe666.iceandfire.entity.block;

import com.github.alexthe666.iceandfire.registry.IafBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BlockEntityDreadPortal extends BlockEntity {
    private long age;
    private BlockPos exitPortal;
    private boolean exactTeleport;

    public BlockEntityDreadPortal(BlockPos pos, BlockState state) {
        super(IafBlockEntities.DREAD_PORTAL, pos, state);
    }

    public static void tick(World level, BlockPos pos, BlockState state, BlockEntityDreadPortal dreadPortal) {
        ++dreadPortal.age;
    }

    @Override
    public void writeNbt(NbtCompound compound) {
        super.writeNbt(compound);
        compound.putLong("Age", this.age);

        //   compound.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));

        if (this.exactTeleport) {
            compound.putBoolean("ExactTeleport", true);
        }
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        this.age = compound.getLong("Age");

        if (compound.contains("ExitPortal", 10)) {
            this.exitPortal = BlockPos.ORIGIN;
        }

        this.exactTeleport = compound.getBoolean("ExactTeleport");
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public boolean shouldRenderFace(Direction face) {
        return true;
    }
}
