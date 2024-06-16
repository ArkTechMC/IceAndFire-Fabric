package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.block.BlockEntityGhostChest;
import com.github.alexthe666.iceandfire.registry.IafBlockEntities;
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

public class BlockGhostChest extends ChestBlock {

    public BlockGhostChest() {
        super(Settings.create().mapColor(MapColor.OAK_TAN).instrument(Instrument.BASS).burnable().strength(2.5F).sounds(BlockSoundGroup.WOOD), () -> IafBlockEntities.GHOST_CHEST);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityGhostChest(pos, state);
    }

    @Override
    protected Stat<Identifier> getOpenStat() {
        return Stats.CUSTOM.getOrCreateStat(Stats.TRIGGER_TRAPPED_CHEST);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
        return MathHelper.clamp(ChestBlockEntity.getPlayersLookingInChestCount(blockAccess, pos), 0, 15);
    }

    @Override
    public int getStrongRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
        return side == Direction.UP ? blockState.getWeakRedstonePower(blockAccess, pos, side) : 0;
    }
}
