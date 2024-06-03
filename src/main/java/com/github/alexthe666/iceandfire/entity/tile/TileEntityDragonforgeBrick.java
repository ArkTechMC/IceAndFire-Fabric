package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

public class TileEntityDragonforgeBrick extends BlockEntity {

    public TileEntityDragonforgeBrick(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_BRICK.get(), pos, state);
    }

    @Override
    public <T> net.minecraftforge.common.util.@NotNull LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.@NotNull Capability<T> capability, Direction facing) {
        if (getConnectedTileEntity() != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            return getConnectedTileEntity().getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    private ICapabilityProvider getConnectedTileEntity() {
        for (Direction facing : Direction.values()) {
            if (world.getBlockEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                return world.getBlockEntity(pos.offset(facing));
            }
        }
        return null;
    }

}
