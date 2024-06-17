package com.iafenvoy.iceandfire.block;

import com.iafenvoy.iceandfire.block.util.IDreadBlock;
import com.iafenvoy.iceandfire.entity.block.BlockEntityDreadSpawner;
import com.iafenvoy.iceandfire.registry.IafBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDreadSpawner extends SpawnerBlock implements IDreadBlock {

    public BlockDreadSpawner() {
        super(Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).strength(10.0F, 10000F).sounds(BlockSoundGroup.METAL).nonOpaque().dynamicBounds());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityDreadSpawner(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, IafBlockEntities.DREAD_SPAWNER, world.isClient ? BlockEntityDreadSpawner::clientTick : BlockEntityDreadSpawner::serverTick);
    }
}
