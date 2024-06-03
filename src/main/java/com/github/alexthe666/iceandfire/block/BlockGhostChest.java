package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityGhostChest;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

public class BlockGhostChest extends ChestBlock {

    public BlockGhostChest() {
        super(
            Settings
                .create()
                .mapColor(MapColor.OAK_TAN)
                .instrument(Instrument.BASS)
                .burnable()
                .strength(2.5F)
                .sounds(BlockSoundGroup.WOOD),
                IafTileEntityRegistry.GHOST_CHEST::get
        );
    }

    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityGhostChest(pos, state);
    }

    @Override
    protected @NotNull Stat<Identifier> getOpenStat() {
        return Stats.CUSTOM.getOrCreateStat(Stats.TRIGGER_TRAPPED_CHEST);
    }

    @Override
    public boolean emitsRedstonePower(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(@NotNull BlockState blockState, @NotNull BlockView blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return MathHelper.clamp(ChestBlockEntity.getPlayersLookingInChestCount(blockAccess, pos), 0, 15);
    }

    @Override
    public int getStrongRedstonePower(@NotNull BlockState blockState, @NotNull BlockView blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return side == Direction.UP ? blockState.getWeakRedstonePower(blockAccess, pos, side) : 0;
    }
}
