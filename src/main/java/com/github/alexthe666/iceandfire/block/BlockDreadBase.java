package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

public class BlockDreadBase extends BlockGeneric implements IDragonProof, IDreadBlock {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.of("player_placed");

/*    public BlockDreadBase(float hardness, float resistance, SoundType sound) {
        super(hardness, resistance, sound);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLAYER_PLACED, Boolean.FALSE));
    }

    public BlockDreadBase(float hardness, float resistance, SoundType sound, boolean slippery) {
        super(hardness, resistance, sound, slippery);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLAYER_PLACED, Boolean.FALSE));
    }*/

    public static BlockDreadBase builder(float hardness, float resistance, BlockSoundGroup sound, MapColor color, Instrument instrument, boolean ignited) {
        Settings props = Settings.create()
                .mapColor(color)
                .sounds(sound)
                .strength(hardness, resistance);
        if (instrument != null) {
            props.instrument(instrument);
        }
        if (ignited) {
            props.burnable();
        }
        return new BlockDreadBase(props);
    }

    public BlockDreadBase(Settings props) {
        super(props);
    }

    @SuppressWarnings("deprecation")
    @Override
    public float calcBlockBreakingDelta(BlockState state, @NotNull PlayerEntity player, @NotNull BlockView worldIn, @NotNull BlockPos pos) {
        if (state.get(PLAYER_PLACED)) {
            float f = 8f;
            //Code from super method
            return player.getDigSpeed(state, pos) / f / (float) 30;
        }
        return super.calcBlockBreakingDelta(state, player, worldIn, pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        return this.getDefaultState().with(PLAYER_PLACED, true);
    }

}
