package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadSpawner;
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
import org.jetbrains.annotations.NotNull;

public class BlockDreadSpawner extends SpawnerBlock implements IDreadBlock {

    public BlockDreadSpawner() {
        super(
                Settings
                        .create()
                        .mapColor(MapColor.STONE_GRAY)
                        .instrument(Instrument.BASEDRUM)
                        .strength(10.0F, 10000F)
                        .sounds(BlockSoundGroup.METAL)
                        .nonOpaque()
                        .dynamicBounds()
        );
    }

    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityDreadSpawner(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World p_154683_, @NotNull BlockState p_154684_, @NotNull BlockEntityType<T> p_154685_) {
        return checkType(p_154685_, IafTileEntityRegistry.DREAD_SPAWNER.get(), p_154683_.isClient ? TileEntityDreadSpawner::clientTick : TileEntityDreadSpawner::serverTick);
    }

}
