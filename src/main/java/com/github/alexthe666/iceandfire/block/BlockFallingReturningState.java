package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class BlockFallingReturningState extends FallingBlock {
    public static final BooleanProperty REVERTS = BooleanProperty.of("revert");
    private final BlockState returnState;

    public BlockFallingReturningState(float hardness, float resistance, BlockSoundGroup sound, MapColor color, BlockState revertState) {
        super(Settings.create().mapColor(color).sounds(sound).strength(hardness, resistance).ticksRandomly());

        this.returnState = revertState;
        this.setDefaultState(this.stateManager.getDefaultState().with(REVERTS, Boolean.FALSE));
    }

    public BlockFallingReturningState(float hardness, float resistance, BlockSoundGroup sound, boolean slippery, MapColor color, BlockState revertState) {
        super(Settings.create().mapColor(color).sounds(sound).strength(hardness, resistance).ticksRandomly());

        this.returnState = revertState;
        this.setDefaultState(this.stateManager.getDefaultState().with(REVERTS, Boolean.FALSE));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        super.scheduledTick(state, world, pos, rand);
        if (!world.isClient) {
            if (!world.isAreaLoaded(pos, 3)) return;
            if (state.get(REVERTS) && rand.nextInt(3) == 0)
                world.setBlockState(pos, this.returnState);
        }
    }

    public int getDustColor(BlockState blkst) {
        return -8356741;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
