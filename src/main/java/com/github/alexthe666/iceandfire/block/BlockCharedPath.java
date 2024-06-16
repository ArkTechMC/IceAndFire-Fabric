package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.registry.IafBlocks;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BlockCharedPath extends DirtPathBlock {
    public static final BooleanProperty REVERTS = BooleanProperty.of("revert");
    public final int dragonType;

    public BlockCharedPath(int dragonType) {
        super(Settings.create().mapColor(MapColor.DARK_GREEN).pistonBehavior(PistonBehavior.DESTROY).sounds(dragonType != 1 ? BlockSoundGroup.GRAVEL : BlockSoundGroup.GLASS).strength(0.6F).slipperiness(dragonType != 1 ? 0.6F : 0.98F).ticksRandomly().requiresTool());
        this.dragonType = dragonType;
        this.setDefaultState(this.stateManager.getDefaultState().with(REVERTS, Boolean.FALSE));
    }

    public static String getNameFromType(int dragonType) {
        return switch (dragonType) {
            case 0 -> "chared_dirt_path";
            case 1 -> "frozen_dirt_path";
            case 2 -> "crackled_dirt_path";
            default -> "";
        };
    }

    public BlockState getSmushedState(int dragonType) {
        return switch (dragonType) {
            case 0 -> IafBlocks.CHARRED_DIRT.getDefaultState();
            case 1 -> IafBlocks.FROZEN_DIRT.getDefaultState();
            case 2 -> IafBlocks.CRACKLED_DIRT.getDefaultState();
            default -> null;
        };
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        super.scheduledTick(state, world, pos, rand);
        if (!world.isClient) {
            if (!world.isAreaLoaded(pos, 3))
                return;
            if (state.get(REVERTS) && rand.nextInt(3) == 0)
                world.setBlockState(pos, Blocks.DIRT_PATH.getDefaultState());
        }
        if (world.getBlockState(pos.up()).isSolid())
            world.setBlockState(pos, this.getSmushedState(this.dragonType));
        this.updateBlockState(world, pos);
    }

    private void updateBlockState(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos.up()).isSolid())
            worldIn.setBlockState(pos, this.getSmushedState(this.dragonType));
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(REVERTS, meta == 1);
    }

    public int getMetaFromState(BlockState state) {
        return state.get(REVERTS) ? 1 : 0;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
