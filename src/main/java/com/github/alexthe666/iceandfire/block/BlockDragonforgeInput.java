package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockDragonforgeInput extends BlockWithEntity implements IDragonProof {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    private final int dragonType;

    public BlockDragonforgeInput(int dragonType) {
        super(Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).dynamicBounds().strength(40, 500).sounds(BlockSoundGroup.METAL));
        this.dragonType = dragonType;
        this.setDefaultState(this.getStateManager().getDefaultState().with(ACTIVE, Boolean.FALSE));
    }

    static String name(int dragonType) {
        return "dragonforge_%s_input".formatted(DragonType.getNameFromInt(dragonType));
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand handIn, BlockHitResult resultIn) {
        if (!world.isClient) {
            BlockPos forge = this.getConnectedTileEntity(world, pos);
            if (forge != null) {
                NamedScreenHandlerFactory screenHandlerFactory = world.getBlockState(forge).createScreenHandlerFactory(world, forge);
                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.FAIL;
    }

    private BlockPos getConnectedTileEntity(World worldIn, BlockPos pos) {
        for (Direction facing : Direction.values()) {
            BlockPos p = pos.offset(facing);
            if (worldIn.getBlockEntity(p) instanceof TileEntityDragonforge forge && forge.dragonType == this.dragonType && forge.isAssembled())
                return p;
        }
        return null;
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(ACTIVE, meta > 0);
    }

    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    public int getMetaFromState(BlockState state) {
        return state.get(ACTIVE) ? 1 : 0;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void neighborUpdate(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        if (worldIn.getBlockEntity(pos) instanceof TileEntityDragonforgeInput input)
            input.resetCore();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDragonforgeInput(pos, state);
    }
}
