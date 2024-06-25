package com.iafenvoy.iceandfire.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class BlockReturningState extends Block {
    public static final BooleanProperty REVERTS = BooleanProperty.of("revert");
    private final BlockState returnState;

    public BlockReturningState(Settings props, BlockState returnToState) {
        super(props);
        this.returnState = returnToState;
        this.setDefaultState(this.stateManager.getDefaultState().with(REVERTS, Boolean.FALSE));
    }

    public static BlockReturningState builder(float hardness, float resistance, BlockSoundGroup sound, boolean slippery, MapColor color, Instrument instrument, PistonBehavior reaction, boolean ignited, BlockState returnToState) {
        Settings props = Settings.create().mapColor(color).sounds(sound).strength(hardness, resistance).slipperiness(0.98F).ticksRandomly();
        if (instrument != null) props.instrument(instrument);
        if (reaction != null) props.pistonBehavior(reaction);
        if (ignited) props.burnable();
        return new BlockReturningState(props, returnToState);
    }

    public static BlockReturningState builder(float hardness, float resistance, BlockSoundGroup sound, MapColor color, Instrument instrument, PistonBehavior reaction, boolean ignited, BlockState returnToState) {
        Settings props = Settings.create().mapColor(color).sounds(sound).strength(hardness, resistance).ticksRandomly();
        if (instrument != null) props.instrument(instrument);
        if (reaction != null) props.pistonBehavior(reaction);
        if (ignited) props.burnable();
        return new BlockReturningState(props, returnToState);
    }

    // FIXME :: Unused because isRandomlyTicking is not used -> The chunk check might be a performance problem anyway (and potentially not needed)
    @Override
    public void scheduledTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isClient) {
            if (!worldIn.isRegionLoaded(pos.add(-3, -3, -3), pos.add(3, 3, 3)))
                return;
            if (state.get(REVERTS) && rand.nextInt(3) == 0)
                worldIn.setBlockState(pos, this.returnState);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
